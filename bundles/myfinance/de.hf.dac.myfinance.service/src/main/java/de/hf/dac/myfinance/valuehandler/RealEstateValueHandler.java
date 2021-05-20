package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.TreeMap;
import java.util.TreeSet;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class RealEstateValueHandler extends AbsValueHandler{
    private InstrumentService instrumentService;

    public RealEstateValueHandler(InstrumentService instrumentService, ValueCurveService valueCurveService){
        super(valueCurveService);
        this.instrumentService = instrumentService;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        TreeMap<LocalDate, Double> yieldgoals = new TreeMap<>();
        TreeMap<LocalDate, Double> profits = new TreeMap<>();
        var dates = new TreeSet<LocalDate>();
        var instrumentProperties = instrumentService.getInstrumentProperties(instrument.getInstrumentid());
        if(instrumentProperties==null || instrumentProperties.isEmpty()) {
            valueCurve.put(LocalDate.now(), 0.0);
            return valueCurve;
        }
        for(var instrumentProperty : instrumentProperties) {
            if(instrumentProperty.getPropertyname().equals(InstrumentPropertyType.REALESTATEPROFITS.getStringValue())) {
                profits.put(instrumentProperty.getValidfrom(), Double.parseDouble(instrumentProperty.getValue()));
            } else if(instrumentProperty.getPropertyname().equals(InstrumentPropertyType.YIELDGOAL.getStringValue())) {
                yieldgoals.put(instrumentProperty.getValidfrom(), Double.parseDouble(instrumentProperty.getValue()));
            }
            dates.add(instrumentProperty.getValidfrom());
        }
        var currentYieldgoal = 0.0;
        var currentProfit = 0.0;
        Double currentValue;
        for( var date : dates) {
            if(profits.containsKey(date)) {
                currentProfit = profits.get(date);
            }
            if(yieldgoals.containsKey(date)) {
                currentYieldgoal = yieldgoals.get(date);
            }
            if(currentYieldgoal == 0.0) {
                currentValue = 0.0;
            } else {
                currentValue = currentProfit * 12 / currentYieldgoal;
            }
            
            valueCurve.put(date, currentValue);
        }
        return valueCurve;
    }
}