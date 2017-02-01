/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RootSecurityProvider.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

import javax.security.auth.Subject;

public interface RootSecurityProvider<ACCESS_TYPE extends Enum<ACCESS_TYPE>, RESOURCE_LEVEL extends Enum<RESOURCE_LEVEL>> {
    void updateAuth();

    boolean isOperationAllowed(ACCESS_TYPE opType, RESOURCE_LEVEL opLevel, String resourceId, Subject subject,
        String operationId);

    boolean isPassthroughAllowed(RESOURCE_LEVEL opLevel, String resourceId, Subject subject);
}
