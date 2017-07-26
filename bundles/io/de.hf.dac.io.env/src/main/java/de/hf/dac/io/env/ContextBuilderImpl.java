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
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Designate(ocd = ContextBuilderConfiguration.class)
@Component(service = { ContextBuilder.class }, name = "DAC.ContextBuilderImpl")
public class ContextBuilderImpl implements ContextBuilder {

    private Map<String, ApplicationContext> contextCache = Collections.synchronizedMap(new HashMap<>());
    private ContextBuilderConfiguration builderConfiguration;

    @Activate
    public void activate(BundleContext bc, ContextBuilderConfiguration builderConfiguration) {
        this.builderConfiguration = builderConfiguration;
    }

    @Override
    public synchronized ApplicationContext build(String contextID, Module[] provider) {
        return buildInternal(contextID, provider, builderConfiguration.enableCaching());
    }

    private ApplicationContext buildInternal(String contextID, Module[] provider, boolean cacheEnabled) {
        if (cacheEnabled && contextCache.containsKey(contextID)) {
            return contextCache.get(contextID);
        } else {
            EnvironmentApplicationContext ctxt = new EnvironmentApplicationContext(contextID, provider, this);
            if (cacheEnabled) {
                contextCache.put(contextID, ctxt);
            }
            return ctxt;
        }
    }

    @Override
    public synchronized void clearCache() {
        contextCache.clear();
    }

    @Override
    public synchronized void remove(String id) {
        this.contextCache.remove(id);
    }
}
