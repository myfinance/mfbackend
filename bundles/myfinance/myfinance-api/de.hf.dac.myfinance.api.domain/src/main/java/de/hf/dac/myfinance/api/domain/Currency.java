/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : Currency.java
 * Author(s)   : hf
 * Created     : 30.11.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;

@Entity
@Table(
    name="mf_instrument")
@ApiModel
@PrimaryKeyJoinColumn(name="instrumentid")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(InstrumentType.CURRENCY_IDSTRING)
public class Currency extends Instrument {

    public Currency(){
        super();
    }

    public Currency(String currencyCode, String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.CURRENCY, description, isactive, treelastchanged);
        setBusinesskey(currencyCode);
    }
}
