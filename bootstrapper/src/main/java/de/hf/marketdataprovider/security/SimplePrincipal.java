/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.security;

import java.io.Serializable;
import java.security.Principal;


public class SimplePrincipal implements Principal, Serializable {
    public static final String WHAT = "$Id$";

    private static final long serialVersionUID = -277583078436504038L;

    private String name;

    /**
     * The default constructor.
     * @param theName the name
     */
    public SimplePrincipal(String theName) {
        this.name = theName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof SimplePrincipal)) {
            return false;
        }

        String n = ((Principal) object).getName();

        return ((name == null && n == null) || (name != null && name.equals(n)));
    }

    /**
     * Get the name of this principal.
     *
     * @return principal name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return (name == null ? 0 : name.hashCode());
    }
}

