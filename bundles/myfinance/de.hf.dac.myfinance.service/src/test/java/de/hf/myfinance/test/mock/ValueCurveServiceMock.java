package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.service.ValueCurveService;

public class ValueCurveServiceMock implements ValueCurveService {

    @Override
    public TreeMap<LocalDate, Double> getValueCurve(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getValue(int instrumentId, LocalDate date) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void updateCache(int instrumentId) {
        // TODO Auto-generated method stub

    }
    
}