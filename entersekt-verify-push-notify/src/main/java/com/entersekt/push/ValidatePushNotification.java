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
import static com.entersekt.api.config.APIConfigConstants.AUTHVERIFYSTATUS;
import static com.entersekt.api.config.APIConfigConstants.ENDPOINTAUTHPOLLURL;
import static com.entersekt.api.config.APIConfigConstants.SERVICEID;
import static com.entersekt.api.config.APIConfigConstants.USERRESPONSEYES;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.forgerock.guava.common.base.Strings;
import org.forgerock.json.JsonValue;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.AbstractDecisionNode;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.core.CoreWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entersekt.api.utility.EntersektPushClientUtility;
import com.entersekt.push.pojo.AuthVerifyResp;
import com.entersekt.restlet.client.EntersektRestletClient;
import com.google.inject.assistedinject.Assisted;
import com.sun.identity.sm.RequiredValueValidator;

/**
 * A node that checks to see if user is responded for push notification
 */
@Node.Metadata(outcomeProvider = AbstractDecisionNode.OutcomeProvider.class, configClass = ValidatePushNotification.Config.class)
public class ValidatePushNotification extends AbstractDecisionNode {

	private final Config config;
	private EntersektRestletClient erc;
	private final Logger logger = LoggerFactory.getLogger("entersektAuth");
	EntersektPushClientUtility epcu = null;

	/**
	 * Configuration for the node.
	 */
	public interface Config {

		@Attribute(order = 100, validators = { RequiredValueValidator.class })
		default Map<String, String> cfgTransaktAuthVerifyEndpoint() {
			final Map<String, String> attributeMappingConfiguration = new HashMap<>();
			attributeMappingConfiguration.put(ENDPOINTAUTHPOLLURL, "");
			attributeMappingConfiguration.put(APIUSER, "");
			attributeMappingConfiguration.put(APIPASSWORD, "");
			attributeMappingConfiguration.put(SERVICEID, "");

			return attributeMappingConfiguration;
		}

	}

	/**
	 * Create the node.
	 * 
	 * @param config
	 *            The service config.
	 * @throws NodeProcessException
	 *             If the configuration was not valid.
	 */
	@Inject
	public ValidatePushNotification(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
		this.config = config;
		final String uri = config.cfgTransaktAuthVerifyEndpoint().get(ENDPOINTAUTHPOLLURL);
		epcu = new EntersektPushClientUtility(this.config.cfgTransaktAuthVerifyEndpoint());
		erc = epcu.getPushClient(uri);
	}

	@Override
	public Action process(TreeContext context) throws NodeProcessException {
		logger.debug("Entered into ValidaePush porcess method");
		return verifyAuth(context);

	}

	private Action verifyAuth(TreeContext context) {
		logger.debug("Entered into verifyAuth  method");
		JsonValue newSharedState = context.sharedState.copy();

		AuthVerifyResp result = erc.validatePushNotify(epcu.getPollAuthReuest(newSharedState.get(AUTHID).asString()));

		if (result != null && !Strings.isNullOrEmpty(result.getSubject_reply())) {
			return goTo(result.getSubject_reply().equalsIgnoreCase(USERRESPONSEYES)).replaceSharedState(newSharedState)
					.build();
		}

		newSharedState.put(AUTHVERIFYSTATUS, result);
		return goTo(false).replaceSharedState(newSharedState).build();

	}
}