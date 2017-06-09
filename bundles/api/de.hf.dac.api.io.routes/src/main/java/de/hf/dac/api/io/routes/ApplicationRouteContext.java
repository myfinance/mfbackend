/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ApplicationRouteContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes;

/**
 * Access to underlying routing context.
 */

public interface ApplicationRouteContext {

    String getContextId();

    void sendMessage(String uri, Object jaxbMessage);

    void stop() throws Exception;

    void start() throws Exception;
}
