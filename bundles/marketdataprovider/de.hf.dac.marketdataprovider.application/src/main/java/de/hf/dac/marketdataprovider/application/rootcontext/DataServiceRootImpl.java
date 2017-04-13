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

import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import de.hf.dac.marketdataprovider.api.application.rootcontext.DataServiceRoot;
import org.osgi.service.component.annotations.Reference;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataServiceRootImpl extends BaseRootContext implements DataServiceRoot{

    @Reference
    private MarketDataEnvironmentBuilder marketDataEnvironmentBuilder;

    //todo kann das in die base class?
    private void activate(RootSecurity cacheRootSecurity) {
        try {
            rootSecurityProvider = securityServiceBuilder.build(cacheRootSecurity.sourceEnvironmentForSecurityDB());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    final private String id = "DataServiceRoot";


    @Override
    public ServiceResourceType getParent() {
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    //todo kann das in die base class?
    @Override
    public List<String> getParentIdTrail() {
        List<String> ret = new ArrayList<>();
        ret.add(id);
        return ret;
    }

    @Override
    public OpLevel getOpLevel() {
        return OpLevel.environment;
    }

    @Override
    public MarketDataEnvironment getMarketDataEnvironment(String env) throws SQLException {
        return marketDataEnvironmentBuilder.build(env);
    }

    @Override
    public List<String> getEnvironmentInfo() {
        return marketDataEnvironmentBuilder.getInfo();
    }

    @Override
    public ServiceResourceType getAuthType(String jobType) {
        return new MDRunnerJobTypeImpl(jobType, this);
    }
}
