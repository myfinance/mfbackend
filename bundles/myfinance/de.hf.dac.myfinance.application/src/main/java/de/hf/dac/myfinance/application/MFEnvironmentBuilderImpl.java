/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFEnvironmentBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application;

import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.myfinance.api.application.MarketDataEnvironmentContextBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import java.sql.SQLException;
import java.util.List;

@Component(service = {MarketDataEnvironmentBuilder.class})
public class MFEnvironmentBuilderImpl implements MarketDataEnvironmentBuilder {

    @Reference
    MarketDataEnvironmentContextBuilder contextBuilder;

    @Override
    public MarketDataEnvironment build(String environment) {
        // create autowire applicationContext
        ApplicationContext applicationContext = contextBuilder.build(environment);
        return applicationContext.autowire(MarketDataEnvironment.class);
    }

    @Override
    public List<String> getInfo() {
        return contextBuilder.getInfo();
    }
}
