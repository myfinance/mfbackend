/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : CompanyPrincipal.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth;

import de.hf.dac.api.security.DacPrincipal;

import java.util.HashMap;
import java.util.Map;

/**
 * See also <code>BaseLoginModule</code>. If one of the LoginModules create a identitty,
 * then a <code>CompanyPrincipal</code> will be created.
 * It stores all information which can be retrieved from LDAP.
 * @author xn01598
 */
public class CompanyPrincipal extends SimplePrincipal implements DacPrincipal {

    private static final long serialVersionUID = -2093165888366191998L;
    private Map userProperties;

    private String dn;

    /**
     * Create a new principal.
     *
     * @param name
     *        name of the user to create the principal for
     */
    public CompanyPrincipal(String name)
    {
        super(name);
        userProperties = new HashMap<>();
    }

    public void addProperty(String propertyName, String propertyValue){
        userProperties.put(propertyName, propertyValue);
    }

    public void setDn(String theDN) {
        this.dn = theDN;
    }

    @Override
    public String getProperty(String propertyName) {
        return userProperties.get(propertyName).toString();
    }
}


