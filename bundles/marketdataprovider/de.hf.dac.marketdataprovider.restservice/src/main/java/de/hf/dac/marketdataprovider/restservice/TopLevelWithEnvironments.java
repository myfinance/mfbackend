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

import javax.security.auth.Subject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.AccessController;
import java.sql.SQLException;
import java.util.List;

public abstract class TopLevelWithEnvironments {

    protected MarketDataEnvironmentBuilder marketDataEnvironmentBuilder;
    //we need this abstract method to override this with Reference Annotation in the Component because we can not Reference in abstract classes
    public abstract void setMarketDataEnvironmentBuilder(MarketDataEnvironmentBuilder marketDataEnvironmentBuilder);

    protected MarketDataEnvironment getMarketDataEnvironment(String env) throws SQLException {
        return marketDataEnvironmentBuilder.build(env);
    }

    protected SecurityService securityService;

    public abstract void setSecurityService(SecurityService securityService);

    protected AuthorizationSubject getAuthorization() {
        return securityService.getAuthorizationSubject(Subject.getSubject(AccessController.getContext()));
    }

    @GET
    @Path("/getEnvironments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Environments",
        response = List.class)
    public List<String> getEnvironments() {

        return marketDataEnvironmentBuilder.getInfo();
    }

}
