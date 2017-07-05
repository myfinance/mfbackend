/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentInfo.java
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentInfo implements Serializable {
    private String name;
    private Map<String, EnvironmentTargetInfo<?>> targets = new HashMap<>();



    public EnvironmentInfo(String name, Map<String, EnvironmentTargetInfo<?>> targets) {
        this.name = name;
        this.targets = targets;
    }

    public EnvironmentInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        EnvironmentInfo that = (EnvironmentInfo) o;

        return new EqualsBuilder().append(name, that.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).toHashCode();
    }

    @Override
    public String toString() {
        return "EnvironmentInfo{" + "name='" + name + '\'' + ", targets=" + targets + '}';
    }

    public void addTarget(EnvironmentTargetInfo<?> targetInfo) {
        getTargets().put(targetInfo.getTargetName(), targetInfo);
    }

    public Map<String, EnvironmentTargetInfo<?>> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, EnvironmentTargetInfo<?>> targets) {
        this.targets = targets;
    }
}
