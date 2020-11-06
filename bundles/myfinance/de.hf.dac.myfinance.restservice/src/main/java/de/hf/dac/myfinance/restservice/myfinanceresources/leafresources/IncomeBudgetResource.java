package de.hf.dac.myfinance.restservice.myfinanceresources.leafresources;

import de.hf.dac.myfinance.api.restservice.InstrumentModel;
import de.hf.dac.services.resources.leaf.LeafResource;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class IncomeBudgetResource  extends LeafResource {
    public IncomeBudgetResource(InstrumentModel data) {
        this.data = data;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Data", response = InstrumentModel.class)
    public Response getIncomeBudget(
            @Context
                    UriInfo uriInfo) {
        return super.getData(uriInfo);
    }
}
