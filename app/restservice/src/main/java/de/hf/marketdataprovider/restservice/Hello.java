/** ----------------------------------------------------------------------------
 *
 * ---                                ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : Hello.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 24.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.restservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;

import java.util.List;
import de.hf.marketdataprovider.service.ProductService;
//import de.hf.marketdataprovider.domain.Product;
import org.osgi.service.component.annotations.Reference;

@Component(service = Hello.class)
@Path("/hello")
public class Hello {

    @Reference
    private ProductService productService;

    @GET
    public String getHello() {
        return "Hello!";
    }

    @GET
    @Path("/get/products")
    @Produces(MediaType.APPLICATION_JSON)
    //public List<Product> getProducts() {
    public String getProducts() {

        return productService.listProducts().toString();
        //return "test";
    }
}
