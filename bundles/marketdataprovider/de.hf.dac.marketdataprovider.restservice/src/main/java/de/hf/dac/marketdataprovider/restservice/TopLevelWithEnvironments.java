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

import de.hf.dac.api.security.SecuredResource;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import io.swagger.annotations.ApiOperation;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import java.security.AccessController;
import java.sql.SQLException;
import java.util.List;

public abstract class TopLevelWithEnvironments{

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

    protected MarketDataEnvironment getMarketDataEnvironment(String env) throws SQLException {

        return getService(MarketDataEnvironmentBuilder.class).build(env);
    }

    protected void audit() {
        AUDIT_LOG.info("User {} Method {} Resource {} ", Subject.getSubject(AccessController.getContext()).getPrincipals().iterator().next(),
                request.getMethod(),
                uriInfo.getRequestUri().getPath());
    }

    @GET
    @Path("/getEnvironments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Environments",
        response = List.class)
    public List<String> getEnvironments() throws SQLException{

        return getService(MarketDataEnvironmentBuilder.class).getInfo();
    }

}
