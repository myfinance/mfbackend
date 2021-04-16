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
@DiscriminatorValue(InstrumentType.BUDGETPORTFOLIO_IDSTRING)
public class BudgetPortfolio  extends Instrument {
    public BudgetPortfolio(){
        super();
    }

    public BudgetPortfolio(String description, boolean isactive, LocalDateTime treelastchanged){
        super(InstrumentType.BUDGETPORTFOLIO, description, isactive, treelastchanged);
    }
}