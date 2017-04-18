/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseDS.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 18.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.common;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Declarative Services can not reference OSGI Components in abstract classes with the Reference annotation, so we have to use the bundle-context
 */
public abstract class BaseDS {
    protected <T> T getService(Class<T> clazz) {
        final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        ServiceReference<T> sr = bundleContext.getServiceReference(clazz);

        T service = bundleContext.getService(sr);

        return service;
    }
}
