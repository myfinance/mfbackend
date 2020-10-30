package de.hf.dac.myfinance.restservice.myfinanceresources.leafresources;

import de.hf.dac.api.rest.model.data.ListModel;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.restservice.RecurrentTransactionListModel;
import de.hf.dac.services.resources.leaf.list.ListResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Api(hidden = false,tags = "UtilityResources")
public class RecurrentTransactionListResource extends ListResource<RecurrentTransaction> {

    public RecurrentTransactionListResource(ListModel<RecurrentTransaction> listData) {
        super(listData);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Data", response = RecurrentTransactionListModel.class)
    public Response getRecurrentTransactionList(
            @Context
                    UriInfo uriInfo) {
        return super.getData(uriInfo);
    }
}
