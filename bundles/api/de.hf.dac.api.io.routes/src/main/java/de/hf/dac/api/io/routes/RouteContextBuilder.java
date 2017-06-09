/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RouteContextBuilder.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * Service interface used for CamelContext creation.
 */
public interface RouteContextBuilder {

    ApplicationRouteContext build(String contextID, ClassLoader messageJaxbClassLoaders,RouteBuilder... providers);

    void remove(String contextID);

    void sendMessage(String contextID, String uri, Object jaxbMessage);

}
