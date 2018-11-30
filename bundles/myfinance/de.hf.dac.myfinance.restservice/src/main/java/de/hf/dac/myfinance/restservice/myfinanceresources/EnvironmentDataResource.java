/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentDataResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 25.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice.myfinanceresources;

import com.google.gson.Gson;
import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.OpType;
import de.hf.dac.myfinance.api.application.servicecontext.MDEnvironmentContext;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.exceptions.MDException;
import de.hf.dac.myfinance.api.exceptions.MDMsgKey;
import de.hf.dac.myfinance.api.restservice.InstrumentListModel;
import de.hf.dac.myfinance.restservice.myfinanceresources.leafresources.InstrumentListResource;
import de.hf.dac.services.resources.BaseSecuredResource;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentDataResource extends BaseSecuredResource<OpType,OpLevel> {

    private static final Logger LOG = LoggerFactory.getLogger(EnvironmentDataResource.class);

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

    @Path("/instruments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = InstrumentListResource.class)
    public InstrumentListResource getInstruments() {
        checkOperationAllowed(OpType.READ);
        LocalDateTime returnvalue = LocalDateTime.now();
        return new InstrumentListResource(new InstrumentListModel(marketDataEnvironment.getInstrumentService().listInstruments()));
    }

    /*@GET
    @Path("/instruments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = List.class)
    public Response getInstruments() {
        checkOperationAllowed(OpType.READ);
        try {
            LocalDateTime returnvalue = LocalDateTime.now();
            return Response.ok(LeafResource.SerializeToJSON(marketDataEnvironment.getInstrumentService().listInstruments())).build();
        } catch(Exception ex) {
            LOG.debug("Full Exception",ex);
            return Response.status(HttpStatus.SC_NO_CONTENT)
                           .entity(ex.getMessage())
                           .type(MediaType.APPLICATION_JSON)
                           .build();
        }
    }*/

    @POST
    @Path("/importprices")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "importprices", response = String.class)
    public String importPrices() {
        checkOperationAllowed(OpType.EXECUTE);
        marketDataEnvironment.getInstrumentService().importPrices(LocalDateTime.now());
        return "sucessful";
    }

    @GET
    @Path("/getsecurity")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Security", response = String.class)
    public Instrument getSecurity(@QueryParam("isin") @ApiParam(value="the isin") String isin) {
        checkOperationAllowed(OpType.READ);
        Optional<Instrument> security = marketDataEnvironment.getInstrumentService().getSecurity(isin);
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
        checkOperationAllowed(OpType.WRITE);

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
        checkOperationAllowed(OpType.WRITE);
        return marketDataEnvironment.getInstrumentService().saveSymbol(isin, symbol, currencyCode);
    }

    @POST
    @Path("/addCurrency")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Instrument",
        response = String.class)
    public String addCurrency(@QueryParam("currencyCode") @ApiParam(value="the currencyCode") String currencyCode,
        @QueryParam("description") @ApiParam(value="description") String description) {
        checkOperationAllowed(OpType.WRITE);
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
        checkOperationAllowed(OpType.WRITE);
        return marketDataEnvironment.getInstrumentService().saveEndOfDayPrice(currencyCode, isin, LocalDate.parse(dayofprice), value, LocalDateTime.now());
    }

    @POST
    @Path("/fillpricehistory")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "fillpricehistory", response = String.class)
    public String fillPricesHistory(@QueryParam("sourceId") @ApiParam(value="the sourceId") int sourceId,
        @QueryParam("isin") @ApiParam(value="the isin") String isin) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().fillPriceHistory(sourceId, isin, LocalDateTime.now());
        return "sucessful";
    }

    @GET
    @Path("/getvaluecurve")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Security", response = String.class)
    public Map<LocalDate, Double> getValueCurve(@QueryParam("instrumentId") @ApiParam(value="the instrumentId") int instrumentId,
            @QueryParam("startdate") @ApiParam(value="startdate in Format yyyy-mm-dd") String startdate,
            @QueryParam("enddate") @ApiParam(value="enddate in Format yyyy-mm-dd") String enddate) {
        checkOperationAllowed(OpType.READ);
        LocalDate start = LocalDate.parse(startdate);
        LocalDate end = LocalDate.parse(enddate);
        return marketDataEnvironment.getInstrumentService().getValueCurve(instrumentId, start, end);

    }
}
