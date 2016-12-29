/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : Hello.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 27.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.restservice;

import de.hf.dac.marketdataprovider.api.domain.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/hello")
@Api(value = "hello")
public interface Hello {
    @GET
    String getHello();

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Products", response = String.class)
    String getProducts();

    @GET
    @Path("/productobjects")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get ProductObjects",
        response = List.class)
    List<Product> getProductObjects();

    @GET
    @Path("/addproduct")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Product",
        response = String.class)
    String addProduct(@QueryParam("productId") @ApiParam(value="the isin") String productId,
        @QueryParam("description") @ApiParam(value="description") String description);

    @GET
    @Path("/dosomework")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "doSomeWork",
        response = String.class)
    String cal(@PathParam("env") @ApiParam(value="The Service Environment") String env);
}
