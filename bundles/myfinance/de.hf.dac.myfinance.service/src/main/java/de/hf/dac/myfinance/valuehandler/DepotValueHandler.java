package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;

public class DepotValueHandler extends AbsValueHandler {
    private final TransactionService transactionService;
    private LocalDate startDate = LocalDate.now();

    public DepotValueHandler(final TransactionService transactionService, final ValueCurveHandler valueCurveService) {
        super(valueCurveService);
        this.transactionService = transactionService;
    }

    public Map<Integer, TreeMap<LocalDate, Double>> calcPositionsCurve(int depotId) {
        return calcPositions(prepareCalculation(depotId));
    }

    private Map<Integer, TreeMap<LocalDate, Double>> prepareCalculation(int depotId) {
        final List<Transaction> transactions = transactionService.getTrades(depotId);
        final var tradeAmounts = new HashMap<Integer, TreeMap<LocalDate, Double>>();
        for (final Transaction transaction : transactions) {
            final var amountMap = new TreeMap<LocalDate, Double>();
            transaction.getTrades().forEach(i -> {
                double amount = i.getAmount();
                int securityId = i.getInstrument().getInstrumentid();
                if (tradeAmounts.containsKey(securityId)) {
                    amountMap.putAll(tradeAmounts.get(securityId));
                }
                amountMap.put(transaction.getTransactiondate(), amount);
                tradeAmounts.put(securityId, amountMap);
            });

            if (transaction.getTransactiondate().isBefore(startDate)) {
                startDate = transaction.getTransactiondate();
            }
        }
        return tradeAmounts;
    }

    private HashMap<Integer, TreeMap<LocalDate, Double>> calcPositions(
        final Map<Integer, TreeMap<LocalDate, Double>> tradeAmounts) {
        final var positions = new HashMap<Integer, TreeMap<LocalDate, Double>>();
        for (final Map.Entry<Integer, TreeMap<LocalDate, Double>> tradeAmountsEntry : tradeAmounts.entrySet()) {
            LocalDate theDate = startDate;
            final var securityTradeAmounts = tradeAmountsEntry.getValue();
            double currentAmount = 0.0;
            final var securityPosition = new TreeMap<LocalDate, Double>();
            while (!theDate.isAfter(LocalDate.now())) {
                if (securityTradeAmounts.containsKey(theDate)) {
                    currentAmount += securityTradeAmounts.get(theDate);
                }
                securityPosition.put(theDate, currentAmount);
                theDate=theDate.plusDays(1);
            }
            positions.put(tradeAmountsEntry.getKey(), securityPosition);
        }
        return positions;
    }

    public Map<Integer, TreeMap<LocalDate, Double>> calcPositionsValueCurve(
            Map<Integer, TreeMap<LocalDate, Double>> positonCurve) {
        var postionValueCurves = new HashMap<Integer, TreeMap<LocalDate, Double>>();
        for (final Map.Entry<Integer, TreeMap<LocalDate, Double>> securityEntry : positonCurve.entrySet()) {
            var positionValueCurve = new TreeMap<LocalDate, Double>();
            int securityId = securityEntry.getKey();
            for (final Entry<LocalDate, Double> positonCurveEntry : securityEntry.getValue().entrySet()) {
                var valdate = positonCurveEntry.getKey();
                positionValueCurve.put(valdate, positonCurveEntry.getValue() * valueCurveService.getValue(securityId, valdate));
            }
            postionValueCurves.put(securityId, positionValueCurve);
        }
        return postionValueCurves;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(final Instrument instrument) {
        var valueCurve = new TreeMap<LocalDate, Double>();
        final var positionValues = valueCurveService.getPositionValueCurve(instrument.getInstrumentid());
        for (final Map.Entry<Integer, TreeMap<LocalDate, Double>> securityEntry : positionValues.entrySet()) {
            for (final Entry<LocalDate, Double> positonValueCurveEntry : securityEntry.getValue().entrySet()) {
                var valdate = positonValueCurveEntry.getKey();
                double value = 0.0;
                if(valueCurve.containsKey(valdate)) {
                    value = valueCurve.get(valdate);
                }
                value += positonValueCurveEntry.getValue();
                valueCurve.put(valdate, value);
            }
        }
        return valueCurve;
    }
}