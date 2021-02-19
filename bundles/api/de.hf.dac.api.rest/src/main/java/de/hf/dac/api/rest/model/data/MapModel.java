/**
 * ----------------------------------------------------------------------------
 * ---          - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : MapModel.java
 * Author(s)   : hf
 * Created     : 14.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.rest.model.data;

import java.io.Serializable;
import java.util.Map;

import de.hf.dac.api.rest.model.ModelBase;

public class MapModel<T, U> extends ModelBase implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6103196936718227555L;
    protected final Map<T, U> values;

    public MapModel(final Map<T, U> map) {
        this.values = map;
    }

    public Map<T, U> getValues() {
        return values;
    }

}