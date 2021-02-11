package de.hf.dac.myfinance.restservice.myfinanceresources.leafresources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentValuesTuple;
import de.hf.dac.myfinance.api.restservice.AccountValueTupleModel;
import de.hf.dac.services.resources.leaf.map.MapResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(hidden = false, tags = "UtilityResources")
public class AccountValueTupleResource extends MapResource<Instrument, InstrumentValuesTuple> {

    public AccountValueTupleResource(AccountValueTupleModel data) {
        super(data);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Map Data", response = AccountValueTupleModel.class)
    public Response getValueMap(
        @Context
            UriInfo uriInfo) {
        return super.getData(uriInfo);

    }
}