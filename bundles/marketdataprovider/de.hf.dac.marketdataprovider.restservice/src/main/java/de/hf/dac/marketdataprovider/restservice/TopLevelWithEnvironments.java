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

import de.hf.dac.api.security.AuthorizationSubject;
import de.hf.dac.api.security.SecurityService;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import io.swagger.annotations.ApiOperation;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import javax.security.auth.Subject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.AccessController;
import java.sql.SQLException;
import java.util.List;

public abstract class TopLevelWithEnvironments {

    protected MarketDataEnvironmentBuilder getMarketDataEnvironmentBuilder() throws SQLException {
        final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        ServiceReference<MarketDataEnvironmentBuilder> sr = bundleContext.getServiceReference(MarketDataEnvironmentBuilder.class);

        return bundleContext.getService(sr);
    }

    protected MarketDataEnvironment getMarketDataEnvironment(String env) throws SQLException {
        final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        ServiceReference<MarketDataEnvironmentBuilder> sr = bundleContext.getServiceReference(MarketDataEnvironmentBuilder.class);

        MarketDataEnvironmentBuilder marketDataEnvironmentBuilder = bundleContext.getService(sr);

        return getMarketDataEnvironmentBuilder().build(env);
    }


    protected AuthorizationSubject getAuthorization() {
        final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        ServiceReference<SecurityService> sr = bundleContext.getServiceReference(SecurityService.class);

        SecurityService securityService = bundleContext.getService(sr);

        return securityService.getAuthorizationSubject(Subject.getSubject(AccessController.getContext()));
    }

    @GET
    @Path("/getEnvironments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Environments",
        response = List.class)
    public List<String> getEnvironments() throws SQLException{

        return getMarketDataEnvironmentBuilder().getInfo();
    }

}
