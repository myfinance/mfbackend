/** ----------------------------------------------------------------------------
 *
 * ---                             ---
 *              Copyright (c) 2015, ... All Rights Reserved
 *
 *
 *  Project     : finance-resteasy
 *
 *  File        : JaxRsActivator.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 13.03.2015
 *
 * ----------------------------------------------------------------------------
 */
package de.hf.finance.secureservice.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * A class extending {@link Application} and annotated with @ApplicationPath is the Java EE 6 "no XML" approach to activating
 * JAX-RS.
 * 
 * <p>
 * Resources are served relative to the servlet path specified in the {@link ApplicationPath} annotation.
 * </p>
 */
@ApplicationPath("/rest")
public class JaxRsActivator extends Application {
    /* class body intentionally left blank */
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>(super.getClasses());
        classes.add(FinanceService.class);
        return classes;
    }
}
