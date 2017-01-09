/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SecuredResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.restauthorization;

import de.hf.dac.api.security.AccessNotAllowedException;
import de.hf.dac.api.security.AuthorizationSubject;
import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.api.security.Secured;

public abstract class SecuredResource<ACCESS_TYPE extends Enum<ACCESS_TYPE>,RESOURCE_LEVEL extends Enum<RESOURCE_LEVEL>> {

    public AuthorizationSubject getUserPermissions() {
        return userPermissions;
    }

    final private AuthorizationSubject userPermissions;
    final private String resourceId1;

    final private RESOURCE_LEVEL opLevel1;

    final private RootSecurityProvider rootSecurityProvider1;

    final private String resourceId2;

    final private RESOURCE_LEVEL opLevel2;

    final private RootSecurityProvider rootSecurityProvider2;


    public <T extends IdentifiableResource<RESOURCE_LEVEL> & Secured> SecuredResource(AuthorizationSubject userPermissions, T resource) {
        this.userPermissions = userPermissions;
        this.resourceId1 = resource.getId();
        this.opLevel1 = resource.getOpLevel();
        this.rootSecurityProvider1 = resource.getRootSecurityProvider();
        this.resourceId2 = resource.getId();
        this.opLevel2 = resource.getOpLevel();
        this.rootSecurityProvider2 = resource.getRootSecurityProvider();
    }

    /*public <T1 extends IdentifiableResource<RESOURCE_LEVEL> & Secured, T2 extends IdentifiableResource<RESOURCE_LEVEL> & Secured>
    SecuredResource(AuthorizationSubject userPermissions, T1 resource1, T2 resource2) {
        this.userPermissions = userPermissions;
        this.resourceId1 = resource1.getId();
        this.opLevel1 = resource1.getOpLevel();
        this.rootSecurityProvider1 = resource1.getRootSecurityProvider();
        this.resourceId2 = resource2.getId();
        this.opLevel2 = resource2.getOpLevel();
        this.rootSecurityProvider2 = resource2.getRootSecurityProvider();
    }*/

    public void checkOperationAllowed(ACCESS_TYPE accessType) throws AccessNotAllowedException {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        String operationId = stack.getMethodName();
        checkOperationAllowed(accessType,operationId);
    }

    public void checkOperationAllowed(ACCESS_TYPE accessType, String operationId) throws AccessNotAllowedException {
        boolean allowed = rootSecurityProvider1.isOperationAllowed(accessType, opLevel1, resourceId1, userPermissions.getInternalRoles(), userPermissions.getPrincipal().getName(), operationId);
        if(allowed)
            return;
        throw new SecurityException("Not allowed");
    }

    public void checkBinaryOperationAllowed(ACCESS_TYPE accessType, String operationId1, String operationId2) throws AccessNotAllowedException {
        boolean allowed1 = rootSecurityProvider1.isOperationAllowed(accessType, opLevel1, resourceId1, userPermissions.getInternalRoles(), userPermissions.getPrincipal().getName(), operationId1);
        boolean allowed2 = rootSecurityProvider2.isOperationAllowed(accessType, opLevel2, resourceId2, userPermissions.getInternalRoles(), userPermissions.getPrincipal().getName(), operationId2);
        if(allowed1 && allowed2)
            return;
        throw new SecurityException("Not allowed");
    }

    public void checkPassthroughAllowed() {
        boolean allowed = rootSecurityProvider1.isPassthroughAllowed(opLevel1, resourceId1, userPermissions.getInternalRoles(), userPermissions.getPrincipal().getName());
        boolean allowed2 = rootSecurityProvider2.isPassthroughAllowed(opLevel2, resourceId2, userPermissions.getInternalRoles(), userPermissions.getPrincipal().getName());
        if(allowed && allowed2)
            return;
        throw new SecurityException("Not allowed");

    }
}

