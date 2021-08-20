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
    static Map<Integer, Map<Integer, TreeMap<LocalDate, Double>>> positionCurveCache = new HashMap<>();
    static Map<Integer, Map<Integer, TreeMap<LocalDate, Double>>> positionValueCurveCache = new HashMap<>();

    @Override
    public TreeMap<LocalDate, Double> getValueCurve(int instrumentId) {
        return valueCurveCache.get(instrumentId);
    }

    @Override
    public Map<Integer, TreeMap<LocalDate, Double>> getPositionCurve(int instrumentId) {
        return positionCurveCache.get(instrumentId);
    }

    @Override
    public Map<Integer, TreeMap<LocalDate, Double>> getPositionValueCurve(int instrumentId) {
        return positionValueCurveCache.get(instrumentId);
    }

    @Override
    public void addValueCurve(int instrumentId, TreeMap<LocalDate, Double> valueCurve) {
        valueCurveCache.put(instrumentId, valueCurve);
    }

    @Override
    public void addPositionCurve(int instrumentId, Map<Integer, TreeMap<LocalDate, Double>> positionCurve) {
        positionCurveCache.put(instrumentId, positionCurve);
    }

    @Override
    public void addPositionValueCurve(int instrumentId, Map<Integer, TreeMap<LocalDate, Double>> positionCurve) {
        positionValueCurveCache.put(instrumentId, positionCurve);
    }

    @Override
    public void removeCurve(int instrumentId) {
        valueCurveCache.remove(instrumentId);
        positionCurveCache.remove(instrumentId);
    }

    @Override
    public void flush() {
        valueCurveCache.clear();
    }
}
