/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2013, ... All Rights Reserved
 *
 *
 *  Project     : domain
 *
 *  File        : Trade.java
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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Trade generated by hbm2java
*/
@Entity
@Table(
    name="mf_trade")
@ApiModel
public class Trade  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


     private Integer tradeid;
     private Instrument instrumentByDepotid;
     private Instrument instrumentByInstrumentid;
     private Transaction transaction;
     private double amount;

    public Trade() {
    }

    public Trade(Instrument instrumentByDepotid, Instrument instrumentByInstrumentid, Transaction transaction, double amount) {
       this.instrumentByDepotid = instrumentByDepotid;
       this.instrumentByInstrumentid = instrumentByInstrumentid;
       this.transaction = transaction;
       this.amount = amount;
    }

    @Id @GeneratedValue(strategy=IDENTITY)    
    @Column(name="tradeid", unique=true, nullable=false)
    @ApiModelProperty(required = true)
    public Integer getTradeid() {
        return this.tradeid;
    }
    
    public void setTradeid(Integer tradeid) {
        this.tradeid = tradeid;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="depotid", nullable=false)
    @ApiModelProperty(required = true)
    public Instrument getInstrumentByDepotid() {
        return this.instrumentByDepotid;
    }
    
    public void setInstrumentByDepotid(Instrument instrumentByDepotid) {
        this.instrumentByDepotid = instrumentByDepotid;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="instrumentid", nullable=false)
    @ApiModelProperty(required = true)
    public Instrument getInstrumentByInstrumentid() {
        return this.instrumentByInstrumentid;
    }
    
    public void setInstrumentByInstrumentid(Instrument instrumentByInstrumentid) {
        this.instrumentByInstrumentid = instrumentByInstrumentid;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="transactionid", nullable=false)
    @ApiModelProperty(required = true)
    public Transaction getTransaction() {
        return this.transaction;
    }
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    @Column(name="amount", nullable=false, precision=10)
    @ApiModelProperty(required = true)
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }




}