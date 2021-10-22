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

public abstract class ListResource<T> extends LeafResource {
    public ListResource(ListModel<T> listData) {
        this.data = listData;
    }
}
