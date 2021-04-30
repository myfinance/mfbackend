/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : BudgetGroup.java
 * Author(s)   : hf
 * Created     : 21.12.2018
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
@DiscriminatorValue(InstrumentType.BUDGETGROUP_IDSTRING)
public class BudgetGroup extends Instrument {
    public BudgetGroup(){
        super();
    }

    public BudgetGroup(String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.BUDGETGROUP, description, isactive, treelastchanged);
    }
}