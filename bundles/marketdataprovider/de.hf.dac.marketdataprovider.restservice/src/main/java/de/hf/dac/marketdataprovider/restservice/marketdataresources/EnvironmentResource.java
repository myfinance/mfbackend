/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentResource.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 25.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice.marketdataresources;

import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.domain.Product;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

public class EnvironmentResource {

    MarketDataEnvironment marketDataEnvironment;

    public EnvironmentResource(MarketDataEnvironment marketDataEnvironment) {
        this.marketDataEnvironment = marketDataEnvironment;
    }

    @GET
    @Path("/productobjects")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get ProductObjects",
        response = List.class)
    public List<Product> getProductObjects() {
        List<Product> returnvalue = null;
        try {
            returnvalue = marketDataEnvironment.getProductService().listProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }
}
