/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : IntResource.java
 * Author(s)   : xn01598
 * Created     : 15.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.services.resources.leaf.simple;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hf.dac.api.rest.model.data.IntModel;
import de.hf.dac.services.resources.leaf.LeafResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(hidden = false,tags = "UtilityResources")
public class IntResource extends LeafResource {
    public IntResource(IntModel listData) {
        this.data = listData;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Data", response = IntModel.class)
    public Response getInt(
        @Context
            UriInfo uriInfo) {
        return super.getData(uriInfo);
    }
}