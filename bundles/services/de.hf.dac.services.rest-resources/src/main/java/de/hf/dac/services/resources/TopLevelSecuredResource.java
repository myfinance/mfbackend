/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TopLevelSecuredResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.services.resources;

import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.Secured;
import de.hf.dac.api.security.SecuredResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import java.security.AccessController;

/**
 * base functionality for rest-root-resources for logging and security
 * @param <ACCESS_TYPE> enum with accesstypes for permission check , e.G. Read, Write, Execute
 * @param <RESOURCE_LEVEL> enum with the Resourcelevels of the application for permission check
 */
public abstract class TopLevelSecuredResource<ACCESS_TYPE extends Enum<ACCESS_TYPE>,RESOURCE_LEVEL extends Enum<RESOURCE_LEVEL>> extends
    SecuredResource<ACCESS_TYPE, RESOURCE_LEVEL> {

    public TopLevelSecuredResource(){
        super();
    }

    public <T extends IdentifiableResource<RESOURCE_LEVEL> & Secured> TopLevelSecuredResource(T resource){
        super(resource);
    }

    private static final Logger AUDIT_LOG = LoggerFactory.getLogger("audit");

    @Context
    protected HttpHeaders httpHeaders;

    @Context
    private Request request;

    @Context
    protected UriInfo uriInfo;

    protected void audit() {
        AUDIT_LOG.info("User {} Method {} Resource {} ", Subject.getSubject(AccessController.getContext()).getPrincipals().iterator().next(),
                request.getMethod(),
                uriInfo.getRequestUri().getPath());
    }
}
