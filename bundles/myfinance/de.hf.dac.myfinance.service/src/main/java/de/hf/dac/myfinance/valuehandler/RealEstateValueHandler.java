package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.service.InstrumentService;

public class RealEstateValueHandler extends AbsValueHandler{
    private InstrumentService instrumentService;

    public RealEstateValueHandler(InstrumentService instrumentService, ValueCurveService valueCurveService){
        super(valueCurveService);
        this.instrumentService = instrumentService;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        var  childs = instrumentService.getInstrumentChilds(instrument.getInstrumentid(), EdgeType.TENANTGRAPH, 1);
        if(childs==null || childs.isEmpty()) {
            return createZeroCurve(valueCurve);
        }
        LocalDate startDate = LocalDate.now();
        for (Instrument child : childs) {
            TreeMap<LocalDate, Double> childValueCurve = this.valueCurveService.getValueCurve(child.getInstrumentid());
            LocalDate minDate = childValueCurve.firstKey();
            if(minDate.isBefore(startDate)) {
                startDate = minDate;
            }
        }
        return getCombinedValueCurve(childs, startDate);
    }
}