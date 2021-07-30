package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.service.ValueCurveService;

public class ValueCurveServiceMock implements ValueCurveService {

    Map<Integer, TreeMap<LocalDate, Double>> positionValueCurve;

    @Override
    public TreeMap<LocalDate, Double> getValueCurve(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getValue(int instrumentId, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate firstDate = LocalDate.parse("2019-01-01", formatter);
        LocalDate secondDate = LocalDate.parse("2020-01-02", formatter);
        LocalDate thirdDate = LocalDate.parse("2020-01-04", formatter);
        double value=0.0;
        if(date.isBefore(firstDate)) {
            value = 0.0;
        } else if(date.isBefore(secondDate)) {
            value = 10.0;
        } else if(date.isBefore(thirdDate)) {
            value = 15.0;
        } else {
            value = 12.0;
        } 

        if(instrumentId==2) {
            value*=2;
        }
        return value;
    }

    @Override
    public void invalidateCache(int instrumentId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<Integer, TreeMap<LocalDate, Double>> getPositionCurve(int depotId) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setPositionValueCurve(Map<Integer, TreeMap<LocalDate, Double>> positionValueCurve) {
        this.positionValueCurve = positionValueCurve;
    }

    @Override
    public Map<Integer, TreeMap<LocalDate, Double>> getPositionValueCurve(int depotId) {

        return this.positionValueCurve;
    }
    
}