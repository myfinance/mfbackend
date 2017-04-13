/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TopLevelWithEnvironments.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice;


import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.Secured;
import de.hf.dac.api.security.SecuredResource;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import java.security.AccessController;

public abstract class TopLevelWithEnvironments {

    private static final Logger AUDIT_LOG = LoggerFactory.getLogger("audit");

    @Context
    protected HttpHeaders httpHeaders;

    @Context
    private Request request;

    @Context
    protected UriInfo uriInfo;

    protected <T> T getService(Class<T> clazz) {
        final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        ServiceReference<T> sr = bundleContext.getServiceReference(clazz);

        T service = bundleContext.getService(sr);

        return service;
    }

    protected void audit() {
        AUDIT_LOG.info("User {} Method {} Resource {} ", Subject.getSubject(AccessController.getContext()).getPrincipals().iterator().next(),
                request.getMethod(),
                uriInfo.getRequestUri().getPath());
    }
}
