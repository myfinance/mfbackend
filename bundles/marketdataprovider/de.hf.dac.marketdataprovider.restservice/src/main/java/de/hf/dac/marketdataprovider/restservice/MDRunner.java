/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice;

import de.hf.dac.api.io.routes.job.JobInformation;
import de.hf.dac.marketdataprovider.api.application.RunnerRoot;
import de.hf.dac.marketdataprovider.restservice.marketdataresources.MDRunnerResource;
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
import javax.ws.rs.core.Response;
import java.util.List;

public class MDRunner extends TopLevelWithEnvironments{

    @Path("/{jobtype}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "launch Job", notes = "Anything Else?")
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server") })
    public MDRunnerResource getJobTypeRunner(@PathParam("jobtype") @ApiParam(name = "jobtype", value = "launching cops jobs") String jobtype) {
        audit();
        // create RunnerResource
        return new MDRunnerResource(getMarketDataEnvironment("dev"), jobtype);
    }

    @GET
    @Path("/list")
    @ApiOperation(response = List.class, value = "list known cops jobs", notes = "List all JobInformation", nickname = "VERBATIMlist", consumes = MediaType.APPLICATION_JSON, httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "List of known jobs", response = List.class) })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response list() {

        List jobInformation = getService(RunnerRoot.class).getDispatcher().list();

        return Response.ok(gson.toJson(jobInformation)).build();
    }

    @GET
    @Path("/status/{uuid}")
    @ApiOperation(response = JobInformation.class, value = "get status of cops jobs", notes = "JobInformation of actual Job", nickname = "VERBATIMstatus", consumes = MediaType.APPLICATION_JSON, httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Status of one Job", response = JobInformation.class) })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response status(@PathParam("uuid") @ApiParam(name = "uuid", value = "uuid of job") String uuid) {

        JobInformation jobInformation = getService(RunnerRoot.class).getDispatcher().getStatus(uuid);

        return Response.ok(gson.toJson(jobInformation)).build();
    }
}
