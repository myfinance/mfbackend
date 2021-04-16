package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.domain.Instrument;
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

        return valueCurve;
    }
}