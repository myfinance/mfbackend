package de.hf.dac.myfinance.api.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public interface ValueCurveHandler {
    TreeMap<LocalDate, Double> getValueCurve(int instrumentId);
    Map<LocalDate, Double> getValueCurve(final int instrumentId, final LocalDate startDate, final LocalDate endDate);
    double getValue(int instrumentId, LocalDate date);
    double getValue(TreeMap<LocalDate, Double> valueCurve, LocalDate date);
    Map<Integer, TreeMap<LocalDate, Double>> getPositionCurve(int depotId);
    Map<Integer, TreeMap<LocalDate, Double>> getPositionValueCurve(int depotId);
    void invalidateCache(int instrumentId);
}