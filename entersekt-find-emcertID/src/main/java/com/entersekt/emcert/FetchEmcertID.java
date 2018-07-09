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


package com.entersekt.emcert;

import static org.forgerock.openam.auth.node.api.SharedStateConstants.REALM;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;
import static com.entersekt.api.config.APIConfigConstants.EMCERTID;

import java.util.Set;

import javax.inject.Inject;

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

import com.google.inject.assistedinject.Assisted;
import com.iplanet.sso.SSOException;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.idm.IdRepoException;

/** 
 * A node that checks to see if emcertID is presented in data store or not 
 * for this request. 
 */
@Node.Metadata(outcomeProvider  = AbstractDecisionNode.OutcomeProvider.class,
               configClass      = FetchEmcertID.Config.class)
public class FetchEmcertID extends AbstractDecisionNode {

    private final Config config;
    private final CoreWrapper coreWrapper;
    private final Logger logger = LoggerFactory.getLogger("entersektAuth");

    /**
     * Configuration for the node.
     */
	public interface Config {

		// Property to search for
		@Attribute(order = 100)
		default String profileAttribute() {
			return "";
		}

	}


    /**
     * Create the node.
     * @param config The service config.
     * @throws NodeProcessException If the configuration was not valid.
     */
    @Inject
    public FetchEmcertID(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        this.coreWrapper = coreWrapper;
    }

    @Override
	public Action process(TreeContext context) throws NodeProcessException {
		
    	logger.debug("FetchEmcertID started");
		JsonValue sharedState = context.sharedState;
		JsonValue transientState = context.transientState;

		// Pull out the user object
		AMIdentity userIdentity = coreWrapper.getIdentity(context.sharedState.get(USERNAME).asString(),
				context.sharedState.get(REALM).asString());

		// Pull out the specified attribute
		logger.debug(" Looking for profile attribute " + config.profileAttribute());

		try {

			Set<String> emcertID = userIdentity.getAttribute(config.profileAttribute());

			if (emcertID == null || emcertID.isEmpty()) {

				logger.debug("Unable to find attribute " + config.profileAttribute()
						+ "on user profile");
				return goTo(false).build();

			} else {

				String attr = emcertID.iterator().next();
				logger.debug( "Found attribute value for: " + config.profileAttribute()
						+ " as " + attr);
				return goTo(true).replaceSharedState(sharedState.copy().put(EMCERTID, attr))
						.replaceTransientState(transientState.copy())
						.build();

			}
		} catch (IdRepoException e) {
			logger.error("Error locating user ", e);
		} catch (SSOException e) {
			logger.error("Error locating user '{}' ", e);
		}
		return goTo(false).build();
	}
}