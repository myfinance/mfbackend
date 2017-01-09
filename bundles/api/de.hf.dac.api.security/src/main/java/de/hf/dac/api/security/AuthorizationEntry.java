/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AuthorizationEntry.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

import javax.persistence.Transient;
import java.util.List;

public interface AuthorizationEntry {
    String getRestIdPattern();

    String getDescription();

    @Transient
    List<String> listOperations();

    @Transient
    List<String> listPermissions();

    @Transient
    List<String> listUsers();
}
