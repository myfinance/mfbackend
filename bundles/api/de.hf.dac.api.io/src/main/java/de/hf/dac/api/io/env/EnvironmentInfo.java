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

import java.io.Serializable;

public class EnvironmentInfo implements Serializable {
    private String name;
    private String target;
    private String type;

    public EnvironmentInfo(String name, String target) {
        this.name = name;
        this.target = target;
    }

    public EnvironmentInfo(String name, String target, String type) {
        this(name, target);
        this.type = type;
    }

    public EnvironmentInfo(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EnvironmentInfo{" + "name='" + name + '\'' + ", target='" + target + '\'' + ", type='" + type + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        EnvironmentInfo that = (EnvironmentInfo) o;

        return new EqualsBuilder().append(name, that.name).append(target, that.target).isEquals();
    }

    @Override
    public int hashCode() {
        return (name + "#" + target).hashCode();
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
