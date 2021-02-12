package de.hf.dac.myfinance.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentDetails;

public interface ValueService {
    Map<LocalDate, Double> getValueCurve(int instrumentId);
    Map<LocalDate, Double> getValueCurve(int instrumentId, LocalDate startDate, LocalDate endDate);
    double getValue(int instrumentId, LocalDate date);
    Map<Integer, InstrumentDetails> getAccValues(List<Instrument> accounts, LocalDate date, LocalDate diffDate);
}