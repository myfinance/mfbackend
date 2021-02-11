/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : CashAccValueHandler.java
 * Author(s)   : xn01598
 * Created     : 21.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.*;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.service.InstrumentService;


public class CashAccValueHandler implements ValueHandler{
    private InstrumentService instrumentService;

    public CashAccValueHandler(InstrumentService instrumentService){
        this.instrumentService = instrumentService;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        Map<LocalDate, List<Cashflow>> cashflows = instrumentService.getInstrumentCashflowMap(instrument.getInstrumentid());
        double value = 0.0;
        if(cashflows==null || cashflows.isEmpty()) {
            valueCurve.put(LocalDate.now(), value);
        } else {
            SortedSet<LocalDate> sortedDates = new TreeSet<>(cashflows.keySet());
            LocalDate lastDate = sortedDates.first();
            //add initial 0 value before the first cashflow
            valueCurve.put(lastDate.minusDays(1), value);
            Iterator<LocalDate> iter = sortedDates.iterator();
            while(iter.hasNext()) {

                LocalDate nextExistingDate = iter.next();
                while(lastDate.isBefore(nextExistingDate)){
                    valueCurve.put(lastDate, value);
                    lastDate=lastDate.plusDays(1);
                }
                lastDate=nextExistingDate.plusDays(1);
                List<Cashflow> cashflow = cashflows.get(nextExistingDate);
                value += cashflow.stream().mapToDouble(Cashflow::getValue).sum();
                valueCurve.put(nextExistingDate, value);
            }
        }
        return valueCurve;
    }
}
