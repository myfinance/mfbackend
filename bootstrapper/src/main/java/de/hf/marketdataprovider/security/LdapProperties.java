/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : LdapProperties.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 18.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.security;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LdapProperties {

    private String bindDN;
    private String bindCredential;
    private String baseFilter;
    private String roleFilter;
    private String roleAttributeID;
    private String permissionAttributeID;
    private String roleCtxDN;
    //für parseUser muss noch eine lösung gefunden werden
    private String baseCtxDN;
    private String url;
    // Is user's role attribute a DN or the role name?
    private boolean roleAttributeIsDN;
    private List<String> userAttr;

    public List<String> getUserAttr() {
        return userAttr;
    }

    public void setUserAttr(String userAttrlist) {
        StringTokenizer tokenizer = new StringTokenizer(userAttrlist, ",");
        userAttr = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            userAttr.add(tokenizer.nextToken());
        }
    }

    public boolean isRoleAttributeIsDN() {
        return roleAttributeIsDN;
    }

    public void setRoleAttributeIsDN(boolean roleAttributeIsDN) {
        this.roleAttributeIsDN = roleAttributeIsDN;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBindDN() {
        return bindDN;
    }

    public void setBindDN(String bindDN) {
        this.bindDN = bindDN;
    }

    public String getBindCredential() {
        return bindCredential;
    }

    public void setBindCredential(String bindCredential) {
        this.bindCredential = bindCredential;
    }

    public String getBaseFilter() {
        return baseFilter;
    }

    public void setBaseFilter(String baseFilter) {
        this.baseFilter = baseFilter;
    }

    public String getRoleFilter() {
        return roleFilter;
    }

    public void setRoleFilter(String roleFilter) {
        this.roleFilter = roleFilter;
    }

    public String getRoleAttributeID() {
        return roleAttributeID;
    }

    public void setRoleAttributeID(String roleAttributeID) {
        this.roleAttributeID = roleAttributeID;
    }

    public String getPermissionAttributeID() {
        return permissionAttributeID;
    }

    public void setPermissionAttributeID(String permissionAttributeID) {
        this.permissionAttributeID = permissionAttributeID;
    }

    public String getRoleCtxDN() {
        return roleCtxDN;
    }

    public void setRoleCtxDN(String roleCtxDN) {
        this.roleCtxDN = roleCtxDN;
    }

    public String getBaseCtxDN() {
        return baseCtxDN;
    }

    public void setBaseCtxDN(String baseCtxDN) {
        this.baseCtxDN = baseCtxDN;
    }
}
