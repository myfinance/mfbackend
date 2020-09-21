package de.hf.dac.myfinance.restapi;

import javax.ws.rs.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

/**
 * MyFinance
 *
 * <p>Spezification for MyFinance
 *
 */
@Path("/")
//@Api(value = "MyFinance",  tags = { "MyFinance"}) //must start with capital letter for client generation
public interface DefaultApi  {

    @GET
    @Path("/v1/myfinance/environments/list")
    @ApiOperation(value = "", tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of environments") })
    public String v1MyfinanceEnvironmentsListGet();

    @GET
    @Path("/v1/myfinance/hello")
    @ApiOperation(value = "", tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "say hello to check that the service is up and running") })
    public String v1MyfinanceHelloGet();
}

