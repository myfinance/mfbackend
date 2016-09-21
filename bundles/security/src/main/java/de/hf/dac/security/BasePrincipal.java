/** ----------------------------------------------------------------------------
 *
 * ---                                                    ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : BasePrincipal.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security;

import java.io.Serializable;
import java.security.Principal;

public class BasePrincipal implements Principal, Serializable {

    /**
     * The default constructor.
     * @param theName the name
     */
    public BasePrincipal(String theName) {
        this.name = theName;
    }

    private String name;

    /**
     * Get the name of this principal.
     *
     * @return principal name
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof BasePrincipal)) {
            return false;
        }

        String n = ((Principal) object).getName();

        return ((name == null && n == null) || (name != null && name.equals(n)));
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
