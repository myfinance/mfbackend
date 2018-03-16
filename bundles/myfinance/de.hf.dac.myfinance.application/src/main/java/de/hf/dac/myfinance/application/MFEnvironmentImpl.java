/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFEnvironmentImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application;

import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.OpType;
import de.hf.dac.myfinance.api.service.ProductService;
import de.hf.dac.myfinance.api.service.InstrumentService;

import javax.inject.Inject;
import javax.inject.Named;

public class MFEnvironmentImpl implements MarketDataEnvironment{

    final private ProductService productService;
    final private InstrumentService instrumentService;
    final private String environment;

    @Inject
    public MFEnvironmentImpl(ProductService productService, InstrumentService instrumentService, @Named("envID") String environment){
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
