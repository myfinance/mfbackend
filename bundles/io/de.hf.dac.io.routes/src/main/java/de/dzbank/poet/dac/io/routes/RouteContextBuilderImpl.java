/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RouteContextBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 01.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.dzbank.poet.dac.io.routes;

import de.hf.dac.api.io.routes.ApplicationRouteContext;
import de.hf.dac.api.io.routes.RouteContextBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultCamelContextNameStrategy;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component(service = RouteContextBuilder.class, scope = ServiceScope.SINGLETON, name = "DAC.RouteContextBuilderImpl")
public class RouteContextBuilderImpl implements RouteContextBuilder {

    private Map<String, ApplicationRouteContext> contextCache = Collections.synchronizedMap(new HashMap<>());

    @Override
    public ApplicationRouteContext build(String contextID, ClassLoader messageJaxbClassLoaders, RouteBuilder... providers) throws Exception {
        ApplicationRouteContext result = contextCache.get(contextID);
        if (result == null) {
            CamelContext ctxt = new DefaultCamelContext();
            ctxt.setNameStrategy(new DefaultCamelContextNameStrategy(contextID));
            ctxt.setApplicationContextClassLoader(messageJaxbClassLoaders);

            for (RouteBuilder provider : providers) {
                ctxt.addRoutes(provider);
            }

            // start camel context
            ctxt.start();

            result = new BaseApplicationRouteContext(contextID, ctxt);
            contextCache.put(contextID, result);
        }

        return result;

    }

    @Override
    public void remove(String contextID) {
        if (contextCache.containsKey(contextID)) {contextCache.remove(contextID);}
    }

    @Override
    public void sendMessage(String contextID, String uri, Object jaxbMessage) {
        if (contextCache.containsKey(contextID)) {
            contextCache.get(contextID).sendMessage(uri, jaxbMessage);
        }
    }

}
