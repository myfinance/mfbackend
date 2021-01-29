/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : TenantValueHandler.java
 * Author(s)   : xn01598
 * Created     : 21.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.ValueHandler;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;


public class TenantValueHandler implements ValueHandler{
    private InstrumentDao instrumentDao;
    private ValueCurveService valueCurveService;

    public TenantValueHandler(InstrumentDao instrumentDao, ValueCurveService valueCurveService){
        this.instrumentDao = instrumentDao;
        this.valueCurveService = valueCurveService;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        List<Instrument>  childs = instrumentDao.getInstrumentChilds(instrument.getInstrumentid(), EdgeType.TENANTGRAPH, 1);
        if(childs==null || childs.isEmpty()) {
            return createZeroCurve(valueCurve);
        }
        Optional<Instrument> accPF = childs.stream().filter(i -> i.getInstrumentType().equals(InstrumentType.AccountPortfolio)).findFirst();
        if(!accPF.isPresent()) {
            return createZeroCurve(valueCurve);
        }
        List<Instrument>  accs = instrumentDao.getInstrumentChilds(accPF.get().getInstrumentid(), EdgeType.TENANTGRAPH, 1);
        if(accs==null || accs.isEmpty()) {
            return createZeroCurve(valueCurve);
        }
        List<TreeMap<LocalDate, Double>> accValueCurveList = new ArrayList<TreeMap<LocalDate, Double>>();
        LocalDate startDate = LocalDate.now();
        for (Instrument acc : accs) {
            TreeMap<LocalDate, Double> accValueCurve = this.valueCurveService.getValueCurve(acc.getInstrumentid());
            accValueCurveList.add(accValueCurve);
            LocalDate minDate = accValueCurve.firstKey();
            if(minDate.isBefore(startDate)) {
                startDate = minDate;
            }
        }

        return valueCurve;
    }

    private TreeMap<LocalDate, Double> createZeroCurve(TreeMap<LocalDate, Double> valueCurve) {
        valueCurve.put(LocalDate.now(), 0.0);
        return valueCurve;
    }
}