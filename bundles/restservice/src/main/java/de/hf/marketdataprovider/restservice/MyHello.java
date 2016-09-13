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
 *  Author(s)   : xn01598
 *
 *  Created     : 24.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.restservice;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//import org.ops4j.pax.cdi.api.OsgiServiceProvider;
//import org.osgi.service.component.annotations.Component;

//import ProductService;
//import org.osgi.service.component.annotations.Reference;
import de.hf.marketdataprovider.api.service.ProductService;
import org.ops4j.pax.cdi.api.OsgiService;

//@Component(service = MyHello.class)
//@OsgiServiceProvider(classes = {MyHello.class})
@Path("/hello")
@Singleton
public class MyHello {

    //@Reference
    @OsgiService
    @Inject
    private ProductService productService;

    @GET
    public String getHello() {
        return "MyHello!";
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
