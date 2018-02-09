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
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.exceptions.MDException;
import de.hf.dac.marketdataprovider.api.exceptions.MDMsgKey;
import de.hf.dac.services.resources.BaseSecuredResource;
import de.hf.dac.web.Http;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @POST
    @Path("/importprices")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "importprices", response = String.class)
    public String importPrices() {
        marketDataEnvironment.getInstrumentService().importPrices();
        return "sucessful";
    }

    @GET
    @Path("/getsecurity")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Security", response = String.class)
    public Security getSecurity(@QueryParam("isin") @ApiParam(value="the isin") String isin) {
        Optional<Security> security = marketDataEnvironment.getInstrumentService().getSecurity(isin);
        if(security.isPresent()) return security.get();
        else
            throw new MDException(MDMsgKey.NO_INSTRUMENT_FOUND_EXCEPTION, "no Instrument found with ISIN "+isin);

    }

    @POST
    @Path("/addEquity")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Instrument",
        response = String.class)
    public String addEquity(@QueryParam("isin") @ApiParam(value="the isin") String isin,
        @QueryParam("description") @ApiParam(value="description") String description) {

        return marketDataEnvironment.getInstrumentService().saveSecurity(isin, description);

    }

    @POST
    @Path("/addSymbol")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Instrument",
        response = String.class)
    public String addSymbol(@QueryParam("isin") @ApiParam(value="the isin") String isin,
        @QueryParam("symbol") @ApiParam(value="symbol") String symbol,
        @QueryParam("currencycode") @ApiParam(value="the code of the currency in which the security is traded in the exchange referenced by the symbol") String currencyCode) {

        return marketDataEnvironment.getInstrumentService().saveSymbol(isin, symbol, currencyCode);
    }

    @POST
    @Path("/addCurrency")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Instrument",
        response = String.class)
    public String addCurrency(@QueryParam("currencyCode") @ApiParam(value="the currencyCode") String currencyCode,
        @QueryParam("description") @ApiParam(value="description") String description) {

        return marketDataEnvironment.getInstrumentService().saveCurrency(currencyCode, description);
    }

    @POST
    @Path("/addPrice")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Price",
        response = String.class)
    public String addPrice(@QueryParam("currencyCode") @ApiParam(value="the currencyCode") String currencyCode,
        @QueryParam("isin") @ApiParam(value="the isin") String isin,
        @QueryParam("dayofprice") @ApiParam(value="the dayofprice(yyyy-mm-dd") String dayofprice,
        @QueryParam("value") @ApiParam(value="value") double value) {

        return marketDataEnvironment.getInstrumentService().saveEndOfDayPrice(currencyCode, isin, LocalDate.parse(dayofprice), value, LocalDate.now());
    }
}
