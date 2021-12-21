/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : TenantValueHandler.java
 * Author(s)   : hf
 * Created     : 21.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.*;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;


public class TenantValueHandler  extends AbsValueHandler{
    private InstrumentService instrumentService;

    public TenantValueHandler(InstrumentService instrumentService, ValueCurveHandler valueCurveService){
        super(valueCurveService);
        this.instrumentService = instrumentService;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        List<Instrument> accounts = instrumentService.listAccounts(instrument.getInstrumentid());
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        if(accounts==null || accounts.isEmpty()) {
            return createZeroCurve(valueCurve);
        }
        return getCombinedValueCurve(getValueCurves(accounts));
    }
}