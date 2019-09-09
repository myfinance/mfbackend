/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : InstrumentForTenantListResource.java
 * Author(s)   : xn01598
 * Created     : 09.09.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice.myfinanceresources.leafresources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hf.dac.api.rest.model.data.ListModel;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.restservice.InstrumentListModel;
import de.hf.dac.services.resources.leaf.list.ListResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(hidden = false,tags = "UtilityResources")
public class InstrumentForTenantListResource extends ListResource<Instrument> {

    public InstrumentForTenantListResource(ListModel<Instrument> listData) {
        super(listData);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Data", response = InstrumentListModel.class)
    public Response getInstrumentForTenantList(
        @Context
            UriInfo uriInfo) {
        return super.getData(uriInfo);
    }
}
