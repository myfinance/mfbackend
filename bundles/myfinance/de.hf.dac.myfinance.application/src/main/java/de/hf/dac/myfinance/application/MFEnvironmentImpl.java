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
import de.hf.dac.myfinance.api.service.PriceService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueService;

import javax.inject.Inject;
import javax.inject.Named;

public class MFEnvironmentImpl implements MarketDataEnvironment{

    final private InstrumentService instrumentService;
    final private ValueService valueService;
    final private TransactionService transactionService;
    final private PriceService priceService;
    final private String environment;

    @Inject
    public MFEnvironmentImpl(InstrumentService instrumentService, ValueService valueService, TransactionService transactionService, PriceService priceService, @Named("envID") String environment){

        this.instrumentService=instrumentService;
        this.valueService=valueService;
        this.transactionService=transactionService;
        this.priceService=priceService;
        this.environment=environment;
    }

    @Override
    public InstrumentService getInstrumentService() {
        return instrumentService;
    }

    @Override
    public ValueService getValueService() {
        return valueService;
    }

    @Override
    public TransactionService getTransactionService() {
        return transactionService;
    }

    @Override
    public PriceService getPriceService() {
        return priceService;
    }
}
