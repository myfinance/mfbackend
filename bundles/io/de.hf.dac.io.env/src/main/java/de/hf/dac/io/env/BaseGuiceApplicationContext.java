/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseGuiceApplicationContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.spi.Message;
import de.hf.dac.api.base.exceptions.DACException;
import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.api.io.env.context.ContextBuilder;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class BaseGuiceApplicationContext implements ApplicationContext {
    private final String contextID;
    private final Module[] provider;
    private final ContextBuilder contextBuilder;
    private final Injector injector;

    private final Map<Class, Object> localInstanceCache = new HashMap<>();

    public BaseGuiceApplicationContext(String contextID, Module[] provider, ContextBuilder contextBuilder) {
        this.contextID = contextID;
        this.provider = provider;
        this.contextBuilder = contextBuilder;

        List<Module> all = new ArrayList<>();
        all.addAll(Arrays.asList(provider));
        all.add(new ProviderModule(this));
        all.add(new GuiceLifecycleListenerModule());
        this.injector = Guice.createInjector(all.toArray(new Module[all.size()]));
    }

    @Override
    public <T> T autowire(Class<T> instanceClass) {
        T instance = null;
        try {
            instance = this.injector.getInstance(instanceClass);
        } catch (CreationException creationException) {
            StringBuilder builder = new StringBuilder();
            for (Message message : creationException.getErrorMessages()) {
                builder.append(message.getMessage());
            }
            throw new DACException(builder.toString(), creationException);
        }
        // wrap all interfaces to catch Exceptions and clean contextCache to avoid working with an corrupted Context later on
        if (instanceClass.isInterface()) {
            instance = (T) Proxy
                .newProxyInstance(instanceClass.getClassLoader(),
                    new Class[] { instanceClass },
                    new ExceptionHandlingHandler(instance, this, contextBuilder)
                );
        }
        return instance;
    }

    @Override
    public ApplicationContext addInstance(Class instanceInterface, Object instance) {
        injector.injectMembers(instance);
        localInstanceCache.put(instanceInterface, instance);
        return this;
    }

    @Override
    public <T> T getLocalInstance(Class<T> instanceClass) {
        return (T) localInstanceCache.get(instanceClass);
    }

    @Override
    public String getId() {
        return contextID;
    }
}
