/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : PortfolioValueHandler.java
 * Author(s)   : HF
 * Created     : 11.02.2021
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.*;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveService;


public class PortfolioValueHandler extends AbsValueHandler{
    private InstrumentService instrumentService;

    public PortfolioValueHandler(InstrumentService instrumentService, ValueCurveService valueCurveService){
        super(valueCurveService);
        this.instrumentService = instrumentService;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        List<Instrument>  childs = instrumentService.getInstrumentChilds(instrument.getInstrumentid(), EdgeType.TENANTGRAPH, 1);
        if(childs==null || childs.isEmpty()) {
            return createZeroCurve(valueCurve);
        }
        LocalDate startDate = LocalDate.now();
        for (Instrument child : childs) {
            TreeMap<LocalDate, Double> childValueCurve = this.valueCurveService.getValueCurve(child.getInstrumentid());
            LocalDate minDate = childValueCurve.firstKey();
            if(minDate.isBefore(startDate)) {
                startDate = minDate;
            }
        }
        return getCombinedValueCurve(childs, startDate);
    }
}