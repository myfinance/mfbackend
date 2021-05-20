/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : InstrumentListModel.java
 * Author(s)   : hf
 * Created     : 01.06.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.restservice;

import java.util.List;

import de.hf.dac.api.rest.model.data.ListModel;
import de.hf.dac.myfinance.api.domain.Instrument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class InstrumentListModel extends ListModel<Instrument>{

    /**
     *
     */
    private static final long serialVersionUID = 1100148638349335873L;

    public InstrumentListModel(List<Instrument> list) {
        super(list);
    }

    @Override
    @ApiModelProperty(required = true)
    public List<Instrument> getValues() {
        return values;
    }
}
