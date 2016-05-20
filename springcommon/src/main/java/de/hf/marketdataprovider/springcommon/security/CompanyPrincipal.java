/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.springcommon.security;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xn01598
 */
public class CompanyPrincipal extends SimplePrincipal {

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

}
