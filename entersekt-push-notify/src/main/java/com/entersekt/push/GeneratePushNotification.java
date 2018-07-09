/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2018 Sacumen.
 */


package com.entersekt.push;

import static com.entersekt.api.config.APIConfigConstants.APIPASSWORD;
import static com.entersekt.api.config.APIConfigConstants.APIUSER;
import static com.entersekt.api.config.APIConfigConstants.AUTHID;
import static com.entersekt.api.config.APIConfigConstants.DELIVERED;
import static com.entersekt.api.config.APIConfigConstants.EMCERTID;
import static com.entersekt.api.config.APIConfigConstants.ENDPOINTAUTHPOLLURL;
import static com.entersekt.api.config.APIConfigConstants.ENDPOINTAUTHURL;
import static com.entersekt.api.config.APIConfigConstants.EXPIRED;
import static com.entersekt.api.config.APIConfigConstants.NEGATIVEBUTTON;
import static com.entersekt.api.config.APIConfigConstants.POSITIVEBUTTON;
import static com.entersekt.api.config.APIConfigConstants.RESPONED;
import static com.entersekt.api.config.APIConfigConstants.SENT;
import static com.entersekt.api.config.APIConfigConstants.SERVICEID;
import static com.entersekt.api.config.APIConfigConstants.TEXT;
import static com.entersekt.api.config.APIConfigConstants.TITLE;
import static com.entersekt.api.config.APIConfigConstants.TTLSECONDS;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.forgerock.guava.common.collect.ImmutableList;
import org.forgerock.json.JsonValue;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Action.ActionBuilder;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.OutcomeProvider;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.core.CoreWrapper;
import org.forgerock.util.Strings;
import org.forgerock.util.i18n.PreferredLocales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entersekt.api.utility.EntersektPushClientUtility;
import com.entersekt.push.pojo.AuthRq;
import com.entersekt.push.pojo.AuthVerifyResp;
import com.entersekt.restlet.client.EntersektRestletClient;
import com.google.inject.assistedinject.Assisted;
import com.sun.identity.sm.RequiredValueValidator;

/** 
 * A node that sends push notification to user device
 */
@Node.Metadata(outcomeProvider  = GeneratePushNotification.NotificationOutcomeProvider.class,
               configClass      = GeneratePushNotification.Config.class)
public class GeneratePushNotification implements Node {

    private final Config config;
    private EntersektRestletClient erc;
    private EntersektRestletClient verc;
    private static final String BUNDLE = "com/entersekt/push/GeneratePushNotification";
    EntersektPushClientUtility epcu = null;
    
    
    private final Logger logger = LoggerFactory.getLogger("entersektAuth");

    /**
     * Configuration for the node.
     */
    public interface Config {
    	
    	@Attribute(order = 100, validators = {RequiredValueValidator.class})
    	default Map<String, String> cfgTransaktAuthEndpoint() {
         final Map<String, String> attributeMappingConfiguration = new HashMap<>();
         attributeMappingConfiguration.put(ENDPOINTAUTHURL, "");
         attributeMappingConfiguration.put(APIUSER, "");
         attributeMappingConfiguration.put(APIPASSWORD, "");
         attributeMappingConfiguration.put(SERVICEID, "");
         attributeMappingConfiguration.put(TEXT, "Do you want to make a payment?");
         attributeMappingConfiguration.put(TITLE, "Confirm Payment");
         attributeMappingConfiguration.put(POSITIVEBUTTON, "yes");
         attributeMappingConfiguration.put(NEGATIVEBUTTON, "no");
         attributeMappingConfiguration.put(TTLSECONDS, "60");
         attributeMappingConfiguration.put(ENDPOINTAUTHPOLLURL, "");
         
         
         return attributeMappingConfiguration;
     }
    	
    }


    /**
     * Create the node.
     * @param config The service config.
     * @throws NodeProcessException If the configuration was not valid.
     */
    @Inject
    public GeneratePushNotification(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        epcu = new EntersektPushClientUtility(this.config.cfgTransaktAuthEndpoint());
        List<EntersektRestletClient> rc = epcu.getPushClient();
        erc= rc.get(0);
        verc = rc.get(1);
    }

    @Override
    public Action process(TreeContext context) throws NodeProcessException {
    	logger.debug("entered into process of genereate push notification");
        ActionBuilder	action=generateAuth(epcu.getSignupReuest(context.sharedState.get(USERNAME).asString(),context.sharedState.get(EMCERTID).asString()),context);
    	return  action.replaceSharedState(context.sharedState.copy()).build();
    }
    
	private ActionBuilder generateAuth(AuthRq auth, TreeContext context) {
        logger.debug("entered into generateAuth");
		ActionBuilder action = null;
		String result = erc.pushNotify(auth);
		
		logger.debug("push notify  auth_id ",result);

		if (!Strings.isNullOrEmpty(result)) {
		   	
			AuthVerifyResp avresp = verc.validatePushNotify(epcu.getPollAuthReuest(result));
			
			logger.debug("validate reponse ponse after push notification:",avresp);

			if (!Strings.isNullOrEmpty(avresp.getStatus())) {
				
				switch (avresp.getStatus()) {
				case  RESPONED :
					action = goTo(PushOutcome.RESPONSED);
					break;

				case SENT:
					action = goTo(PushOutcome.SENT);
					break;

				case EXPIRED:
					action = goTo(PushOutcome.EXPIRED);
					break;

				case DELIVERED:
					action = goTo(PushOutcome.DELIVERED);
					break;

				default:
					action = goTo(PushOutcome.ERROR);

				}
			}

		}
		context.sharedState.put(AUTHID, result);
		return action;
	}
	
	
	
	
	 public enum PushOutcome {
	        
	        EXPIRED,
	       
	        DELIVERED,
	       
	        SENT,
	       
	        RESPONSED,
	        
	        ERROR
	    }
	
	public static class NotificationOutcomeProvider implements OutcomeProvider {
        @Override
        public List<Outcome> getOutcomes(PreferredLocales locales, JsonValue nodeAttributes) {
        	 ResourceBundle bundle = locales.getBundleInPreferredLocale(GeneratePushNotification.BUNDLE,
        			 GeneratePushNotification.class.getClassLoader());
            return ImmutableList.of(
                    new Outcome(PushOutcome.EXPIRED.name(), bundle.getString("push.status.expired")),
                    new Outcome(PushOutcome.DELIVERED.name(), bundle.getString("push.status.delivered")),
                    new Outcome(PushOutcome.SENT.name(), bundle.getString("push.status.sent")),
                    new Outcome(PushOutcome.RESPONSED.name(), bundle.getString("push.status.responded")),
                    new Outcome(PushOutcome.ERROR.name(), bundle.getString("push.status.error")));
        }
    }
	
	private ActionBuilder goTo(PushOutcome outcome) {
        return Action.goTo(outcome.name());
    }
}