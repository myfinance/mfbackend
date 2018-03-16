/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseRootSecurityContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 31.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.rootcontext;

import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.api.security.SecurityServiceBuilder;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.OpType;
import de.hf.dac.myfinance.api.application.ServiceResourceType;
import de.hf.dac.myfinance.api.application.securitycontext.SecurityEnvProviderService;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRootSecurityContext extends BaseSecurityContext {

    protected RootSecurityProvider<OpType, OpLevel> rootSecurityProvider;

    /**
     *  this will execute only once, so runtime changes on the securityEnvironmentConfig will have no effect.
     *  But to change the SecurityEnvironment at runtime is not recommanded anyway
     *  if it should needed at at some point the sub-classes has to Designate(ocd = RootSecurityEnvironmentConfiguration.class)
     *  in stead of SecurityEnvProviderService. It can not be done in this class because DS is not processing annotaions in super-classes
     *  an so we can not write an activate methode here
     * @param id
     */
    public BaseRootSecurityContext(String id){
        super(id);
        SecurityServiceBuilder<OpType, OpLevel> securityServiceBuilder = getService(SecurityServiceBuilder.class);
        rootSecurityProvider = securityServiceBuilder.build(getService(SecurityEnvProviderService.class).getSecurityEnvironment());
    }


    @Override
    public ServiceResourceType getParent() {
        return this;
    }

    @Override
    public RootSecurityProvider getRootSecurityProvider() {
        return rootSecurityProvider;
    }

    @Override
    public List<String> getParentIdTrail() {
        List<String> ret = new ArrayList<>();
        ret.add(id);
        return ret;
    }

}
