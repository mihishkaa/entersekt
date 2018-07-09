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
 * Copyright 2018 ForgeRock AS.
 */


package com.entersekt.signup;

import static com.entersekt.api.config.APIConfigConstants.APIPASSWORD;
import static com.entersekt.api.config.APIConfigConstants.APIUSER;
import static com.entersekt.api.config.APIConfigConstants.EMCERTID;
import static com.entersekt.api.config.APIConfigConstants.EXTERNALPASSKEY;
import static com.entersekt.api.config.APIConfigConstants.LENGTH;
import static com.entersekt.api.config.APIConfigConstants.SERVICEID;
import static com.entersekt.api.config.APIConfigConstants.SIGNUPCODE;
import static com.entersekt.api.config.APIConfigConstants.SIGNUPEPURL;
import static com.entersekt.api.config.APIConfigConstants.SIGNUPPOLLURL;
import static com.entersekt.api.config.APIConfigConstants.TTLSECONDS;
import static org.forgerock.openam.auth.node.api.Action.send;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;

import org.forgerock.guava.common.base.Strings;
import org.forgerock.guava.common.collect.ImmutableList;
import org.forgerock.json.JsonValue;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.SingleOutcomeNode;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.core.CoreWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entersekt.api.utility.EntersektSignUpClientUtility;
import com.entersekt.restlet.client.EntersektRestletClient;
import com.google.inject.assistedinject.Assisted;
import com.sun.identity.authentication.callbacks.HiddenValueCallback;
import com.sun.identity.sm.RequiredValueValidator;

/** 
 * A node that generate signup code and display to user for registering his/her device with gateway. 
 */
@Node.Metadata(outcomeProvider  = SingleOutcomeNode.OutcomeProvider.class,
               configClass      = ShowSignupCode.Config.class)
public class ShowSignupCode extends SingleOutcomeNode {

    private final Config config;
    private final Logger logger = LoggerFactory.getLogger("entersektAuth");
    EntersektRestletClient ercsc = null;
    EntersektRestletClient ercpsc = null;
    private static final String BUNDLE = "com/entersekt/signup/ShowSignupCode";
    EntersektSignUpClientUtility ecu = null;
    

    /**
     * Configuration for the node.
     */
    public interface Config {
   
    	@Attribute(order = 100, validators = {RequiredValueValidator.class})
    	default Map<String, String> cfgTransaktEndpoint() {
         final Map<String, String> attributeMappingConfiguration = new HashMap<>();
         attributeMappingConfiguration.put(SIGNUPEPURL, "");
         attributeMappingConfiguration.put(SIGNUPPOLLURL, "");
         attributeMappingConfiguration.put(APIUSER, "");
         attributeMappingConfiguration.put(APIPASSWORD, "");
         attributeMappingConfiguration.put(SERVICEID, "");
         attributeMappingConfiguration.put(TTLSECONDS, "60");
         attributeMappingConfiguration.put(LENGTH, "8");
         attributeMappingConfiguration.put(EXTERNALPASSKEY, "");
        
         return attributeMappingConfiguration;
     }
    }


    /**
     * Create the node.
     * @param config The service config.
     * @throws NodeProcessException If the configuration was not valid.
     */
    @Inject
    public ShowSignupCode(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        ecu = new EntersektSignUpClientUtility(this.config.cfgTransaktEndpoint());
        List<EntersektRestletClient> clist= ecu.getSignupClient();
        ercsc =  clist.get(0);
        ercpsc = clist.get(1);
       
    }
    
	private Action showSignUpCode(TreeContext context) {
		List<Callback> callbacks = new ArrayList<Callback>(2);
		String signupcode = ercsc.getSignUpCode(ecu.getSignupReuest(context.sharedState.get(USERNAME).asString(), ""));

		HiddenValueCallback signupCallback = new HiddenValueCallback(SIGNUPCODE, signupcode);
		ResourceBundle bundle = context.request.locales.getBundleInPreferredLocale(BUNDLE, getClass().getClassLoader());
		NameCallback nameCallback = new NameCallback(bundle.getString("callback.signupcode") + signupcode);
		HiddenValueCallback emcertHidden = new HiddenValueCallback(EMCERTID, "");

		callbacks.add(signupCallback);
		callbacks.add(emcertHidden);
		callbacks.add(nameCallback);
		return send(ImmutableList.copyOf(callbacks)).build();
	}

    @Override
	public Action process(TreeContext context) throws NodeProcessException {

		logger.debug("show signup code started");
		JsonValue sharedState = context.sharedState;

		return context.getCallback(HiddenValueCallback.class).map(HiddenValueCallback::getValue).map(String::new)
				.filter(value -> !Strings.isNullOrEmpty(value)).map(value -> {
					logger.debug("User has been signed up with code and clicked the login button");

					String emcertID = ercpsc.pollSignup(
							ecu.getPollSignupReuest(context.sharedState.get(USERNAME).asString(), ""));

					return goToNext().replaceSharedState(sharedState.copy().put(EMCERTID, emcertID)).build();
				}).orElseGet(() -> {
					logger.debug("showing signup code");
					return showSignUpCode(context);
				});

	}
 

    
}