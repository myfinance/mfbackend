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

import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.api.security.SecurityServiceBuilder;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.InstrumentService;
import de.hf.dac.marketdataprovider.application.rootcontext.BaseRootSecurityContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarketDataEnvironmentImpl implements MarketDataEnvironment{

    final private ProductService productService;
    final private InstrumentService instrumentService;
    final private String environment;

    @Inject
    public MarketDataEnvironmentImpl(ProductService productService, InstrumentService instrumentService, @Named("envID") String environment){
        this.productService=productService;
        this.instrumentService=instrumentService;
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


}
