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
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import io.swagger.annotations.ApiOperation;
import org.ops4j.pax.cdi.api.OsgiService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

public abstract class TopLevelWithEnvironments {

    @Inject
    @OsgiService
    private MarketDataEnvironmentBuilder marketDataEnvironmentBuilder;

    protected MarketDataEnvironment getMarketDataEnvironment(String env) throws SQLException {
        return marketDataEnvironmentBuilder.build(env);
    }

    @Inject
    @OsgiService
    EnvironmentService environmentService;

    @GET
    @Path("/getEnvironments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Environments",
        response = List.class)
    public List<String> getEnvironments() {

        return marketDataEnvironmentBuilder.getInfo();
    }
}