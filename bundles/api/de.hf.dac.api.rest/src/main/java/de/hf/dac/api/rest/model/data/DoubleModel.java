/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : DoubleModel.java
 * Author(s)   : xn01598
 * Created     : 15.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.rest.model.data;

import java.io.Serializable;

import de.hf.dac.api.rest.model.ModelBase;

public class DoubleModel extends ModelBase implements Serializable {

    final protected double value;

    public DoubleModel(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
