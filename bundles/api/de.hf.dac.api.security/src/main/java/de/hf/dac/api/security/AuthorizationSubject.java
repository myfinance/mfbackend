/** ----------------------------------------------------------------------------
 *
 * ---          Hf - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AuthorizationSubject.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

import java.util.List;

public interface AuthorizationSubject {

    List<String> getPermissions();

    List<String> getRoles();

    List<String> getInternalRoles();

    DacPrincipal getPrincipal();

}
