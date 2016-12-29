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
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.hf.dac.api.security.SecurityService;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.restservice.Hello;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.SQLException;
import java.util.List;

@Component(service = Hello.class, immediate = true, property={"service.exported.interfaces=*",
    "service.exported.configs=org.apache.cxf.rs",
    "org.apache.cxf.rs.address=/rest/hello"})
//@Component(service = Hello.class, immediate = true)
@Named
public class MyHello  extends TopLevelWithEnvironments implements Hello{

    public String getHello() {
        return "MyHello!";
    }

    public String getProducts() {
        String returnvalue = "No Products";
        try {
            returnvalue = getMarketDataEnvironment("dev").getProductService().listProducts().toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }

    public List<Product> getProductObjects() {
        List<Product> returnvalue = null;
        try {
            returnvalue = getMarketDataEnvironment("dev").getProductService().listProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }

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

    public String cal(@PathParam("env") @ApiParam(value="The Service Environment") String env) {
        try {
            return getMarketDataEnvironment(env).getProductService().doSomeWork().toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    @Reference
    public void setMarketDataEnvironmentBuilder(MarketDataEnvironmentBuilder marketDataEnvironmentBuilder) {
        this.marketDataEnvironmentBuilder = marketDataEnvironmentBuilder;
    }

    @Override
    @Reference
    public void setSecurityService(SecurityService securityService) {
        this.securityService=securityService;
    }
}
