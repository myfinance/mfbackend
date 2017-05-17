/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentDataResource.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 25.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice.marketdataresources;

import com.google.gson.Gson;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.application.servicecontext.MDEnvironmentContext;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.services.resources.BaseSecuredResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EnvironmentDataResource extends BaseSecuredResource<OpType,OpLevel> {

    MarketDataEnvironment marketDataEnvironment;
    final protected static Gson gson = new Gson();

    public EnvironmentDataResource(MDEnvironmentContext envContext) {
        super(envContext);
        this.marketDataEnvironment = envContext.getMarketDataEnvironment();
    }

    @GET
    @Path("/instruments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = List.class)
    public List<Instrument> getInstruments() {
        checkOperationAllowed(OpType.READ);
        List<Instrument> returnvalue = marketDataEnvironment.getInstrumentService().listInstruments();
        return returnvalue;
    }

    @GET
    @Path("/filteredinstruments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = List.class)
    public List<Instrument> getFilteredInstruments(@QueryParam("isin") @ApiParam(value="the isin") String isin) {
        checkOperationAllowed(OpType.READ);
        List<Instrument> returnvalue =
            marketDataEnvironment.getInstrumentService().listInstruments().stream().filter(i->i.getIsin().contains(isin)).collect(Collectors.toList());
        return returnvalue;
    }

    @GET
    @Path("/addproduct")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Product",
        response = String.class)
    public String addProduct(@QueryParam("productId") @ApiParam(value="the isin") String productId,
        @QueryParam("description") @ApiParam(value="description") String description) {
        Product p = new Product(productId, description);
        try {
            marketDataEnvironment.getProductService().saveProduct(p);
        } catch (SQLException e) {
            e.printStackTrace();
            return "not Saved";
        }
        return "saved";
    }

    @GET
    @Path("/productobjects")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get ProductObjects",
        response = List.class)
    public List<Product> getProductObjects() {
        List<Product> returnvalue = null;
        try {
            marketDataEnvironment.getProductService().listProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Products", response = String.class)
    public String getProducts() {
        String returnvalue = "No Products";
        try {
            returnvalue = marketDataEnvironment.getProductService().listProducts().toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }
}
