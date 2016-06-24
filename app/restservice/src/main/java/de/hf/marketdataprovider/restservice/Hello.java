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
import org.osgi.service.component.annotations.Component;

@Component(service = Hello.class)
@Path("/hello")
public class Hello {


    @GET
    public String getHello() {
        return "Hello!";
    }

}
