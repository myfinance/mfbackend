/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentCredential.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Holds information about the environment(Test, Integration, Produktion).
 * See also <code>LdapLoginModule</code>.
 *
 * @author xn00147
 */
public class EnvironmentCredential implements Serializable {
    public static final String WHAT = "$Id$";

    private static final long serialVersionUID = 1L;

    private String environment;

    /**
     * The default constructor.
     *
     * @param theEnvironment
     *        the environment name
     */
    public EnvironmentCredential(String theEnvironment) {
        super();
        List<String> envs = Arrays.asList("Produktion", "Integration", "Integration1", "Integration2",
            "Test",  "Konfiguration",  "UAT",  "SIMULATION",
            "Test1", "Konfiguration1", "UAT1", "SIMULATION1",
            "Test2", "Konfiguration2", "UAT2", "SIMULATION2",
            "Test3", "Konfiguration3", "UAT3", "SIMULATION3",
            "Test4", "Konfiguration4", "UAT4", "SIMULATION4");
        ///
        if (theEnvironment != null && envs.contains(theEnvironment)) {
            this.environment = theEnvironment;
        } else {
            throw new IllegalArgumentException(
                "The enviroment is invalid, use one of: Test..., Integration..., UAT..., SIMULATION..., Konfiguration... or Produktion");
        }
    }


    public String getEnvironment() {
        return this.environment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.environment == null) ? 0 : this.environment.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EnvironmentCredential other = (EnvironmentCredential) obj;
        if (this.environment == null) {
            if (other.environment != null) {
                return false;
            }
        } else if (!this.environment.equals(other.environment)) {
            return false;
        }
        return true;
    }

}
