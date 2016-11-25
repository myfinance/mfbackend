/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataEnvironmentContextBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application;

import com.google.inject.Module;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentInfo;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.api.io.env.context.ContextBuilder;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentContextBuilder;
import de.hf.dac.marketdataprovider.api.application.MarketDataSystemDescriptor;
import org.ops4j.pax.cdi.api.OsgiService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.TransactionManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class MarketDataEnvironmentContextBuilderImpl implements MarketDataEnvironmentContextBuilder {
    @Inject
    @OsgiService
    ContextBuilder contextBuilder;

    @Inject
    @OsgiService
    EnvironmentService envService;

    @Inject
    @OsgiService
    EntityManagerFactorySetup emfb;

    @Inject
    @OsgiService
    TransactionManager jtaManager;


    @Override
    public ApplicationContext build(String environment) throws SQLException {
        MarketDataEnvironmentBuilderModule marketDataEnvironmentBuilderModule //
            = new MarketDataEnvironmentBuilderModule(envService, //
            emfb, //
            jtaManager, //
            environment);
        return contextBuilder.build(MarketDataSystemDescriptor.CONTEXT+"/" + environment, new Module[] { marketDataEnvironmentBuilderModule });
    }

    @Override
    public List<String> getInfo() {
        final Collection<EnvironmentInfo> environmentInfos = envService.availableEnvironments();
        return environmentInfos.stream().map(EnvironmentInfo::getName).collect(Collectors.toList());
    }
}
