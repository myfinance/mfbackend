/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : EnvironmentListResource.java
 * Author(s)   : xn01598
 * Created     : 06.07.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice.myfinanceresources.leafresources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hf.dac.api.rest.model.data.StringListModel;
import de.hf.dac.services.resources.leaf.list.ListResource;
import io.swagger.annotations.ApiOperation;

public class EnvironmentListResource extends ListResource<String> {

    public EnvironmentListResource(StringListModel listData) {
        super(listData);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Environments" , response = StringListModel.class )
    public Response getEnvironmentList(@Context
        UriInfo uriInfo) {
        return super.getList(uriInfo);

    }

}
