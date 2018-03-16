/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MyFinanceRunnerResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice.myfinanceresources;


import com.google.gson.Gson;
import de.hf.dac.api.io.routes.job.JobInformation;
import de.hf.dac.api.io.routes.job.WrappedJobParameter;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.OpType;
import de.hf.dac.myfinance.api.application.servicecontext.MDRunnerJobTypeContext;
import de.hf.dac.myfinance.api.runner.BaseMFRunnerParameter;
import de.hf.dac.services.resources.BaseSecuredResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(tags = "Jobs")
public class MyFinanceRunnerResource extends BaseSecuredResource<OpType,OpLevel> {
    final protected static Gson gson = new Gson();
    private final MDRunnerJobTypeContext ctx;


    public MyFinanceRunnerResource(MDRunnerJobTypeContext runnerContext) {
        super(runnerContext);
        ctx = runnerContext;
    }

    @POST
    @Path("/{env}/start")
    @ApiOperation(response = JobInformation.class, value = "execute myfinance launcher", notes = "Execute myfinance Core Launcher", nickname = "VERBATIMstart")
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Job Submitted", response = JobInformation.class) })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response start(@PathParam("env") @ApiParam(value="The env") String env,
            @ApiParam(name = "params", value = "Parameter") BaseMFRunnerParameter params) {
        checkOperationAllowed(OpType.EXECUTE);
        JobInformation jobInformation = ctx.getDispatcher()
            .sendJob(new WrappedJobParameter(params, env, ctx.getId(),null, WrappedJobParameter.RUNNER_REQUEST, WrappedJobParameter.RUNNER_RESULT));

        return Response.ok(gson.toJson(jobInformation)).build();
    }
}
