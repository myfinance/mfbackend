package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;

public abstract class AbsValueHandler implements ValueHandler {

    protected ValueCurveHandler valueCurveService;

    protected AbsValueHandler(ValueCurveHandler valueCurveService){
        this.valueCurveService = valueCurveService;
    }

    protected TreeMap<LocalDate, Double> createZeroCurve(TreeMap<LocalDate, Double> valueCurve) {
        valueCurve.put(LocalDate.now(), 0.0);
        return valueCurve;
    }

    protected TreeMap<LocalDate, Double> getCombinedValueCurve(List<TreeMap<LocalDate, Double>> valueCurves) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        var currentDate = calcCurveStartDate(valueCurves);
        while(!currentDate.isAfter(LocalDate.now())) {
            double value = 0.0;
            for (TreeMap<LocalDate, Double> singleValueCurve : valueCurves) {
                // use the valueCurveService to get the value instead of just map.get to handle not existing values correctly
                value+=valueCurveService.getValue(singleValueCurve, currentDate);
            }
            valueCurve.put(currentDate, value);
            currentDate = currentDate.plusDays(1);
        }
        return valueCurve;
    }

    protected LocalDate calcCurveStartDate(List<TreeMap<LocalDate, Double>> valueCurves) {
        LocalDate startDate = LocalDate.now();
        for (TreeMap<LocalDate, Double> childValueCurve : valueCurves) {
            LocalDate minDate = childValueCurve.firstKey();
            if(minDate.isBefore(startDate)) {
                startDate = minDate;
            }
        }
        return startDate;
    }

    protected List<TreeMap<LocalDate, Double>> getValueCurves(List<Instrument>  instruments) {
        List<TreeMap<LocalDate, Double>> valueCurves = new ArrayList<TreeMap<LocalDate, Double>>();
        for (Instrument instrument : instruments) {
            valueCurves.add(valueCurveService.getValueCurve(instrument.getInstrumentid()));
        }
        return valueCurves;
    }
}