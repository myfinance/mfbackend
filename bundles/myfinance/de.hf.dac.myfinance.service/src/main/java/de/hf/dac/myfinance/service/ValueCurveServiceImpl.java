/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ValueCurveService.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.service;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveCache;
import de.hf.dac.myfinance.api.service.ValueCurveService;
import de.hf.dac.myfinance.valuehandler.CashAccValueHandler;
import de.hf.dac.myfinance.valuehandler.PortfolioValueHandler;
import de.hf.dac.myfinance.valuehandler.SecurityValueHandler;
import de.hf.dac.myfinance.valuehandler.TenantValueHandler;
import de.hf.dac.myfinance.valuehandler.ValueHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.inject.Inject;

public class ValueCurveServiceImpl implements ValueCurveService {

    private InstrumentService instrumentService;
    private TransactionService transactionService;
    private EndOfDayPriceDao endOfDayPriceDao;
    ValueCurveCache cache;

    @Inject
    public ValueCurveServiceImpl(InstrumentService instrumentService, EndOfDayPriceDao endOfDayPriceDao, ValueCurveCache cache, TransactionService transactionService){
        this.instrumentService = instrumentService;
        this.endOfDayPriceDao = endOfDayPriceDao;
        this.transactionService = transactionService;
        this.cache = cache;
    }

    public TreeMap<LocalDate, Double> getValueCurve(int instrumentId){
        TreeMap<LocalDate, Double> valueCurve = cache.getValueCurve(instrumentId);
        if(valueCurve==null){
            var instrument = instrumentService.getInstrument(instrumentId);
            valueCurve = getValueHandler(instrument.getInstrumentType()).calcValueCurve(instrument);
            cache.addValueCurve(instrumentId, valueCurve);

        }
        return valueCurve;
    }

    private ValueHandler getValueHandler(InstrumentType instrumentType){
        ValueHandler valueHandler;
        switch(instrumentType.getTypeGroup()){
            case SECURITY:
                valueHandler = new SecurityValueHandler(this, endOfDayPriceDao);
                break;
            case CASHACCOUNT:
                valueHandler = new CashAccValueHandler(transactionService);
                break;
            case TENANT:
                valueHandler = new TenantValueHandler(instrumentService, this);
                break;  
            case PORTFOLIO:
                valueHandler = new PortfolioValueHandler(instrumentService, this);
                break;                                
            case DEPRECATIONOBJECT:
            case LIVEINSURANCE:
            case LOAN:
            case REALESTATE:
            case UNKNOWN:           
            default:
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "Type:"+instrumentType);   
        }
        return valueHandler;
    }

    public double getValue(int instrumentId, LocalDate date){
        double value=0.0;
        TreeMap<LocalDate, Double> valueCurve = getValueCurve(instrumentId);
        if(valueCurve.containsKey(date)) {
            value=valueCurve.get(date);
        } else if(valueCurve.firstKey().isAfter(date)){
            value = valueCurve.get(valueCurve.firstKey());
        } else if(valueCurve.lastKey().isBefore(date)){
            value = valueCurve.get(valueCurve.lastKey());
        }
        return value;
    }

    public void updateCache(int instrumentId){
        List<InstrumentGraphEntry> ancestorGraphEntries = instrumentService.getAncestorGraphEntries(instrumentId, EdgeType.TENANTGRAPH);
        if(ancestorGraphEntries != null && !ancestorGraphEntries.isEmpty()){
            for (InstrumentGraphEntry entry : ancestorGraphEntries) {
                cache.removeCurve(entry.getId().getAncestor());
            }
        }
    }
}
