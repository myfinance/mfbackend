/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : StringModel.java
 * Author(s)   : xn01598
 * Created     : 15.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.rest.model.data;

import java.io.Serializable;

import de.hf.dac.api.rest.model.ModelBase;

public class StringModel extends ModelBase implements Serializable {

    final protected String value;

    public StringModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
