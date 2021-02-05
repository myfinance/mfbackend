package de.hf.dac.myfinance.api.service;

import java.time.LocalDate;
import java.util.Map;

public interface ValueService {
    Map<LocalDate, Double> getValueCurve(int instrumentId);
    Map<LocalDate, Double> getValueCurve(int instrumentId, LocalDate startDate, LocalDate endDate);
    double getValue(int instrumentId, LocalDate date);
}