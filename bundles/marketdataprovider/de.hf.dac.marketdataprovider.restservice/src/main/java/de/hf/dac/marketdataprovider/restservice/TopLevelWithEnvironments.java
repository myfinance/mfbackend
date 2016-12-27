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

import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.security.AuthorizationSubject;
import de.hf.dac.api.security.SecurityService;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import io.swagger.annotations.ApiOperation;
import org.ops4j.pax.cdi.api.OsgiService;
import org.osgi.service.component.annotations.Reference;

import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.AccessController;
import java.sql.SQLException;
import java.util.List;

public abstract class TopLevelWithEnvironments {

    //@Inject
    //@OsgiService
    @Reference
    private MarketDataEnvironmentBuilder marketDataEnvironmentBuilder;

    protected MarketDataEnvironment getMarketDataEnvironment(String env) throws SQLException {
        return marketDataEnvironmentBuilder.build(env);
    }

    @Inject
    @OsgiService
    SecurityService securityService;

    @GET
    @Path("/getEnvironments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Environments",
        response = List.class)
    public List<String> getEnvironments() {

        return marketDataEnvironmentBuilder.getInfo();
    }

    protected AuthorizationSubject getAuthorization() {
        return securityService.getAuthorizationSubject(Subject.getSubject(AccessController.getContext()));
    }
}
