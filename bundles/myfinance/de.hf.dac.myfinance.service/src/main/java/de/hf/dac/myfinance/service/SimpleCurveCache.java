/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SimpleCurveCache.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.service.ValueCurveCache;

public class SimpleCurveCache implements ValueCurveCache {

    static Map<Integer, TreeMap<LocalDate, Double>> valueCurveCache = new HashMap<>();

    @Override
    public TreeMap<LocalDate, Double> getValueCurve(int instrumentId) {
        return valueCurveCache.get(instrumentId);
    }

    @Override
    public void addValueCurve(int instrumentId, TreeMap<LocalDate, Double> valueCurve) {
        valueCurveCache.put(instrumentId, valueCurve);
    }

    @Override
    public void removeCurve(int instrumentId) {
        valueCurveCache.remove(instrumentId);
    }

    @Override
    public void flush() {
        valueCurveCache.clear();
    }
}
