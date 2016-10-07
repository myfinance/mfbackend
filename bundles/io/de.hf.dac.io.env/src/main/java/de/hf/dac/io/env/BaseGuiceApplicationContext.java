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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.hf.dac.api.io.env.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

abstract public class BaseGuiceApplicationContext implements ApplicationContext {
    private final Module[] provider;
    private final Injector injector;

    private final Map<Class, Object> localInstanceCache = new HashMap<>();

    public BaseGuiceApplicationContext(Module[] provider) {
        this.provider = provider;
        Module[] allModules = new Module[provider.length + 1];
        System.arraycopy(provider, 0, allModules, 0, provider.length);
        allModules[allModules.length - 1] = new ProviderModule(this);
        this.injector = Guice.createInjector(allModules);
    }

    @Override
    public <T> T autowire(Class<T> instanceClass) {
        return this.injector.getInstance(instanceClass);
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
}
