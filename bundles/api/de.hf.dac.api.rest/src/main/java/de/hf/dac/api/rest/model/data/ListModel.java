/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ListModel.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 31.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.rest.model.data;

import de.hf.dac.api.rest.model.ModelBase;

import java.io.Serializable;
import java.util.List;

public class ListModel<T> extends ModelBase implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8915222530836443973L;
    protected final List<T> values;

    public ListModel(List<T> list) {
        this.values = list;
    }

    public List<T> getValues() {
        return values;
    }

}
