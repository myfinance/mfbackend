/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : DateDoubleModel.java
 * Author(s)   : xn01598
 * Created     : 14.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.rest.model.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class DateDoubleModel extends MapModel<LocalDate, Double> implements Serializable {
    public DateDoubleModel(Map<LocalDate, Double> map) {
        super(map);
    }

    @ApiModelProperty(required = true)
    @Override
    public Map<LocalDate, Double> getValues() {
        return values;
    }
}
