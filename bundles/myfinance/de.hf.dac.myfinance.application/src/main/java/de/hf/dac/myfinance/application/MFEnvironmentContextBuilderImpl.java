/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFEnvironmentContextBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application;

import com.google.inject.Module;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentInfo;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.api.io.env.context.ContextBuilder;
import de.hf.dac.api.io.web.WebRequestService;
import de.hf.dac.myfinance.api.application.MarketDataEnvironmentContextBuilder;
import de.hf.dac.myfinance.api.application.MarketDataSystemDescriptor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.transaction.TransactionManager;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MFEnvironmentContextBuilderImpl implements MarketDataEnvironmentContextBuilder {
    @Reference
    ContextBuilder contextBuilder;

    @Reference
    EnvironmentService envService;

    @Reference
    EntityManagerFactorySetup emfb;

    @Reference
    TransactionManager jtaManager;

    @Reference
    WebRequestService webRequestService;



    @Override
    public ApplicationContext build(String environment){
        MFEnvironmentBuilderModule marketDataEnvironmentBuilderModule //
            = new MFEnvironmentBuilderModule(envService, //
            emfb, //
            jtaManager, //
            environment, webRequestService);
        return contextBuilder.build(MarketDataSystemDescriptor.CONTEXT+"/" + environment, new Module[] { marketDataEnvironmentBuilderModule });
    }

    @Override
    public List<String> getInfo() {
        final Collection<EnvironmentInfo> environmentInfos = envService.availableEnvironments();
        return environmentInfos.stream().map(EnvironmentInfo::getName).collect(Collectors.toList());
    }
}
