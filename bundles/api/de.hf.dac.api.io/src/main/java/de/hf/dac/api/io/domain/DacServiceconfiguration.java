/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2013, ... All Rights Reserved
 *
 *
 *  Project     : domain
 *
 *  File        : DacServiceconfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.12.2013
 *
 * ----------------------------------------------------------------------------
 */
 package de.hf.dac.api.io.domain;
// Generated by Hibernate Tools 5.2.5.Final


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * DacServiceconfiguration generated by hbm2java
*/
@Entity
@Table(
    name="dac_serviceconfiguration",
    uniqueConstraints = @UniqueConstraint(columnNames={"target", "environment"})
    )
@ApiModel
public class DacServiceconfiguration  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


     private int serviceconfigurationid;
     private String environment;
     private String target;
     private String envtype;
     private String identifier;
     private String jdbcurl;
     private String jndiurl;

    public DacServiceconfiguration() {
    }

	
    public DacServiceconfiguration(int serviceconfigurationid, String environment, String target, String envtype, String identifier) {
        this.serviceconfigurationid = serviceconfigurationid;
        this.environment = environment;
        this.target = target;
        this.envtype = envtype;
        this.identifier = identifier;
    }
    public DacServiceconfiguration(int serviceconfigurationid, String environment, String target, String envtype, String identifier, String jdbcurl, String jndiurl) {
       this.serviceconfigurationid = serviceconfigurationid;
       this.environment = environment;
       this.target = target;
       this.envtype = envtype;
       this.identifier = identifier;
       this.jdbcurl = jdbcurl;
       this.jndiurl = jndiurl;
    }

    @Id     
    @Column(name="serviceconfigurationid", unique=true, nullable=false)
    @ApiModelProperty(required = true)
    public int getServiceconfigurationid() {
        return this.serviceconfigurationid;
    }
    
    public void setServiceconfigurationid(int serviceconfigurationid) {
        this.serviceconfigurationid = serviceconfigurationid;
    }
    
    @Column(name="environment", nullable=false, length=20)
    @ApiModelProperty(required = true)
    public String getEnvironment() {
        return this.environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    @Column(name="target", nullable=false, length=20)
    @ApiModelProperty(required = true)
    public String getTarget() {
        return this.target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    @Column(name="envtype", nullable=false, length=25)
    @ApiModelProperty(required = true)
    public String getEnvtype() {
        return this.envtype;
    }
    
    public void setEnvtype(String envtype) {
        this.envtype = envtype;
    }
    
    @Column(name="identifier", nullable=false, length=40)
    @ApiModelProperty(required = true)
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    @Column(name="jdbcurl", length=100)
    @ApiModelProperty(required = true)
    public String getJdbcurl() {
        return this.jdbcurl;
    }
    
    public void setJdbcurl(String jdbcurl) {
        this.jdbcurl = jdbcurl;
    }
    
    @Column(name="jndiurl", length=50)
    @ApiModelProperty(required = true)
    public String getJndiurl() {
        return this.jndiurl;
    }
    
    public void setJndiurl(String jndiurl) {
        this.jndiurl = jndiurl;
    }




}
