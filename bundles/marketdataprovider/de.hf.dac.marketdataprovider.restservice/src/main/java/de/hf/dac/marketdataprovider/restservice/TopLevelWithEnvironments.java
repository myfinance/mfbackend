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

import de.hf.dac.common.BaseDS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import java.security.AccessController;

public abstract class TopLevelWithEnvironments extends BaseDS {

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
