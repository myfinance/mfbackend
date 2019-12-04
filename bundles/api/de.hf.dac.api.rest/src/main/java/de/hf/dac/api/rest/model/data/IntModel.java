/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : IntModel.java
 * Author(s)   : xn01598
 * Created     : 15.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.rest.model.data;

import java.io.Serializable;

import de.hf.dac.api.rest.model.ModelBase;

public class IntModel  extends ModelBase implements Serializable {

    final protected int value;

    public IntModel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
