/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : StringResource.java
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

import de.hf.dac.api.rest.model.data.StringModel;
import de.hf.dac.services.resources.leaf.LeafResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(hidden = false,tags = "UtilityResources")
public class StringResource extends LeafResource {
    public StringResource(StringModel data) {
        this.data = data;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Data", response = StringModel.class)
    public Response getString(
        @Context
            UriInfo uriInfo) {
        return super.getData(uriInfo);
    }
}
