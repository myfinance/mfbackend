/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : GrantedAuthorityImpl.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 11.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.springcommon.security;

import org.springframework.security.core.GrantedAuthority;
import de.hf.marketdataprovider.common.security.BasePrincipal;

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
