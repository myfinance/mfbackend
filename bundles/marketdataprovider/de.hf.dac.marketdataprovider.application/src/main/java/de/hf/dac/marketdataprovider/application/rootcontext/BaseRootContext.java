/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseRootContext.java
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

public abstract class BaseRootContext  implements ServiceResourceType {

    /**
     * Not all Requests have an Environment e.G. listRunner (there are jobs for multiple environments listed).
     * so I have to configure a environment to secure these operations as well
     */
    @ObjectClassDefinition(name = "Runner Security Source Configuration")
    public @interface RootSecurity {
        String sourceEnvironmentForSecurityDB() default "dev";
    }

    @Reference
    SecurityServiceBuilder<OpType, OpLevel> securityServiceBuilder;

    protected RootSecurityProvider<OpType, OpLevel> rootSecurityProvider;

    @Override
    public RootSecurityProvider getRootSecurityProvider() {
        return rootSecurityProvider;
    }

    public abstract ServiceResourceType getAuthType(String id);
}
