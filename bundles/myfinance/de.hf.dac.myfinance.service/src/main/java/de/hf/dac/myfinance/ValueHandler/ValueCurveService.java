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

package de.hf.dac.myfinance.ValueHandler;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MDException;
import de.hf.dac.myfinance.api.exceptions.MDMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

import java.time.LocalDate;
import java.util.Optional;
import java.util.TreeMap;

public class ValueCurveService {

    private InstrumentDao instrumentDao;

    public ValueCurveService(InstrumentDao instrumentDao){
        this.instrumentDao = instrumentDao;
    }

    ValueCurveCache cache = new SimpleCurveCache();

    public TreeMap<LocalDate, Double> getValueCurve(int instrumentId){
        TreeMap<LocalDate, Double> valueCurve = cache.getValueCurve(instrumentId);
        if(valueCurve==null){
            Optional<Instrument> instrument = instrumentDao.getInstrument(instrumentId);
            if(instrument.isPresent()){
                valueCurve = getValueHandler(instrument.get().getInstrumentType()).calcValueCurve(instrument.get());
                cache.addValueCurve(instrumentId, valueCurve);
            } else {
                throw new MDException(MDMsgKey.NO_INSTRUMENT_FOUND_EXCEPTION, "Instrument with id:"+instrumentId+" not found");
            }

        }
        return valueCurve;
    }

    public ValueHandler getValueHandler(InstrumentType instrumentType){
        ValueHandler valueHandler = null;
        switch(instrumentType){
            case SECURITY:
                valueHandler = new SecurityValueHandler(instrumentDao);
                break;
            case UNKNOWN:
                throw new MDException(MDMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "Type:"+instrumentType);
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
}
