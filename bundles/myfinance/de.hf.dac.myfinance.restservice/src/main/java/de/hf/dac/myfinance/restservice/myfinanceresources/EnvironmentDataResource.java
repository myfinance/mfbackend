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

import de.hf.dac.api.rest.model.data.DateDoubleModel;
import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.OpType;
import de.hf.dac.myfinance.api.application.servicecontext.MDEnvironmentContext;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.RecurrentFrequency;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.restservice.InstrumentListModel;
import de.hf.dac.myfinance.api.restservice.InstrumentModel;
import de.hf.dac.myfinance.api.restservice.RecurrentTransactionListModel;
import de.hf.dac.myfinance.api.restservice.TransactionListModel;
import de.hf.dac.myfinance.restservice.myfinanceresources.leafresources.*;
import de.hf.dac.services.resources.BaseSecuredResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.http.HttpStatus;
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

    @Path("/instruments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = InstrumentListResource.class)
    public InstrumentListResource getInstruments() {
        checkOperationAllowed(OpType.READ);
        return new InstrumentListResource(new InstrumentListModel(marketDataEnvironment.getInstrumentService().listInstruments()));
    }

    @Path("/instrumentsfortenant")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments for tenant",
        response = InstrumentListResource.class)
    public InstrumentForTenantListResource getInstrumentsForTenant(@QueryParam("tenant") @ApiParam(value="tenant id") int tenantId) {
        checkOperationAllowed(OpType.READ);
        return new InstrumentForTenantListResource(new InstrumentListModel(marketDataEnvironment.getInstrumentService().listInstruments(tenantId)));
    }

    @Path("/instrumentspertype")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments per type",
        response = InstrumentListResource.class)
    public InstrumentPerTypeListResource getInstrumentsPerType(@QueryParam("tenant") @ApiParam(value="tenant id") int tenantId,
        @QueryParam("instrumenttype") @ApiParam(value="instrumenttype")
            InstrumentType instrumentType) {
        checkOperationAllowed(OpType.READ);
        return new InstrumentPerTypeListResource(new InstrumentListModel(marketDataEnvironment.getInstrumentService().listInstruments(tenantId, instrumentType)));
    }

    @Path("/listTransactions")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Transactions", response = TransactionListResource.class)
    public TransactionListResource getTransaction(@QueryParam("startdate") @ApiParam(value="startdate in Format yyyy-mm-dd") String startdate,
        @QueryParam("enddate") @ApiParam(value="enddate in Format yyyy-mm-dd") String enddate) {
        checkOperationAllowed(OpType.READ);
        LocalDate start = LocalDate.parse(startdate);
        LocalDate end = LocalDate.parse(enddate);
        return new TransactionListResource(new TransactionListModel(marketDataEnvironment.getInstrumentService().listTransactions(start, end)));

    }

    @Path("/listRecurrentTransactions")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get RecurrentTransactions", response = RecurrentTransactionListResource.class)
    public RecurrentTransactionListResource getRecurrentTransaction() {
        checkOperationAllowed(OpType.READ);
        return new RecurrentTransactionListResource(new RecurrentTransactionListModel(marketDataEnvironment.getInstrumentService().listRecurrentTransactions()));

    }

    @Path("/getvaluecurve/{instrumentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get value curve", response = ValueMapResource.class)
    public ValueMapResource getValueCurve(@PathParam("instrumentId") @ApiParam(value="the instrumentId") int instrumentId,
        @QueryParam("startdate") @ApiParam(value="startdate in Format yyyy-mm-dd") String startdate,
        @QueryParam("enddate") @ApiParam(value="enddate in Format yyyy-mm-dd") String enddate) {
        checkOperationAllowed(OpType.READ);
        LocalDate start = LocalDate.parse(startdate);
        LocalDate end = LocalDate.parse(enddate);
        return new ValueMapResource(new DateDoubleModel(marketDataEnvironment.getInstrumentService().getValueCurve(instrumentId, start, end)));

    }

    @POST
    @Path("/importprices")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "importprices")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "Prices imported"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response importPrices() {
        checkOperationAllowed(OpType.EXECUTE);
        marketDataEnvironment.getInstrumentService().importPrices(LocalDateTime.now());
        return Response.ok().build();
    }

    @Path("/getequity")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Equity", response = InstrumentResource.class)
    public InstrumentResource getEquity(@QueryParam("isin") @ApiParam(value="the isin") String isin) {
        checkOperationAllowed(OpType.READ);
        Optional<Equity> equity = marketDataEnvironment.getInstrumentService().getEquity(isin);
        if(equity.isPresent()) return new InstrumentResource(new InstrumentModel(equity.get()));
        else
            throw new MFException(MFMsgKey.NO_INSTRUMENT_FOUND_EXCEPTION, "no Instrument found with ISIN "+isin);

    }

    @Path("/listTenants")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "list Tenants", response = InstrumentResource.class)
    public TenantListResource getTenants() {
        checkOperationAllowed(OpType.READ);
        return new TenantListResource(new InstrumentListModel(marketDataEnvironment.getInstrumentService().listTenants()));
    }

    @POST
    @Path("/addEquity")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Instrument")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addEquity(@QueryParam("isin") @ApiParam(value="the isin") String isin,
        @QueryParam("description") @ApiParam(value="description") String description) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().saveEquity(isin, description);
        return Response.ok().build();
    }

    @POST
    @Path("/addSymbol")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Instrument")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addSymbol(@QueryParam("isin") @ApiParam(value="the isin") String isin,
        @QueryParam("symbol") @ApiParam(value="symbol") String symbol,
        @QueryParam("currencycode") @ApiParam(value="the code of the currency in which the security is traded in the exchange referenced by the symbol") String currencyCode) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().saveSymbol(isin, symbol, currencyCode);
        return Response.ok().build();
    }

    @POST
    @Path("/addCurrency")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Instrument")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addCurrency(@QueryParam("currencyCode") @ApiParam(value="the currencyCode") String currencyCode,
        @QueryParam("description") @ApiParam(value="description") String description) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().saveCurrency(currencyCode, description);
        return Response.ok().build();
    }

    @POST
    @Path("/addPrice")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Price")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addPrice(@QueryParam("currencyCode") @ApiParam(value="the currencyCode") String currencyCode,
        @QueryParam("isin") @ApiParam(value="the isin") String isin,
        @QueryParam("dayofprice") @ApiParam(value="the dayofprice(yyyy-mm-dd") String dayofprice,
        @QueryParam("value") @ApiParam(value="value") double value) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().saveEndOfDayPrice(currencyCode, isin, LocalDate.parse(dayofprice), value, LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/fillpricehistory")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "fillpricehistory")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response fillPricesHistory(@QueryParam("sourceId") @ApiParam(value="the sourceId") int sourceId,
        @QueryParam("isin") @ApiParam(value="the isin") String isin) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().fillPriceHistory(sourceId, isin, LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/addBudget")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "add Budget")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addBudget(@QueryParam("description") @ApiParam(value="description") String description,
                            @QueryParam("budgetGroupId") @ApiParam(value="the Id of the budgetGroup which the budget is attached to") int budgetGroupId) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().newBudget(description, budgetGroupId, LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/addTenant")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Tenant")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addTenant(@QueryParam("description") @ApiParam(value="description") String description) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().newTenant(description, LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/updateInstrument")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "update Instrument")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "updated"),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response updateInstrument(@QueryParam("id") @ApiParam(value="id") int id,
                                     @QueryParam("description") @ApiParam(value="description") String description,
                                     @QueryParam("isactive") @ApiParam(value="isactive") boolean isactive) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().updateInstrument(id, description, isactive);
        return Response.ok().build();
    }

    @POST
    @Path("/addGiro")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Giro")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addGiro(@QueryParam("description") @ApiParam(value="description") String description,
        @QueryParam("tenantId") @ApiParam(value="the Id of the tenant which the giro is attached to") int tenantId) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().newGiroAccount(description, tenantId, LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/addIncomeExpense")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Income or Expense")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addIncomeExpense(@QueryParam("description") @ApiParam(value="description") String description,
        @QueryParam("accId") @ApiParam(value="the accountId of the income or expense") int accId,
        @QueryParam("budgetId") @ApiParam(value="the budgetId of the income or expense") int budgetId,
        @QueryParam("value") @ApiParam(value="the value of the income or expense") double value,
        @QueryParam("transactiondate") @ApiParam(value="the transactiondate(yyyy-mm-dd") String transactiondate) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().newIncomeExpense(description, accId, budgetId, value, LocalDate.parse(transactiondate), LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/updateTransaction")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "update Transaction")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "updated"),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response updateTransaction(@QueryParam("id") @ApiParam(value="id") int id,
                                      @QueryParam("description") @ApiParam(value="description") String description,
                                      @QueryParam("value") @ApiParam(value="the value of the income or expense") double value,
                                      @QueryParam("transactiondate") @ApiParam(value="the transactiondate(yyyy-mm-dd") String transactiondate) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().updateTransaction(id, description, value, LocalDate.parse(transactiondate), LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/addTransfer")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save Transfer or BudgetTransfer")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addTransfer(@QueryParam("description") @ApiParam(value="description") String description,
        @QueryParam("srcId") @ApiParam(value="the instrumentId of the source") int srcId,
        @QueryParam("trgId") @ApiParam(value="the instrumentId of the target") int trgId,
        @QueryParam("value") @ApiParam(value="the value of the income or expense") double value,
        @QueryParam("transactiondate") @ApiParam(value="the transactiondate(yyyy-mm-dd") String transactiondate) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().newTransfer(description, srcId, trgId, value, LocalDate.parse(transactiondate), LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/addRecurrentTransfer")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "save RecurrentTransfer")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "added"),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response addRecurrentTransfer(@QueryParam("description") @ApiParam(value="description") String description,
                                @QueryParam("srcId") @ApiParam(value="the instrumentId of the source") int srcId,
                                @QueryParam("trgId") @ApiParam(value="the instrumentId of the target") int trgId,
                                @QueryParam("recurrentFrequency") @ApiParam(value="the frequency of the recurrent transaction") RecurrentFrequency recurrentFrequency,
                                @QueryParam("value") @ApiParam(value="the value of the income or expense") double value,
                                @QueryParam("transactiondate") @ApiParam(value="the transactiondate(yyyy-mm-dd") String transactiondate) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().newRecurrentTransaction(description, srcId, trgId, recurrentFrequency, value, LocalDate.parse(transactiondate), LocalDateTime.now());
        return Response.ok().build();
    }

    @POST
    @Path("/delTransfer")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "delete Transfer")
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "deleted"),
        @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server")})
    public Response delTransfer(@QueryParam("transactionId") @ApiParam(value="transactionId") int transactionId) {
        checkOperationAllowed(OpType.WRITE);
        marketDataEnvironment.getInstrumentService().deleteTransaction(transactionId);
        return Response.ok().build();
    }
}
