/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseApplicationRouteContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.dzbank.poet.dac.io.routes;

import de.hf.dac.api.base.exceptions.DACException;
import de.hf.dac.api.base.exceptions.DACMsgKey;
import de.hf.dac.api.io.routes.ApplicationRouteContext;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * handles the camel communication. Builds with the camel context a camel producerTemplate and sends asyn msg
 */
public class BaseApplicationRouteContext implements ApplicationRouteContext {

    public static final Logger LOG = LoggerFactory.getLogger(BaseApplicationRouteContext.class);

    private final String contextID;
    private final CamelContext ctxt;

    private final ProducerTemplate producerTemplate;

    public BaseApplicationRouteContext(String contextID, CamelContext ctxt) {
        this.contextID = contextID;
        this.ctxt = ctxt;
        this.producerTemplate = ctxt.createProducerTemplate();
    }

    @Override
    public String getContextId() {
        return this.contextID;
    }

    @Override
    public void sendMessage(String uri, Object object) {
        if (object == null || uri == null || uri.isEmpty()) {
            LOG.info("Unable to send message. uri = {}, message = {}", uri, object);
        } else {
            Endpoint endpoint = ctxt.getEndpoint(uri);
            LOG.info("{}: Sending Job of type {} into {} ", this.contextID, object.getClass().getName(), uri);
            producerTemplate.asyncSendBody(endpoint, object);
        }
    }

    @Override
    public void stop() {
        producerTemplate.cleanUp();
        try {
            producerTemplate.stop();
            ctxt.stop();
        } catch(Exception ex) {
            throw new DACException(DACMsgKey.CAMEL_STOP,"Can't stop",ex);
        }
    }

    @Override
    public void start() {
        try {
            ctxt.start();
        } catch(Exception ex) {
            throw new DACException(DACMsgKey.CAMEL_START,"Can't stop",ex);
        }
    }


}