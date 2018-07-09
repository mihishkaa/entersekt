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
 * 
 *
 * Copyright 2018 Sacumen.
 */


package com.entersekt.otp;

import static org.forgerock.openam.auth.node.api.Action.send;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.ONE_TIME_PASSWORD;

import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.security.auth.callback.PasswordCallback;

import org.forgerock.guava.common.base.Strings;
import org.forgerock.json.JsonValue;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.SingleOutcomeNode;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.core.CoreWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.assistedinject.Assisted;

/**
 * A node which collects a OTP from the user via a password callback.
 *
 * <p>Places the result in the transient state as 'ONE TIME PASSWORD'.</p>
 */
@Node.Metadata(outcomeProvider  = SingleOutcomeNode.OutcomeProvider.class,
               configClass      = CollectEntersktOTP.Config.class)
public class CollectEntersktOTP extends SingleOutcomeNode {

    private final Config config;
    private static final String BUNDLE = "com/entersekt/otp/CollectEntersktOTP";
    private final Logger logger = LoggerFactory.getLogger("entersektAuth");

    /**
     * Configuration for the node.
     */
    public interface Config {}


    /**
     * Create the node.
     * @param config The service config.
     * @throws NodeProcessException If the configuration was not valid.
     */
    @Inject
    public CollectEntersktOTP(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
       
    }
    
	private Action collectOTP(TreeContext context) {
		ResourceBundle bundle = context.request.locales.getBundleInPreferredLocale(BUNDLE, getClass().getClassLoader());
		return send(new PasswordCallback(bundle.getString("callback.otp"), false)).build();
	}

    @Override
    public Action process(TreeContext context) throws NodeProcessException {
    	logger.debug("CollectEntersktOTP started");
        JsonValue sharedState = context.sharedState;
        JsonValue transientState = context.transientState;
        return context.getCallback(PasswordCallback.class)
                .map(PasswordCallback::getPassword)
                .map(String::new)
                .filter(password -> !Strings.isNullOrEmpty(password))
                .map(password -> {
                	logger.debug("OTP has been collected and plased  into the Transient State");
                    return goToNext()
                        .replaceSharedState(sharedState.copy())
                        .replaceTransientState(transientState.copy().put(ONE_TIME_PASSWORD, password)).build();
                })
                .orElseGet(() -> {
                	logger.debug("Collect Entersekt OTP");
                    return collectOTP(context);
                });
    }
}