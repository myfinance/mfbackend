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

import de.hf.dac.marketdataprovider.api.restservice.MarketData;
import de.hf.dac.marketdataprovider.restservice.marketdataresources.EnvironmentResourceImpl;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.PathParam;
import java.sql.SQLException;

public class MarketDataImpl extends TopLevelWithEnvironments implements MarketData{

    public EnvironmentResourceImpl getEnvironment(@PathParam("envID") @ApiParam(value="The Service Environment") String envID) throws SQLException {
        return new EnvironmentResourceImpl(getAuthorization(), getMarketDataEnvironment(envID));
    }

}
