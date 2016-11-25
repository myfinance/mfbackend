/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketData.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice;

import de.hf.dac.marketdataprovider.restservice.marketdataresources.EnvironmentResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Singleton;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("/marketdata")
@Api(value = "marketdata")
@Singleton
public class MarketData extends TopLevelWithEnvironments{

    @Path("/environments/{envID}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Environment",
        response = EnvironmentResource.class)
    public EnvironmentResource getEnvironment(@PathParam("envID") @ApiParam(value="The Service Environment") String envID) throws SQLException {
        return new EnvironmentResource(getMarketDataEnvironment(envID));
    }

}