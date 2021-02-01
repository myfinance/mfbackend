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

package de.hf.dac.myfinance.valuehandler;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.service.ValueCurveService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.TreeMap;

import javax.inject.Inject;

public class ValueCurveServiceImpl implements ValueCurveService {

    private InstrumentDao instrumentDao;
    private EndOfDayPriceDao endOfDayPriceDao;
    ValueCurveCache cache = new SimpleCurveCache();

    @Inject
    public ValueCurveServiceImpl(InstrumentDao instrumentDao, EndOfDayPriceDao endOfDayPriceDao){
        this.instrumentDao = instrumentDao;
        this.endOfDayPriceDao = endOfDayPriceDao;
    }

    public TreeMap<LocalDate, Double> getValueCurve(int instrumentId){
        TreeMap<LocalDate, Double> valueCurve = cache.getValueCurve(instrumentId);
        if(valueCurve==null){
            Optional<Instrument> instrument = instrumentDao.getInstrument(instrumentId);
            if(instrument.isPresent()){
                valueCurve = getValueHandler(instrument.get().getInstrumentType()).calcValueCurve(instrument.get());
                cache.addValueCurve(instrumentId, valueCurve);
            } else {
                throw new MFException(MFMsgKey.NO_INSTRUMENT_FOUND_EXCEPTION, "Instrument with id:"+instrumentId+" not found");
            }

        }
        return valueCurve;
    }

    private ValueHandler getValueHandler(InstrumentType instrumentType){
        ValueHandler valueHandler = null;
        switch(instrumentType.getTypeGroup()){
            case SECURITY:
                valueHandler = new SecurityValueHandler(instrumentDao, endOfDayPriceDao);
                break;
            case CASHACCOUNT:
                valueHandler = new CashAccValueHandler(instrumentDao);
                break;
            case TENANT:
                valueHandler = new TenantValueHandler(instrumentDao, this);
                break;                
            case UNKNOWN:
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
        cache.removeCurve(instrumentId);
    }
}
