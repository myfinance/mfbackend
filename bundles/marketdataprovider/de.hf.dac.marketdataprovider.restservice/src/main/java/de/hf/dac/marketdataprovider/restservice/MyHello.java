/** ----------------------------------------------------------------------------
 *
 * ---                                ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : MyHello.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice;


import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.hf.dac.marketdataprovider.api.domain.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.sql.SQLException;
import java.util.List;

@Path("/hello")
@Api(value = "Hello")
public class MyHello  extends TopLevelWithEnvironments{

    @GET
    public String getHello() {
        return "MyHello!";
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Products", response = String.class)
    public String getProducts() {
        String returnvalue = "No Products";
        try {
            returnvalue = getMarketDataEnvironment("dev").getProductService().listProducts().toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }

    @GET
    @Path("/productobjects")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get ProductObjects",
        response = List.class)
    public List<Product> getProductObjects() {
        List<Product> returnvalue = null;
        try {
            returnvalue = getMarketDataEnvironment("dev").getProductService().listProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            getMarketDataEnvironment("dev").getProductService().saveProduct(p);
        } catch (SQLException e) {
            e.printStackTrace();
            return "not Saved";
        }
        return "saved";
    }

    /*@GET
    @Path("/dosomework")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "doSomeWork",
        response = String.class)
    public String cal(@PathParam("env") @ApiParam(value="The Service Environment") String env) {
        try {
            return getMarketDataEnvironment(env).getProductService().doSomeWork().toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }*/
}
