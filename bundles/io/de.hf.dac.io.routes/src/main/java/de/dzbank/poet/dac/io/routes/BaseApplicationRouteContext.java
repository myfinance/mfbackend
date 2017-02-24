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

import de.hf.dac.api.io.routes.ApplicationRouteContext;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        if (object == null || uri == null || uri.length() == 0) {
            LOG.info("Unable to send message. uri = {}, message = {}", uri, object);
        }
        if (uri == null) {
            LOG.error("No EndpointURI provided. Unable to forward Messages {}", object);
        } else {
            Endpoint endpoint = ctxt.getEndpoint(uri);
            LOG.info("{}: Sending Job of type {} into {} ", this.contextID, object.getClass().getName(), uri);
            producerTemplate.asyncSendBody(endpoint, object);
        }
    }

    @Override
    public void stop() throws Exception {
        producerTemplate.cleanUp();
        producerTemplate.stop();
        ctxt.stop();
    }

    @Override
    public void start() throws Exception {
        ctxt.start();
    }


}

