/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ApplicationContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.env.context;

/**
 * Autowiring ApplicationContext.
 */
public interface ApplicationContext {

    /**
     * Use prepared context to autowire an instance for this class.
     * @param instanceClass Class to be instantiated
     * @param <T> Type of Instance
     * @return autowired instance
     */
    <T> T autowire(Class<T> instanceClass);

    /**
     * Check if one instance of this interface/class is available in context.
     *
     * @param instanceClass type of instance to search
     * @param <T> Type
     * @return instance if found. Null if no binding found in context
     */
    <T> T getLocalInstance(Class<T> instanceClass);

    ApplicationContext addInstance(Class instanceInterface, Object instance);
}

