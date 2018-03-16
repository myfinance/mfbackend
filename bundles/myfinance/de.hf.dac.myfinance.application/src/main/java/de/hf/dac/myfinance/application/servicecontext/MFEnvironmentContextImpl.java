/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFEnvironmentContextImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 18.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.servicecontext;

import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.ServiceResourceType;
import de.hf.dac.myfinance.api.application.servicecontext.MDEnvironmentContext;
import de.hf.dac.myfinance.application.rootcontext.BaseSecurityContext;

import java.sql.SQLException;

public class MFEnvironmentContextImpl extends BaseSecurityContext implements MDEnvironmentContext {

    MarketDataEnvironment environment;

    public MFEnvironmentContextImpl(MarketDataEnvironmentBuilder marketDataEnvironmentBuilder, String env, ServiceResourceType parent){
        super(env,parent);
        environment = marketDataEnvironmentBuilder.build(env);
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