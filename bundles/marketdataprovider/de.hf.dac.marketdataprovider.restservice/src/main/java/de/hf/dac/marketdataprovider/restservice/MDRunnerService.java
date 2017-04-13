/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDRunnerService.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice;

import com.google.gson.Gson;
import de.hf.dac.api.io.routes.job.JobInformation;
import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.Secured;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.rootcontext.RunnerRoot;
import de.hf.dac.marketdataprovider.restservice.marketdataresources.MDRunnerResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/runner")
@Api(value = "MDRunner") //must start with capital letter for client generation
public class MDRunnerService extends TopLevelWithEnvironments{

    final protected static Gson gson = new Gson();

    @Path("/{jobtype}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "launch Job", notes = "Anything Else?")
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Something wrong in Server") })
    public MDRunnerResource getJobTypeRunner(@PathParam("jobtype") @ApiParam(name = "jobtype", value = "launching md jobs") String jobtype)
        throws SQLException {
        audit();
        // create RunnerResource
        return new MDRunnerResource(getService(RunnerRoot.class).getDispatcher(), getService(RunnerRoot.class).getAuthType(jobtype));
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
