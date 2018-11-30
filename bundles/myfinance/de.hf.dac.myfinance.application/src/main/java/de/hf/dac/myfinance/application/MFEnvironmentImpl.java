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
import de.hf.dac.myfinance.api.service.InstrumentService;

import javax.inject.Inject;
import javax.inject.Named;

public class MFEnvironmentImpl implements MarketDataEnvironment{

    final private InstrumentService instrumentService;
    final private String environment;

    @Inject
    public MFEnvironmentImpl(InstrumentService instrumentService, @Named("envID") String environment){

        this.instrumentService=instrumentService;
        this.environment=environment;
    }

    @Override
    public InstrumentService getInstrumentService() {
        return instrumentService;
    }


}
