/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2013, ... All Rights Reserved
 *
 *
 *  Project     : domain
 *
 *  File        : Instrument.java
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Instrument generated by hbm2java
*/
@Entity
@Table(
    name="mf_instrument")
@ApiModel
public class Instrument  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private Integer instrumentid;
    private Integer instrumenttypeId;
    private String description;
    private boolean isactive;
    private LocalDate maturitydate;
    private LocalDate closingdate;
    private LocalDateTime treelastchanged;
    private InstrumentType instrumentType;
    private String businesskey;

    public Instrument() {
    }

    public Instrument(InstrumentType instrumentType, String description, boolean isactive, LocalDateTime treelastchanged) {
        setInstrumentTypeId(instrumentType.getValue());
        this.description = description;
        this.isactive = isactive;
        this.treelastchanged = treelastchanged;
    }

    @Id @GeneratedValue(strategy=IDENTITY)    
    @Column(name="instrumentid", unique=true, nullable=false)
    @ApiModelProperty(required = true)
    public Integer getInstrumentid() {
        return this.instrumentid;
    }
    public void setInstrumentid(Integer instrumentid) {
        this.instrumentid = instrumentid;
    }

    @Column(name = "instrumenttypeid", nullable=false)
    @ApiModelProperty(required = true)
    protected Integer getInstrumentTypeId() {
        return this.instrumenttypeId;
    }
    protected void setInstrumentTypeId(Integer instrumentTypeId) {
        this.instrumenttypeId = instrumentTypeId;
        instrumentType = InstrumentType.getInstrumentTypeById(instrumentTypeId);
    }

    @Transient
    public InstrumentType getInstrumentType(){
        return instrumentType;
    }

    @Column(name="description", nullable=false, length=100)
    @ApiModelProperty(required = true)
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="isactive", nullable=false)
    @ApiModelProperty(required = true)
    public boolean isIsactive() {
        return this.isactive;
    }
    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
    
    @Column(name="maturitydate", length=13)
    public LocalDate getMaturitydate() {
        return this.maturitydate;
    }
    public void setMaturitydate(LocalDate maturitydate) {
        this.maturitydate = maturitydate;
    }
    
    @Column(name="closingdate", length=13)
    public LocalDate getClosingdate() {
        return this.closingdate;
    }
    public void setClosingdate(LocalDate closingdate) {
        this.closingdate = closingdate;
    }
    
    @Column(name="treelastchanged", nullable=false, length=13)
    @ApiModelProperty(required = true)
    public LocalDateTime getTreelastchanged() {
        return this.treelastchanged;
    }
    public void setTreelastchanged(LocalDateTime treelastchanged) {
        this.treelastchanged = treelastchanged;
    }

    @Column(name="businesskey", length=32)
    @ApiModelProperty(required = true)
    public String getBusinesskey() {
        return this.businesskey;
    }

    public void setBusinesskey(String businesskey) {
        this.businesskey = businesskey;
    }

}
