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
import de.hf.dac.marketdataprovider.api.domain.InstrumentType;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.SecurityType;
import de.hf.dac.marketdataprovider.api.exceptions.MDException;
import de.hf.dac.marketdataprovider.api.exceptions.MDMsgKey;
import de.hf.dac.services.resources.BaseSecuredResource;
import de.hf.dac.web.Http;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentDataResource extends BaseSecuredResource<OpType,OpLevel> {

    MarketDataEnvironment marketDataEnvironment;
    final protected static Gson gson = new Gson();

    public EnvironmentDataResource(MDEnvironmentContext envContext) {
        super(envContext);
        this.marketDataEnvironment = envContext.getMarketDataEnvironment();
    }

    @GET
    @Path("/instrumentshateos")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = List.class)
    public Response getInstrumentshateos() {
        checkOperationAllowed(OpType.READ);
        List<Instrument> returnvalue = marketDataEnvironment.getInstrumentService().listInstruments();
        Link link = Link.fromUri("http://foo.bar/employee/john").rel("manager").rel("friend")
            .title("employee").type("application/xml").build();
        return Response.ok(returnvalue).links(link).build();
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
        //List<Instrument> returnvalue =
        //    marketDataEnvironment.getInstrumentService().listInstruments().stream().filter(i->i.getIsin().contains(isin)).collect(Collectors.toList());
        //return returnvalue;
        return new ArrayList<>();
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

    @GET
    @Path("/download")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Products", response = String.class)
    public String getData() {
        Http downloadHandler = new Http(10000);
        String returnvalue = "No Products";
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&apikey=Q6RLS6PGB55105EP";
        try {
            returnvalue=downloadHandler.getRequest(url, true, "proxy.dzbank.vrnet", 8080, "xn01598", "XN01598");
        } catch (IOException e) {
            throw new MDException(MDMsgKey.NO_RESPONSE_FROM_URL_EXCEPTION, "no response form "+url, e);
        }
        return returnvalue;
    }

    @GET
    @Path("/addEquity")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Instrument",
        response = String.class)
    public String addEquity(@QueryParam("isin") @ApiParam(value="the isin") String isin,
        @QueryParam("description") @ApiParam(value="description") String description) {
        Security i = new Security(description, true, LocalDate.now(), SecurityType.EQUITY, isin);
        marketDataEnvironment.getInstrumentService().saveSecurity(i);
        return "saved";
    }
}
