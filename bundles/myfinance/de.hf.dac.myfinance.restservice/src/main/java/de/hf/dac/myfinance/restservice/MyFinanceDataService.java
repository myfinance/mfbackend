/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MyFinanceDataService.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice;

import de.hf.dac.api.rest.model.data.StringListModel;
import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.Secured;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.OpType;
import de.hf.dac.myfinance.api.application.rootcontext.DataServiceRoot;
import de.hf.dac.myfinance.restservice.myfinanceresources.EnvironmentDataResource;
import de.hf.dac.services.resources.TopLevelSecuredResource;
import de.hf.dac.services.resources.leaf.list.StringListResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/myfinance")
@Api(value = "MyFinance",  tags = { "MyFinance"}) //must start with capital letter for client generation
public class MyFinanceDataService extends TopLevelSecuredResource<OpType,OpLevel> {
    private DataServiceRoot root;

    @Override
    protected <T extends IdentifiableResource<OpLevel> & Secured> T getSecurityContext() {
        root = getService(DataServiceRoot.class);
        return (T)root;
    }

    @Path("/environments/{envID}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Environment", response = EnvironmentDataResource.class)
    public EnvironmentDataResource getEnvironment(@PathParam("envID") @ApiParam(value="The Service Environment") String envID){
        audit();
        return new EnvironmentDataResource(root.getMDEnvironmentContext(envID));
    }

    @Path("/environments/list")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Environments", response = StringListResource.class, nickname ="getEnvironmentList")
    public StringListResource getEnvironmentList() {
        checkOperationAllowed(OpType.READ, "getEnvironmentList");
        audit();
        List<String> envs = root.getEnvironmentInfo();

        return new StringListResource(new StringListModel(envs));
    }

    @Path("/environments/test")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "testlist", response = StringListResource.class, nickname ="getTestList")
    public StringListResource getTestList() {
        checkOperationAllowed(OpType.READ, "getEnvironmentList");
        audit();
        List<String> envs = root.getEnvironmentInfo();

        return new StringListResource(new StringListModel(envs));
    }

}
