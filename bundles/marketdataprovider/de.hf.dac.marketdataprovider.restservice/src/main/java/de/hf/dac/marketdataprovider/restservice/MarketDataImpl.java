/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice;

import de.hf.dac.marketdataprovider.api.restservice.EnvironmentResource;
import de.hf.dac.marketdataprovider.restservice.marketdataresources.EnvironmentResourceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("/marketdata")
@Api(value = "marketdata")
public class MarketDataImpl extends TopLevelWithEnvironments{

    @Path("/environments/{envID}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Environment", response = EnvironmentResource.class)
    public EnvironmentResourceImpl getEnvironment(@PathParam("envID") @ApiParam(value="The Service Environment") String envID) throws SQLException {
        return new EnvironmentResourceImpl(getAuthorization(), getMarketDataEnvironment(envID));
    }

}
