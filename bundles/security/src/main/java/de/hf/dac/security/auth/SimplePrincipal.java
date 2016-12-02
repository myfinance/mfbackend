/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SimplePrincipal.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth;

import java.io.Serializable;
import java.security.Principal;

/**
 * An implementation of <code>Principal</code> that consists of a name only.
 *
 * @author Jens Granseuer
 */
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
