/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : OSGIServiceLookup.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.lookup;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleReference;
import org.osgi.framework.ServiceReference;

public class OSGIServiceLookup {

    public static <T> T getServiceByInterface(Class<?> clazz) {
        BundleContext context = ((BundleReference) OSGIServiceLookup.class.getClassLoader()).getBundle().getBundleContext();
        ServiceReference txRef = context.getServiceReference(clazz.getName());
        T service = (T) context.getService(txRef);
        return service;
    }
}
