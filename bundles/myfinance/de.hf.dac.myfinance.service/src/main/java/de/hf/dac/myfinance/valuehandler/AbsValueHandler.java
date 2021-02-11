package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public abstract class AbsValueHandler implements ValueHandler {

    protected ValueCurveService valueCurveService;

    protected AbsValueHandler(ValueCurveService valueCurveService){
        this.valueCurveService = valueCurveService;
    }

    protected TreeMap<LocalDate, Double> createZeroCurve(TreeMap<LocalDate, Double> valueCurve) {
        valueCurve.put(LocalDate.now(), 0.0);
        return valueCurve;
    }

    protected TreeMap<LocalDate, Double> getCombinedValueCurve(List<Instrument>  accs, LocalDate startDate) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        while(!startDate.isAfter(LocalDate.now())) {
            double value = 0.0;
            for (Instrument accValueCurve : accs) {
                value+=valueCurveService.getValue(accValueCurve.getInstrumentid(), startDate);
            }
            valueCurve.put(startDate, value);
            startDate = startDate.plusDays(1);
        }
        return valueCurve;
    }
}