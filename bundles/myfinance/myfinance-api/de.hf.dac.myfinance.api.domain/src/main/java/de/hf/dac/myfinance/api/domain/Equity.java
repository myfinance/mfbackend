/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : Equity.java
 * Author(s)   : xn01598
 * Created     : 13.12.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(
    name="mf_instrument")
@ApiModel
@PrimaryKeyJoinColumn(name="instrumentid")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(InstrumentType.EQUITY_IDSTRING)
public class Equity extends Instrument {

    private Set<SecuritySymbols> symbols = new HashSet<SecuritySymbols>(0);

    public Equity(){
        super();
    }

    public Equity(String isin, String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.Equity, description, isactive, treelastchanged);
        setBusinesskey(isin);
    }

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name = "instrumentid")
    @ApiModelProperty(required = true)
    public Set<SecuritySymbols> getSymbols() {
        return this.symbols;
    }
    public void setSymbols(Set<SecuritySymbols> trades) {
        this.symbols = trades;
    }
}
