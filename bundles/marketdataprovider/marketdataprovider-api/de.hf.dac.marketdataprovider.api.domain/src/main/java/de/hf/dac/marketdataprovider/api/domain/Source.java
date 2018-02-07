/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2013, ... All Rights Reserved
 *
 *
 *  Project     : domain
 *
 *  File        : Source.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.12.2013
 *
 * ----------------------------------------------------------------------------
 */
 package de.hf.dac.marketdataprovider.api.domain;
// Generated by Hibernate Tools 5.2.5.Final


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Source generated by hbm2java
*/
@Entity
@Table(
    name="mf_source")
@ApiModel
public class Source  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


     private Integer sourceid;
     private ImportType importType;
     private String description;
     private int prio;
     private String urlprefix;
     private String urlpostfix;
     private Set<EndOfDayPrice> endOfDayPrices = new HashSet<EndOfDayPrice>(0);

    public Source() {
    }

	
    public Source(String description, int prio) {
        this.description = description;
        this.prio = prio;
    }
    public Source(ImportType importType, String description, int prio, String urlprefix, String urlpostfix, Set<EndOfDayPrice> endOfDayPrices) {
       this.importType = importType;
       this.description = description;
       this.prio = prio;
       this.urlprefix = urlprefix;
       this.urlpostfix = urlpostfix;
       this.endOfDayPrices = endOfDayPrices;
    }

    @Id @GeneratedValue(strategy=IDENTITY)    
    @Column(name="sourceid", unique=true, nullable=false)
    @ApiModelProperty(required = true)
    public Integer getSourceid() {
        return this.sourceid;
    }
    
    public void setSourceid(Integer sourceid) {
        this.sourceid = sourceid;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="importtypeid")
    @ApiModelProperty(required = true)
    public ImportType getImportType() {
        return this.importType;
    }
    
    public void setImportType(ImportType importType) {
        this.importType = importType;
    }
    
    @Column(name="description", nullable=false, length=100)
    @ApiModelProperty(required = true)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="prio", nullable=false)
    @ApiModelProperty(required = true)
    public int getPrio() {
        return this.prio;
    }
    
    public void setPrio(int prio) {
        this.prio = prio;
    }
    
    @Column(name="urlprefix")
    @ApiModelProperty(required = true)
    public String getUrlprefix() {
        return this.urlprefix;
    }
    
    public void setUrlprefix(String urlprefix) {
        this.urlprefix = urlprefix;
    }
    
    @Column(name="urlpostfix")
    @ApiModelProperty(required = true)
    public String getUrlpostfix() {
        return this.urlpostfix;
    }
    
    public void setUrlpostfix(String urlpostfix) {
        this.urlpostfix = urlpostfix;
    }
    @OneToMany(fetch=FetchType.LAZY, mappedBy="source")
    @ApiModelProperty(required = true)
    public Set<EndOfDayPrice> getEndOfDayPrices() {
        return this.endOfDayPrices;
    }
    
    public void setEndOfDayPrices(Set<EndOfDayPrice> endOfDayPrices) {
        this.endOfDayPrices = endOfDayPrices;
    }




}