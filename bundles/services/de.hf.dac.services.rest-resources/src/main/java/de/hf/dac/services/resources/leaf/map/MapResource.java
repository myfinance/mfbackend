/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : MapResource.java
 * Author(s)   : xn01598
 * Created     : 14.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.services.resources.leaf.map;

import de.hf.dac.api.rest.model.data.MapModel;
import de.hf.dac.services.resources.leaf.LeafResource;

public abstract class MapResource<T, U> extends LeafResource {
    protected MapResource(MapModel<T, U> data) {
        this.data = data;
    }
}

