/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentResource.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 25.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice.marketdataresources;

import de.hf.dac.api.security.SecuredResource;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class EnvironmentResource extends SecuredResource<OpType,OpLevel> {

    MarketDataEnvironment marketDataEnvironment;
    final String OPERATIONID="environment";

    public EnvironmentResource(MarketDataEnvironment marketDataEnvironment) {
        super(marketDataEnvironment);
        this.marketDataEnvironment = marketDataEnvironment;
    }

    @GET
    @Path("/instruments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = List.class)
    public List<Instrument> getInstruments() {
        checkOperationAllowed(OpType.READ, OPERATIONID);
        List<Instrument> returnvalue = marketDataEnvironment.getInstrumentService().listInstruments();
        return returnvalue;
    }

    @Path("/{jobtype}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "launch Job", notes = "Anything Else?")
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server") })
    public MDRunnerResource getJobTypeRunner(@PathParam("jobtype") @ApiParam(name = "jobtype", value = "launching cops jobs") String jobtype) {
        checkOperationAllowed(OpType.WRITE, jobtype);
        // create RunnerResource
        return new MDRunnerResource();
    }
}
