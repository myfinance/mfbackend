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

import de.hf.dac.api.security.SecurityService;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.marketdataprovider.api.restservice.MarketData;
import de.hf.dac.marketdataprovider.restservice.marketdataresources.EnvironmentResourceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.inject.Named;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Component(service = MarketData.class, immediate = true, property={"service.exported.interfaces=*",
    "service.exported.configs=org.apache.cxf.rs",
    "org.apache.cxf.rs.address=/rest/marketdataservice","org.apache.cxf.httpservice.filter=true"})
//@Component(service = MarketData.class, immediate = true)
@Named
public class MarketDataImpl extends TopLevelWithEnvironments implements MarketData{

    public EnvironmentResourceImpl getEnvironment(@PathParam("envID") @ApiParam(value="The Service Environment") String envID) throws SQLException {
        return new EnvironmentResourceImpl(getAuthorization(), getMarketDataEnvironment(envID));
    }

    @Override
    @Reference
    public void setMarketDataEnvironmentBuilder(MarketDataEnvironmentBuilder marketDataEnvironmentBuilder) {
        this.marketDataEnvironmentBuilder = marketDataEnvironmentBuilder;
    }

    @Override
    @Reference
    public void setSecurityService(SecurityService securityService) {
        this.securityService=securityService;
    }
}
