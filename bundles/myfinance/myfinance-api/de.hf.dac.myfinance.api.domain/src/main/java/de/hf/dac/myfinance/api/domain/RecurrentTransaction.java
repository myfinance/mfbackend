/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2013, ... All Rights Reserved
 *
 *
 *  Project     : domain
 *
 *  File        : RecurrentTransaction.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.12.2013
 *
 * ----------------------------------------------------------------------------
 */
 package de.hf.dac.myfinance.api.domain;
// Generated by Hibernate Tools 5.2.5.Final


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * RecurrentTransaction generated by hbm2java
*/
@Entity
@Table(
    name="mf_recurrenttransaction")
@ApiModel
public class RecurrentTransaction  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


     private Integer recurrenttransactionid;
     private Instrument instrumentByInstrumentid2;
     private Instrument instrumentBySecurityid;
     private Instrument instrumentByInstrumentid1;
     private int recurrencytype;
     private String description;
     private double value;
     private LocalDate nexttransaction;
     private int recurrentfrequenceId;

    public RecurrentTransaction() {
    }

	
    public RecurrentTransaction(Instrument instrumentByInstrumentid2, Instrument instrumentByInstrumentid1, int recurrencytype, String description, double value, LocalDate nexttransaction, RecurrentFrequency recurrentfrequence) {
        this.instrumentByInstrumentid2 = instrumentByInstrumentid2;
        this.instrumentByInstrumentid1 = instrumentByInstrumentid1;
        this.recurrencytype = recurrencytype;
        this.description = description;
        this.value = value;
        this.nexttransaction = nexttransaction;
        setRecurrentfrequence(recurrentfrequence);
    }
    public RecurrentTransaction(Instrument instrumentByInstrumentid2, Instrument instrumentBySecurityid, Instrument instrumentByInstrumentid1, int recurrencytype, String description, double value, LocalDate nexttransaction, RecurrentFrequency recurrentfrequence) {
       this.instrumentByInstrumentid2 = instrumentByInstrumentid2;
       this.instrumentBySecurityid = instrumentBySecurityid;
       this.instrumentByInstrumentid1 = instrumentByInstrumentid1;
       this.recurrencytype = recurrencytype;
       this.description = description;
       this.value = value;
       this.nexttransaction = nexttransaction;
        setRecurrentfrequence(recurrentfrequence);
    }

    @Id @GeneratedValue(strategy=IDENTITY)    
    @Column(name="recurrenttransactionid", unique=true, nullable=false)
    @ApiModelProperty(required = true)
    public Integer getRecurrenttransactionid() {
        return this.recurrenttransactionid;
    }
    public void setRecurrenttransactionid(Integer recurrenttransactionid) {
        this.recurrenttransactionid = recurrenttransactionid;
    }

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name="instrumentid1", nullable=false)
    @ApiModelProperty(required = true)
    public Instrument getInstrumentByInstrumentid1() {
        return this.instrumentByInstrumentid1;
    }
    public void setInstrumentByInstrumentid1(Instrument instrumentByInstrumentid1) {
        this.instrumentByInstrumentid1 = instrumentByInstrumentid1;
    }

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name="instrumentid2", nullable=false)
    @ApiModelProperty(required = true)
    public Instrument getInstrumentByInstrumentid2() {
        return this.instrumentByInstrumentid2;
    }
    public void setInstrumentByInstrumentid2(Instrument instrumentByInstrumentid2) {
        this.instrumentByInstrumentid2 = instrumentByInstrumentid2;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="securityid")
    @ApiModelProperty(required = true)
    public Instrument getInstrumentBySecurityid() {
        return this.instrumentBySecurityid;
    }
    public void setInstrumentBySecurityid(Instrument instrumentBySecurityid) {
        this.instrumentBySecurityid = instrumentBySecurityid;
    }

    @Column(name="recurrencytype", nullable=false)
    @ApiModelProperty(required = true)
    public int getRecurrencytype() {
        return this.recurrencytype;
    }
    public void setRecurrencytype(int recurrencytype) {
        this.recurrencytype = recurrencytype;
    }
    
    @Column(name="description", nullable=false, length=128)
    @ApiModelProperty(required = true)
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="value", nullable=false, precision=10)
    @ApiModelProperty(required = true)
    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    
    @Column(name="nexttransaction", nullable=false, length=13)
    @ApiModelProperty(required = true)
    public LocalDate getNexttransaction() {
        return this.nexttransaction;
    }
    public void setNexttransaction(LocalDate nexttransaction) {
        this.nexttransaction = nexttransaction;
    }

    @Column(name="recurrentfrequence", nullable=false)
    @ApiModelProperty(required = true)
    protected int getRecurrentfrequenceId() {
        return this.recurrentfrequenceId;
    }
    protected void setRecurrentfrequenceId(int recurrentfrequenceId) {
        this.recurrentfrequenceId = recurrentfrequenceId;
    }
    @Transient
    @ApiModelProperty(required = true)
    public RecurrentFrequency getRecurrentfrequence(){
        return RecurrentFrequency.getRecurrentFrequencyById(recurrentfrequenceId);
    }
    protected void setRecurrentfrequence(RecurrentFrequency recurrentfrequence) {
        this.recurrentfrequenceId = recurrentfrequence.getValue();
    }
}
