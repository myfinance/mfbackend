/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ContextBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import com.google.inject.Module;
import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.api.io.env.context.ContextBuilder;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.osgi.service.component.annotations.Component;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//@OsgiServiceProvider(classes = { ContextBuilder.class })
//@Singleton
@Component
public class ContextBuilderImpl implements ContextBuilder {

    private Map<String, ApplicationContext> contextCache = Collections.synchronizedMap(new HashMap<>());

    @Override
    public ApplicationContext build(String contextID, Module[] provider) {
        if (contextCache.containsKey(contextID)) {
            return contextCache.get(contextID);
        } else {
            EnvironmentApplicationContext ctxt = new EnvironmentApplicationContext(provider);
            contextCache.put(contextID, ctxt);
            return ctxt;
        }
    }
}
