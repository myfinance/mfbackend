/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DacEnvironmentConfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 22.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.env.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="dacenvironmentconfiguration" )
public class DacEnvironmentConfiguration implements java.io.Serializable {

    @Id
    @Column(name = "environment", nullable = false,length = 20)
    private String environment;

    @Id
    @Column(name = "target", nullable = false,length = 20)
    private String target;

    @Column(name = "envtype", nullable = false,length = 25)
    private String envtype;

    @Column(name = "identifier", nullable = false,length = 40)
    private String identifier;

    @Column(name = "jdbcurl", nullable = false,length = 100)
    private String jdbcUrl;


    @Column(name = "jndiurl", nullable = false,length = 50)
    private String jndiUrl;

    public DacEnvironmentConfiguration() {
    }

    public String getEnvironment() {
        return environment;
    }

    public String getTarget() {
        return target;
    }

    public String getEnvtype() {
        return envtype;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getJndiUrl() {
        return jndiUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DacEnvironmentConfiguration)) return false;
        DacEnvironmentConfiguration that = (DacEnvironmentConfiguration) o;
        return Objects.equals(getEnvironment(), that.getEnvironment()) &&
            getTarget() == that.getTarget() &&
            getEnvtype() == that.getEnvtype() &&
            Objects.equals(getIdentifier(), that.getIdentifier()) &&
            Objects.equals(getJndiUrl(), that.getJndiUrl()) &&
            Objects.equals(getJdbcUrl(), that.getJdbcUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEnvironment(), getTarget(), getEnvtype(), getIdentifier(), getJdbcUrl(),getJndiUrl());
    }
}
