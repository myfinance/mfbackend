/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : Currency.java
 * Author(s)   : xn01598
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
@DiscriminatorValue("13")
public class Currency extends Instrument {
    public Currency(String currencyCode, String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.Currency, description, isactive, treelastchanged);
        setBusinesskey(currencyCode);
    }
}
