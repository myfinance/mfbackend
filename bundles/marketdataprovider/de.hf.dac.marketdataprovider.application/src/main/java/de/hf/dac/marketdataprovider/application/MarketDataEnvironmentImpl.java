/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataEnvironmentImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application;

import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.InstrumentService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

public class MarketDataEnvironmentImpl implements MarketDataEnvironment{

    final private ProductService productService;
    final private InstrumentService instrumentService;
    final private RootSecurityProvider rootSecurityProvider;
    final private String environment;

    @Inject
    public MarketDataEnvironmentImpl(RootSecurityProvider rootSecurityProvider, ProductService productService, InstrumentService instrumentService, @Named("envID") String environment){
        this.productService=productService;
        this.instrumentService=instrumentService;
        this.rootSecurityProvider=rootSecurityProvider;
        this.environment=environment;
    }

    @Override
    public ProductService getProductService() {
        return productService;
    }

    @Override
    public InstrumentService getInstrumentService() {
        return instrumentService;
    }

    @Override
    public RootSecurityProvider getRootSecurityProvider() {
        return rootSecurityProvider;
    }

    @Override
    public String getId() {
        return environment;
    }

    @Override
    public List<String> getParentIdTrail() {
        List<String> ret = new ArrayList<>();
        ret.add(environment);
        return ret;
    }

    @Override
    public OpLevel getOpLevel() {
        return OpLevel.environment;
    }
}
