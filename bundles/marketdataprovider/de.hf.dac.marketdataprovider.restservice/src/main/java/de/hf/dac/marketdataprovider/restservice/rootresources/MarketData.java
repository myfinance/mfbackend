/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketData.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice.rootresources;

import io.swagger.annotations.Api;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/marketdata")
@Api(value = "marketdata")
@Singleton
public class MarketData extends TopLevelWithEnvironments{

    @GET
    public String getHello() {
        return "MyHello!";
    }
}
