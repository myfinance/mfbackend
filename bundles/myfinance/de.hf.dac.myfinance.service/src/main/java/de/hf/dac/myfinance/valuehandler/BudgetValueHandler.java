package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;

public class BudgetValueHandler extends CashAccValueHandler {

    private InstrumentService instrumentService;
    
    public BudgetValueHandler(TransactionService transactionService, InstrumentService instrumentService, ValueCurveHandler valueCurveHandler){
        super(transactionService, valueCurveHandler);
        this.instrumentService = instrumentService;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        var budgetValueCurve = super.calcValueCurve(instrument);
        var instruments2add = instrumentService.getInstrumentChilds(instrument.getInstrumentid(), EdgeType.VALUEBUDGET, 1);
        var valueCurves = getValueCurves(instruments2add);
        valueCurves.add(budgetValueCurve);
        return getCombinedValueCurve(valueCurves);
    }
}