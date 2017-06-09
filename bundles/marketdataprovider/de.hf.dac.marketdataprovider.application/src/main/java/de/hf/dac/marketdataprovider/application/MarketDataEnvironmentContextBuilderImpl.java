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
import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentContextBuilder;
import de.hf.dac.marketdataprovider.api.application.MarketDataSystemDescriptor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.transaction.TransactionManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MarketDataEnvironmentContextBuilderImpl implements MarketDataEnvironmentContextBuilder {
    @Reference
    ContextBuilder contextBuilder;

    @Reference
    EnvironmentService envService;

    @Reference
    EntityManagerFactorySetup emfb;

    @Reference
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
