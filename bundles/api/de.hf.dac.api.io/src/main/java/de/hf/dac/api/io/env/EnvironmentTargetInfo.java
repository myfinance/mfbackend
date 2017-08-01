/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentTargetInfo.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 22.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.env;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class EnvironmentTargetInfo<T> {
    private String envName;

    private String targetName;
    private String targetType;

    private T targetDetails;

    public EnvironmentTargetInfo(String envName, String targetName, String targetType) {
        this.envName = envName;
        this.targetName = targetName;
        this.targetType = targetType;
    }

    public EnvironmentTargetInfo(String envName, String targetName, String targetType, T targetDetails) {
        this.envName = envName;
        this.targetName = targetName;
        this.targetType = targetType;
        this.targetDetails = targetDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        EnvironmentTargetInfo<?> that = (EnvironmentTargetInfo<?>) o;

        return new EqualsBuilder().append(envName, that.envName).append(targetName, that.targetName).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(envName).append(targetName).toHashCode();
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public T getTargetDetails() {
        return targetDetails;
    }

    public void setTargetDetails(T targetDetails) {
        this.targetDetails = targetDetails;
    }

    @Override
    public String toString() {
        return "EnvironmentTargetInfo{" + "envName='" + envName + '\'' + ", targetName='" + targetName + '\'' + ", targetType='" + targetType + '\''
            + ", targetDetails=" + targetDetails + '}';
    }
}

