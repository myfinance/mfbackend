/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : Tenant.java
 * Author(s)   : hf
 * Created     : 20.12.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;

@Entity
@Table(
    name="mf_instrument")
@ApiModel
@PrimaryKeyJoinColumn(name="instrumentid")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(InstrumentType.TENANT_IDSTRING)
public class Tenant extends Instrument {

    List<InstrumentGraph> instrumentGraph;

    public Tenant(){
        super();
    }

    public Tenant(String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.Tenant, description, isactive, treelastchanged);
    }
}
