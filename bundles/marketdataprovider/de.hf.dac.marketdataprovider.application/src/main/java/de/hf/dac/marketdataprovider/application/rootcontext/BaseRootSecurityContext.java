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

package de.hf.dac.marketdataprovider.application.rootcontext;

import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.api.security.SecurityServiceBuilder;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRootSecurityContext extends BaseSecurityContext {

    /**
     * Not all Requests have an Environment e.G. listRunner (there are jobs for multiple environments listed).
     * so I have to configure a environment to secure these operations as well
     */
    @ObjectClassDefinition(name = "Runner Security Source Configuration")
    public @interface RootSecurity {
        String sourceEnvironmentForSecurityDB() default "dev";
    }

    private void activate(RootSecurity cacheRootSecurity) {
        try {
            rootSecurityProvider = securityServiceBuilder.build(cacheRootSecurity.sourceEnvironmentForSecurityDB());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected RootSecurityProvider<OpType, OpLevel> rootSecurityProvider;
    protected SecurityServiceBuilder<OpType, OpLevel> securityServiceBuilder;

    public BaseRootSecurityContext(String id){
        super(id);
        securityServiceBuilder = getService(SecurityServiceBuilder.class);
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
