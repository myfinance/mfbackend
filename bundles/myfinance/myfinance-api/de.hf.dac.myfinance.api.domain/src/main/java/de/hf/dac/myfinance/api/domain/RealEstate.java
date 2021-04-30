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
@DiscriminatorValue(InstrumentType.REALESTATE_IDSTRING)
public class RealEstate  extends Instrument {
    protected RealEstate(){
        super();
    }

    public RealEstate(String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.REALESTATE, description, isactive, treelastchanged);
    }
}