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

package de.hf.dac.api.security;

import de.hf.dac.common.BaseDS;

import javax.security.auth.Subject;
import java.security.AccessController;

public abstract class SecuredResource<ACCESS_TYPE extends Enum<ACCESS_TYPE>,RESOURCE_LEVEL extends Enum<RESOURCE_LEVEL>> extends BaseDS {

    private String resourceId1;

    private RESOURCE_LEVEL opLevel1;

    private RootSecurityProvider rootSecurityProvider1;

    private String resourceId2;

    private RESOURCE_LEVEL opLevel2;

    private RootSecurityProvider rootSecurityProvider2;

    private boolean isInit = false;

    public SecuredResource() {
        init(getSecurityContext());
    }

    public <T extends IdentifiableResource<RESOURCE_LEVEL> & Secured> SecuredResource(T resource) {
        init(resource);
    }

    protected abstract <T extends IdentifiableResource<RESOURCE_LEVEL> & Secured>  T getSecurityContext();

    protected <T extends IdentifiableResource<RESOURCE_LEVEL> & Secured> void init(T securityContext){
        if(!isInit){
            this.resourceId1 = securityContext.getId();
            this.opLevel1 = securityContext.getOpLevel();
            this.rootSecurityProvider1 = securityContext.getRootSecurityProvider();
            this.resourceId2 = securityContext.getId();
            this.opLevel2 = securityContext.getOpLevel();
            this.rootSecurityProvider2 = securityContext.getRootSecurityProvider();
            isInit=true;
        }
    }

    /**
     * for multiEnvironment operations e.G. if you want to compare two sets of Information from different environments
     * @param securityContext1
     * @param securityContext2
     * @param <T1>
     * @param <T2>
     */
    public <T1 extends IdentifiableResource<RESOURCE_LEVEL> & Secured, T2 extends IdentifiableResource<RESOURCE_LEVEL> & Secured>
    SecuredResource(T1 securityContext1, T2 securityContext2) {
        this.resourceId1 = securityContext1.getId();
        this.opLevel1 = securityContext1.getOpLevel();
        this.rootSecurityProvider1 = securityContext1.getRootSecurityProvider();
        this.resourceId2 = securityContext2.getId();
        this.opLevel2 = securityContext2.getOpLevel();
        this.rootSecurityProvider2 = securityContext2.getRootSecurityProvider();
    }

    public void checkOperationAllowed(ACCESS_TYPE accessType) throws AccessNotAllowedException {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        String operationId = stack.getMethodName();
        checkOperationAllowed(accessType,operationId);
    }

    public void checkOperationAllowed(ACCESS_TYPE accessType, String operationId) throws AccessNotAllowedException {
        boolean allowed = rootSecurityProvider1.isOperationAllowed(accessType, opLevel1, resourceId1, Subject.getSubject(AccessController.getContext()), operationId);
        if(allowed)
            return;
        throw new SecurityException("Not allowed");
    }

    public void checkBinaryOperationAllowed(ACCESS_TYPE accessType, String operationId1, String operationId2) throws AccessNotAllowedException {
        boolean allowed1 = rootSecurityProvider1.isOperationAllowed(accessType, opLevel1, resourceId1, Subject.getSubject(AccessController.getContext()), operationId1);
        boolean allowed2 = rootSecurityProvider2.isOperationAllowed(accessType, opLevel2, resourceId2, Subject.getSubject(AccessController.getContext()), operationId2);
        if(allowed1 && allowed2)
            return;
        throw new SecurityException("Not allowed");
    }

    public void checkPassthroughAllowed() {
        boolean allowed = rootSecurityProvider1.isPassthroughAllowed(opLevel1, resourceId1, Subject.getSubject(AccessController.getContext()));
        boolean allowed2 = rootSecurityProvider2.isPassthroughAllowed(opLevel2, resourceId2, Subject.getSubject(AccessController.getContext()));
        if(allowed && allowed2)
            return;
        throw new SecurityException("Not allowed");

    }
}

