/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DataServiceRootImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 31.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.rootcontext;

import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import de.hf.dac.marketdataprovider.api.application.rootcontext.DataServiceRoot;
import de.hf.dac.marketdataprovider.api.application.servicecontext.MDEnvironmentContext;
import de.hf.dac.marketdataprovider.application.servicecontext.MDEnvironmentContextImpl;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import java.util.List;

@Component(service = DataServiceRoot.class, scope = ServiceScope.SINGLETON)
public class DataServiceRootImpl extends BaseRootSecurityContext implements DataServiceRoot{

    @Reference
    private MarketDataEnvironmentBuilder marketDataEnvironmentBuilder;

    public DataServiceRootImpl() {
        super("DataServiceRoot");
    }

    @Override
    public OpLevel getOpLevel() {
        return OpLevel.environment;
    }

    @Override
    public List<String> getEnvironmentInfo() {
        return marketDataEnvironmentBuilder.getInfo();
    }

    @Override
    public ServiceResourceType getChildServiceContext(String id) {
        return getMDEnvironmentContext(id);
    }

    @Override
    public MDEnvironmentContext getMDEnvironmentContext(String env){
        return new MDEnvironmentContextImpl(marketDataEnvironmentBuilder, env, this);
    }
}
