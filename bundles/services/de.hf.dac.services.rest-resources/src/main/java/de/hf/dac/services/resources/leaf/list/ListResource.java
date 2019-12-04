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
    public ListResource(ListModel<T> listData) {
        this.data = listData;
    }
}
