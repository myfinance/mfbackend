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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

//import org.ops4j.pax.cdi.api.OsgiServiceProvider;
//import org.osgi.service.component.annotations.Component;

//import ProductService;
//import org.osgi.service.component.annotations.Reference;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.ProductServiceBuilder;
import org.ops4j.pax.cdi.api.OsgiService;

import java.sql.SQLException;

//@Component(service = MyHello.class)
//@OsgiServiceProvider(classes = {MyHello.class})
@Path("/hello")
@Singleton
public class MyHello {

    @OsgiService
    @Inject
    protected ProductServiceBuilder productServiceBuilder;

    @GET
    public String getHello() {
        return "MyHello!";
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    //public List<Product> getProducts() {
    public String getProducts() {
        String returnvalue = "No Products";
        try {
            ProductService productService = productServiceBuilder.build("dev");
            returnvalue = productService.listProducts().toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }

    @GET
    @Path("/addproduct")
    @Produces(MediaType.APPLICATION_JSON)
    public String addProduct(@QueryParam("productId") String productId, @QueryParam("description") String description) {
        Product p = new Product(productId, description);
        try {
            ProductService productService = productServiceBuilder.build("dev");
            productService.saveProduct(p);
        } catch (SQLException e) {
            e.printStackTrace();
            return "not Saved";
        }
        return "saved";
    }
}