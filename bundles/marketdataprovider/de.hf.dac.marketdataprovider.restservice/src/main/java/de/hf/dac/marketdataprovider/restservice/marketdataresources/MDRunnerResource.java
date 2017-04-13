/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDRunnerResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice.marketdataresources;


import com.google.gson.Gson;
import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobInformation;
import de.hf.dac.api.io.routes.job.WrappedJobParameter;
import de.hf.dac.api.security.SecuredResource;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import de.hf.dac.marketdataprovider.api.runner.BaseMDRunnerParameter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(tags = "Jobs")
public class MDRunnerResource extends SecuredResource<OpType,OpLevel> {
    private static final Logger LOG = LoggerFactory.getLogger("MDRunnerResource");
    final protected static Gson gson = new Gson();
    private final JobDispatcher dispatcher;


    public MDRunnerResource(JobDispatcher dispatcher, ServiceResourceType jobTyp) {
        super(jobTyp);
        this.dispatcher = dispatcher;
    }

    @POST
    @Path("/{env}/start")
    @ApiOperation(response = JobInformation.class, value = "execute marketdata launcher", notes = "Execute marketdata Core Launcher", nickname = "VERBATIMstart")
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Job Submitted", response = JobInformation.class) })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response start(@PathParam("env") @ApiParam(value="The env") String env,
            @ApiParam(name = "params", value = "Parameter") BaseMDRunnerParameter params) {
        //checkOperationAllowed(OpType.WRITE, jobtype);
        //todo wird hier env benötigt?, sollte nicht besser der Jobtype mitgegeben werden
        JobInformation jobInformation = dispatcher
            .sendJob(new WrappedJobParameter(params, env, null, WrappedJobParameter.RUNNER_REQUEST, WrappedJobParameter.RUNNER_RESULT));

        return Response.ok(gson.toJson(jobInformation)).build();
    }
}
