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


package com.entersekt.emcert;

import static org.forgerock.openam.auth.node.api.SharedStateConstants.REALM;
import static org.forgerock.openam.auth.node.api.SharedStateConstants.USERNAME;
import static com.entersekt.api.config.APIConfigConstants.EMCERTID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.NodeProcessException;
import org.forgerock.openam.auth.node.api.SingleOutcomeNode;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.forgerock.openam.core.CoreWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.assistedinject.Assisted;
import com.iplanet.sso.SSOException;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.idm.IdRepoException;

/** 
 * A node that saves emcertID into data store. 
 */
@Node.Metadata(outcomeProvider  = SingleOutcomeNode.OutcomeProvider.class,
               configClass      = SaveEmcertID.Config.class)
public class SaveEmcertID extends SingleOutcomeNode {

    private final Config config;
    private final CoreWrapper coreWrapper;
    private final Logger logger = LoggerFactory.getLogger("entersektAuth");
   

    /**
     * Configuration for the node.
     */
    public interface Config {
    	@Attribute(order = 100)
        default String profileAttribute() {return "";
        }
    }


    /**
     * Create the node.
     * @param config The service config.
     * @throws NodeProcessException If the configuration was not valid.
     */
    @Inject
    public SaveEmcertID(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        this.coreWrapper = coreWrapper;
    }

    @Override
    public Action process(TreeContext context) throws NodeProcessException {
    	logger.debug("entered into SaveEmcertID Node");

        AMIdentity userIdentity = coreWrapper.getIdentity(context.sharedState.get(USERNAME).asString(),context.sharedState.get(REALM).asString());

        Map<String, Set> map = new HashMap<String, Set>();
        Set<String> values = new HashSet<String>();
        values.add(context.sharedState.get(EMCERTID).asString());
        map.put(config.profileAttribute(), values);
        try {
                userIdentity.setAttributes(map);
                userIdentity.store();
                logger.debug("emcertid has been stored into datastore");
            } catch (IdRepoException e) {
            	logger.error(" Error storing emcertid  atttibute '{}' ", e);
            } catch (SSOException e) {
            	logger.error("Node exception", e);
            }
        

        return goToNext().build();
    }
}