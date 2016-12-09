/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : GrantedAuthorityImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.security;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl extends BasePrincipal implements GrantedAuthority {

    /**
     * The default constructor.
     * @param theName the name
     */
    public GrantedAuthorityImpl(String theName) {
        super(theName);
    }
    @Override
    public String getAuthority() {
        return getName();
    }
}
