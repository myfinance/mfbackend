package de.hf.dac.myfinance.api.service;

import java.time.LocalDate;
import java.util.TreeMap;

public interface ValueCurveService {
    TreeMap<LocalDate, Double> getValueCurve(int instrumentId);
    double getValue(int instrumentId, LocalDate date);
    void updateCache(int instrumentId);
}