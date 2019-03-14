/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : ValueMapResource.java
 * Author(s)   : xn01598
 * Created     : 14.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice.myfinanceresources.leafresources;

import java.time.LocalDate;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hf.dac.api.rest.model.data.DateDoubleModel;
import de.hf.dac.services.resources.leaf.map.MapResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(hidden = false,tags = "UtilityResources")
public class ValueMapResource extends MapResource<LocalDate, Double> {

    public ValueMapResource(DateDoubleModel data) {
        super(data);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Map Data", response = DateDoubleModel.class)
    public Response getValueMap(
        @Context
            UriInfo uriInfo) {
        return super.getData(uriInfo);

    }
}
