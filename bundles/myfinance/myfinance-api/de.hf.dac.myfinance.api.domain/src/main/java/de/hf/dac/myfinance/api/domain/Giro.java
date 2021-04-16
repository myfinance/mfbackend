/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : Giro.java
 * Author(s)   : hf
 * Created     : 02.01.2019
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
@DiscriminatorValue(InstrumentType.GIRO_IDSTRING)
public class Giro  extends Instrument {
    protected Giro(){
        super();
    }

    public Giro(String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.GIRO, description, isactive, treelastchanged);
    }
}
