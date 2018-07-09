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


package com.entersekt.otp;

import static com.entersekt.api.config.APIConfigConstants.APIPASSWORD;
import static com.entersekt.api.config.APIConfigConstants.APIUSER;
import static com.entersekt.api.config.APIConfigConstants.EMCERTID;
import static com.entersekt.api.config.APIConfigConstants.ENDPOINTOTPURL;
import static com.entersekt.api.config.APIConfigConstants.OTPEXPIRE;
import static com.entersekt.api.config.APIConfigConstants.SERVICEID;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.ONE_TIME_PASSWORD;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.AbstractDecisionNode;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.core.CoreWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entersekt.api.utility.EntersektOTPClientUtility;
import com.entersekt.push.pojo.VerifyOTPRq;
import com.entersekt.restlet.client.EntersektRestletClient;
import com.google.inject.assistedinject.Assisted;
import com.sun.identity.sm.RequiredValueValidator;

/** 
 * A node that checks to see if zero-page login headers have specified user name and shared key 
 * for this request. 
 */
@Node.Metadata(outcomeProvider  = AbstractDecisionNode.OutcomeProvider.class,
               configClass      = ValidateEntersktOTP.Config.class)
public class ValidateEntersktOTP extends AbstractDecisionNode {

    private final Config config;
    private final Logger logger = LoggerFactory.getLogger("entersektAuth");
    private EntersektOTPClientUtility eotpc = null;
    EntersektRestletClient erc = null;

    /**
     * Configuration for the node.
     */
    public interface Config {
    	
    	@Attribute(order = 100, validators = {RequiredValueValidator.class})
    	default Map<String, String> cfgTransaktOTPEndpoint() {
         final Map<String, String> attributeMappingConfiguration = new HashMap<>();
         attributeMappingConfiguration.put(ENDPOINTOTPURL, "");
         attributeMappingConfiguration.put(APIUSER, "");
         attributeMappingConfiguration.put(APIPASSWORD, "");
         attributeMappingConfiguration.put(SERVICEID, "");
         attributeMappingConfiguration.put(OTPEXPIRE, "30");
        
        
         return attributeMappingConfiguration;
     }
    	
    }


    /**
     * Create the node.
     * @param config The service config.
     * @throws NodeProcessException If the configuration was not valid.
     */
    @Inject
    public ValidateEntersktOTP(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        eotpc = new EntersektOTPClientUtility(this.config.cfgTransaktOTPEndpoint());
		erc = eotpc.getOtpClient();
    }

    @Override
    public Action process(TreeContext context) throws NodeProcessException {
    	logger.debug("enter into verify otp...");
    	return checkOTP(eotpc.getOtpReuest(context.sharedState.get(USERNAME).asString(),context.transientState.get(ONE_TIME_PASSWORD).asString(),context.sharedState.get(EMCERTID).asString(),""));
    }
    
	private Action checkOTP(VerifyOTPRq otp) {
		logger.debug("enter into checkOTP...");
		return goTo(erc.verifyOTP(otp).equalsIgnoreCase("true") ? true : false).build();

	}
    
   
}