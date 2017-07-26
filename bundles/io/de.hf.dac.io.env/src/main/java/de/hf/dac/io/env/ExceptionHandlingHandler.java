/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ExceptionHandlingHandler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 26.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import de.hf.dac.api.io.env.context.ContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * wrap an instance of a class T and catch exceptions from there methodes to remove the appcontext from internal cache in that case
 * to avoid working with an corrupted context further on
 */
public class ExceptionHandlingHandler implements InvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingHandler.class);

    private final Object instance;
    private final BaseGuiceApplicationContext baseGuiceApplicationContext;
    private final ContextBuilder contextBuilder;

    public <T> ExceptionHandlingHandler(T instance, BaseGuiceApplicationContext baseGuiceApplicationContext, ContextBuilder contextBuilder) {
        this.instance = instance;
        this.baseGuiceApplicationContext = baseGuiceApplicationContext;
        this.contextBuilder = contextBuilder;
    }

    @Override
    @java.lang.SuppressWarnings("squid:S1181")
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        // all we try to achieve here is to catch exceptions and remove this appcontext from internal cache
        try {
            return method.invoke(this.instance, objects);
        } catch (Throwable throwable) {
            // make sure this context is removed from cache
            LOG.warn("Unhandled {} Exception within GUICE Context. Will remove {} from cache!", throwable.getClass().getName(), this.baseGuiceApplicationContext.getId());
            try {
                if (this.contextBuilder != null) {
                    this.contextBuilder.remove(this.baseGuiceApplicationContext.getId());
                }
            } catch (Throwable unth) {
                LOG.error("Can't remove Context",unth);
                // failsafe
                this.contextBuilder.clearCache();
            }
            // publish
            throw throwable;
        }
    }
}

