/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseSecuredResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.services.resources;

import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.Secured;
import de.hf.dac.api.security.SecuredResource;

public abstract class BaseSecuredResource <ACCESS_TYPE extends Enum<ACCESS_TYPE>,RESOURCE_LEVEL extends Enum<RESOURCE_LEVEL>> extends
    SecuredResource<ACCESS_TYPE, RESOURCE_LEVEL> {

    public <T extends IdentifiableResource<RESOURCE_LEVEL> & Secured> BaseSecuredResource(T resource){
        super(resource);
    }

    @Override
    protected <T extends IdentifiableResource<RESOURCE_LEVEL> & Secured>  T getSecurityContext() {
        throw new SecurityException("No security context defined. Secured Resources (not Top-Level-Resources) have to set the security-Context in the constructor");
    }
}
