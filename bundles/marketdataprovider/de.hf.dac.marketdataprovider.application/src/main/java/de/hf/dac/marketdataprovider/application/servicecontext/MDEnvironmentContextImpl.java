/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDEnvironmentContextImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 18.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.servicecontext;

import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import de.hf.dac.marketdataprovider.api.application.servicecontext.MDEnvironmentContext;
import de.hf.dac.marketdataprovider.application.rootcontext.BaseSecurityContext;

import java.sql.SQLException;

public class MDEnvironmentContextImpl extends BaseSecurityContext implements MDEnvironmentContext {

    MarketDataEnvironment environment;

    public MDEnvironmentContextImpl(MarketDataEnvironmentBuilder marketDataEnvironmentBuilder, String env, ServiceResourceType parent){
        super(env,parent);
        try {
            environment = marketDataEnvironmentBuilder.build(env);
        } catch (SQLException e) {
            //todo how to handle this?
            e.printStackTrace();
        }
    }

    public MarketDataEnvironment getMarketDataEnvironment()  {
        return environment;
    }

    @Override
    public ServiceResourceType getChildServiceContext(String id) {
        return null;
    }

    @Override
    public OpLevel getOpLevel() {
        return OpLevel.environment;
    }

}