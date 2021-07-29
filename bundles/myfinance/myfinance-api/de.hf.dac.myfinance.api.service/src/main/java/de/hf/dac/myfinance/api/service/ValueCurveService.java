package de.hf.dac.myfinance.api.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public interface ValueCurveService {
    TreeMap<LocalDate, Double> getValueCurve(int instrumentId);
    double getValue(int instrumentId, LocalDate date);
    Map<Integer, TreeMap<LocalDate, Double>> getPositionCurve(int depotId);
    Map<Integer, TreeMap<LocalDate, Double>> getPositionValueCurve(int depotId);
    void invalidateCache(int instrumentId);
}