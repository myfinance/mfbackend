/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SecurityServiceBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.securitycontext;

import com.google.inject.Module;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.api.io.env.context.ContextBuilder;
import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.api.security.SecurityServiceBuilder;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.application.MDSecurityApplicationContextModule;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(service =  {SecurityServiceBuilder.class})
public class SecurityServiceBuilderImpl implements SecurityServiceBuilder {

    @Reference
    protected EntityManagerFactorySetup emfb;

    @Reference
    protected EnvironmentService envService;

    @Reference
    ContextBuilder contextBuilder;


    @Override
    public RootSecurityProvider<OpType, OpLevel> build(String env) {

        MDSecurityApplicationContextModule mdSecurityApplicationContextModule
            = new MDSecurityApplicationContextModule(env,envService,emfb);

        ApplicationContext applicationContext = contextBuilder.build("md-security/" + env,new Module[]{mdSecurityApplicationContextModule});

        return applicationContext.autowire(RootSecurityServiceImpl.class);

    }

    @Override
    public List<String> getInfo() {
        return new ArrayList<>();
    }

}
