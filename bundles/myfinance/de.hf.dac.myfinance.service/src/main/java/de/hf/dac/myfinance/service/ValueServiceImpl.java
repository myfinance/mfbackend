/** ----------------------------------------------------------------------------
 *
 * ---                              ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : ValueServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import de.hf.dac.myfinance.api.service.ValueCurveService;
import de.hf.dac.myfinance.api.service.ValueService;

public class ValueServiceImpl implements ValueService {

    private ValueCurveService service;

    @Inject
    public ValueServiceImpl(ValueCurveService service){
        this.service = service;
    }

    @Override
    public Map<LocalDate, Double> getValueCurve(final int instrumentId) {
        return service.getValueCurve(instrumentId);
    }

    @Override
    public Map<LocalDate, Double> getValueCurve(final int instrumentId, final LocalDate startDate,
            final LocalDate endDate) {
        final Map<LocalDate, Double> adjValueCurve = new HashMap<LocalDate, Double>();
        if (startDate.isAfter(endDate) || startDate.getYear() < 1970)
            return adjValueCurve;
        final Map<LocalDate, Double> valueCurve = getValueCurve(instrumentId);
        for (final LocalDate date : valueCurve.keySet()) {
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                adjValueCurve.put(date, valueCurve.get(date));
            }
        }
        return valueCurve;
    }

    @Override
    public double getValue(final int instrumentId, final LocalDate date) {
        return service.getValue(instrumentId, date);
    }
}