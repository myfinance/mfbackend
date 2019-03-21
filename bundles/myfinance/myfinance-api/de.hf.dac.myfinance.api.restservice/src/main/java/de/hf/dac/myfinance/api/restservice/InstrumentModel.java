/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : InstrumentModel.java
 * Author(s)   : hf
 * Created     : 01.06.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.restservice;

import java.io.Serializable;

import de.hf.dac.api.rest.model.ModelBase;
import de.hf.dac.myfinance.api.domain.Instrument;
import io.swagger.annotations.ApiModel;

@ApiModel
public class InstrumentModel  extends ModelBase implements Serializable {

    final protected Instrument value;

    public InstrumentModel(Instrument value) {
        this.value = value;
    }

    public Instrument getValue() {
        return value;
    }

}
