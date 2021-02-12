package de.hf.dac.myfinance.restservice.myfinanceresources.leafresources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hf.dac.myfinance.api.domain.InstrumentDetails;
import de.hf.dac.myfinance.api.restservice.InstrumentDetailModel;
import de.hf.dac.services.resources.leaf.map.MapResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(hidden = false, tags = "UtilityResources")
public class InstrumentDetailResource extends MapResource<Integer, InstrumentDetails> {

    public InstrumentDetailResource(InstrumentDetailModel data) {
        super(data);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Map Data", response = InstrumentDetailModel.class)
    public Response getAccountValueTupleMap(
        @Context
            UriInfo uriInfo) {
        return super.getData(uriInfo);

    }
}