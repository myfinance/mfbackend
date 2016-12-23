/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataEnvironmentBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application;

import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentContextBuilder;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.List;

@OsgiServiceProvider(classes = { MarketDataEnvironmentBuilder.class })
@Singleton
public class MarketDataEnvironmentBuilderImpl implements MarketDataEnvironmentBuilder {

    @Inject
    MarketDataEnvironmentContextBuilder contextBuilder;

    @Override
    public MarketDataEnvironment build(String environment) throws SQLException {
        // create autowire applicationContext
        ApplicationContext applicationContext = contextBuilder.build(environment);
        return applicationContext.autowire(MarketDataEnvironment.class);
    }

    @Override
    public List<String> getInfo() {
        return contextBuilder.getInfo();
    }
}
