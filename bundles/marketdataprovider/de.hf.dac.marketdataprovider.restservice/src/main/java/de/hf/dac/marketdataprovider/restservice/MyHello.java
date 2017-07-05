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


import javax.ws.rs.GET;
import javax.ws.rs.Path;

import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import io.swagger.annotations.Api;

@Path("/hello")
@Api(value = "Hello")
public class MyHello {

    @GET
    public String getHello() {
        return "MyHello!";
    }

    @GET
    @Path("/list")
    public String getProducts() {

        //ProductDao productDao = new ProductDaoImpl()
        return "MyHello!";
    }

}
