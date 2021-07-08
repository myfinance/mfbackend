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
public class Depot extends Instrument {

    private static final long serialVersionUID = 1L;

    protected Depot() {
        super();
    }

    public Depot(String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.DEPOT, description, isactive, treelastchanged);
    }
}