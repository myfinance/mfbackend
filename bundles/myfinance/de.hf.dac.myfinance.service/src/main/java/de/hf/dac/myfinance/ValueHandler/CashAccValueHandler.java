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

package de.hf.dac.myfinance.ValueHandler;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;


public class CashAccValueHandler implements ValueHandler{
    private InstrumentDao instrumentDao;

    public CashAccValueHandler(InstrumentDao instrumentDao){
        this.instrumentDao = instrumentDao;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        Map<LocalDate, Cashflow> cashflows = instrumentDao.getInstrumentCashflowMap(instrument.getInstrumentid());

        SortedSet<LocalDate> sortedDates = new TreeSet<>(cashflows.keySet());

        LocalDate lastDate = sortedDates.first();
        double value = 0.0;
        Iterator<LocalDate> iter = sortedDates.iterator();
        while(iter.hasNext()) {

            LocalDate nextExistingDate = iter.next();
            while(lastDate.isBefore(nextExistingDate)){
                valueCurve.put(lastDate, value);
                lastDate=lastDate.plusDays(1);
            }
            lastDate=nextExistingDate.plusDays(1);
            Cashflow cashflow = cashflows.get(nextExistingDate);

            value = value + cashflow.getValue();
            valueCurve.put(nextExistingDate, value);

        }


        return valueCurve;
    }
}
