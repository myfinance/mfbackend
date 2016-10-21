/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DaoContextBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 13.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import com.google.inject.Module;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentInfo;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.api.io.env.context.ContextBuilder;
import de.hf.dac.marketdataprovider.api.persistence.DaoContextBuilder;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.TransactionManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@OsgiServiceProvider(classes = { DaoContextBuilder.class })
@Singleton
public class DaoContextBuilderImpl implements DaoContextBuilder {
    @Inject
    @OsgiService
    ContextBuilder contextBuilder;

    @Inject
    @OsgiService
    EntityManagerFactorySetup emfb;

    @Inject
    @OsgiService
    EnvironmentService envService;

    @Inject
    @OsgiService
    TransactionManager jtaManager;

    @Override
    public ApplicationContext build(String env) throws SQLException {
        DaoBuilderModule daoBuilderModule //
            = new DaoBuilderModule(emfb, //
            envService, //
            jtaManager, //
            env);
        return contextBuilder.build("daobuilder/" + env, new Module[] { daoBuilderModule });
    }

    @Override
    public List<String> getInfo() {
        final Collection<EnvironmentInfo> environmentInfos = envService.availableEnvironments();
        return environmentInfos.stream().map(EnvironmentInfo::getName).collect(Collectors.toList());
    }
}
