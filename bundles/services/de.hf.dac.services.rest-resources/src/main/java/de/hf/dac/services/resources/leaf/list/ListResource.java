/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ListResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 31.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.services.resources.leaf.list;

import de.hf.dac.api.rest.model.data.ListModel;
import de.hf.dac.services.resources.leaf.LeafResource;
import org.apache.http.HttpStatus;


import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public abstract class ListResource<T> extends LeafResource {

    final private ListModel<T> listData;

    public ListResource(ListModel<T> listData) {
        this.listData = listData;
    }

    public Response getList(@Context UriInfo uriInfo) {
        try {
            URI uri = uriInfo == null ? null
                : uriInfo.getRequestUri();

            String urs = uri == null ? ""
                : uri.toString();

            listData.setUrl(urs);

            return uriInfo == null ? Response.ok(SerializeToJSON(listData)).build()
                : Response.ok(SerializeToJSON(listData)).location(uriInfo.getRequestUri()).build();
        } catch(Exception ex) {
            return Response.status(HttpStatus.SC_NO_CONTENT)
                .entity(ex.getMessage())
                .type(MediaType.APPLICATION_JSON)
                .build();
        }

    }


}
