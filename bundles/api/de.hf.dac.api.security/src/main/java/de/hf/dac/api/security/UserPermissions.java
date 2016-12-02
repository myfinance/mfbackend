/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : UserPermissions.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

import java.util.List;

public class UserPermissions {
    final private String userName;

    public List<String> getPermissions() {
        return permissions;
    }

    public String getUserName() {
        return userName;
    }

    private final List<String> permissions;

    public UserPermissions(List<String> permissions, String userName) {
        this.userName = userName;
        this.permissions = permissions;
    }
}
