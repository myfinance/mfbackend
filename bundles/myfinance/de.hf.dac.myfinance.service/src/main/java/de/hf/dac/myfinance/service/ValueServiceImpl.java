/** ----------------------------------------------------------------------------
 *
 * ---                              ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : ValueServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentDetails;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveService;
import de.hf.dac.myfinance.api.service.ValueService;

public class ValueServiceImpl implements ValueService {

    private ValueCurveService service;
    private TransactionService transactionService;

    @Inject
    public ValueServiceImpl(ValueCurveService service, TransactionService transactionService){
        this.service = service;
        this.transactionService = transactionService;
    }

    @Override
    public Map<LocalDate, Double> getValueCurve(final int instrumentId) {
        return service.getValueCurve(instrumentId);
    }

    @Override
    public Map<LocalDate, Double> getValueCurve(final int instrumentId, final LocalDate startDate,
            final LocalDate endDate) {
        final Map<LocalDate, Double> adjValueCurve = new TreeMap<>();
        if (startDate.isAfter(endDate) || startDate.getYear() < 1970)
            return adjValueCurve;
        final Map<LocalDate, Double> valueCurve = getValueCurve(instrumentId);
        var first = LocalDate.MIN;
        var last = LocalDate.MAX;
        for (final LocalDate date : valueCurve.keySet()) {
            if(first==LocalDate.MIN) {
                first = date;
            }
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                adjValueCurve.put(date, valueCurve.get(date));
            }
            last = date;
        }
        if(!valueCurve.isEmpty()) {
            if(first.isAfter(endDate)) {
                adjValueCurve.put(startDate, valueCurve.get(first));
                adjValueCurve.put(endDate, valueCurve.get(first));
            } else if(last.isBefore(startDate)) {
                adjValueCurve.put(startDate, valueCurve.get(last));
                adjValueCurve.put(endDate, valueCurve.get(last));
            } else if(first.isAfter(startDate)) {
                adjValueCurve.put(startDate, valueCurve.get(first));
            } else if(last.isBefore(endDate)) {
                adjValueCurve.put(endDate, valueCurve.get(last));
            }
        }
        return adjValueCurve;
    }

    @Override
    public double getValue(final int instrumentId, final LocalDate date) {
        return service.getValue(instrumentId, date);
    }

    @Override
    public Map<Integer, InstrumentDetails> getInstrumentValues(List<Instrument> instruments, LocalDate date, LocalDate diffDate) {
        Map<Integer, InstrumentDetails> valueMap = new HashMap<>();
        for (Instrument instrument : instruments) {
            double value = getValue(instrument.getInstrumentid(), date);
            double valueDiffdate = getValue(instrument.getInstrumentid(), diffDate);
            double valueChange = value - valueDiffdate;
            InstrumentDetails instrumentDetails = new InstrumentDetails();
            instrumentDetails.putValue("description", instrument.getDescription());
            instrumentDetails.putValue("instrumenttype", String.valueOf(instrument.getInstrumentType()));
            instrumentDetails.putValue("value", String.valueOf(value));
            instrumentDetails.putValue("valueDiffDate", String.valueOf(valueDiffdate));
            instrumentDetails.putValue("valueChange", String.valueOf(valueChange));
            instrumentDetails.putValue("liquiditytype", String.valueOf(instrument.getInstrumentType().getLiquidityType()));
            valueMap.put(instrument.getInstrumentid(), instrumentDetails);
            if(instrument.getInstrumentType() == InstrumentType.BUDGET || instrument.getInstrumentType() == InstrumentType.GIRO) {
                instrumentDetails = setCashflowData(instrument, instrumentDetails, date);
            }
        }
        return valueMap;
    }

    private InstrumentDetails setCashflowData(Instrument instrument, InstrumentDetails instrumentDetails, LocalDate date) {
        Map<LocalDate, List<Cashflow>> cashflowmap = transactionService.getInstrumentCashflowMap(instrument.getInstrumentid());
        double expenses = 0.0;
        LocalDate firstCashflowDate = date;
        LocalDate lastmonth = date.plusMonths(-1);
        LocalDate lastyear = date.plusYears(-1);
        double expensesLastYear = 0.0;
        List<String[]> expensesLastMonth = new ArrayList<>();
        List<String[]> incomeLastMonth = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Map.Entry<LocalDate, List<Cashflow>> entry : cashflowmap.entrySet()) {
            LocalDate currentDate = entry.getKey();
            if(currentDate.isBefore(firstCashflowDate)) {
                firstCashflowDate = currentDate;
            }
            if(currentDate.isBefore(date)) {
                for (Cashflow cashflow : entry.getValue()) {
                    double currentValue = cashflow.getValue();
                    var isExpense = false;
                    if(currentValue<0) isExpense = true;
                    if(currentDate.isAfter(lastmonth)) {
                        String[] cashflowDetails = new String[3];
                        cashflowDetails[0] = currentDate.format(formatter);
                        cashflowDetails[1] = cashflow.getDescription();
                        cashflowDetails[2] = ((Double)cashflow.getValue()).toString();
                        if(isExpense) expensesLastMonth.add(cashflowDetails);
                        else incomeLastMonth.add(cashflowDetails);
                    } 
                    if(isExpense) {
                        if(currentDate.isAfter(lastyear) && isExpense) {
                            expensesLastYear+=cashflow.getValue();
                        } 
                        expenses+=cashflow.getValue();
                    }
                }
            }            
        }
        long relevantMonth = ChronoUnit.MONTHS.between(firstCashflowDate, date) +1;
        long relevantMonthOfTheYear = 12;
        if (relevantMonth<12) relevantMonthOfTheYear = relevantMonth;
        Double avgCashoutLastYear = expensesLastYear / relevantMonthOfTheYear;
        Double avgCashoutPerMonth = expenses / relevantMonth;
        instrumentDetails.putValue("avgCashoutLastYear", avgCashoutLastYear.toString());
        instrumentDetails.putValue("avgCashoutPerMonth", avgCashoutPerMonth.toString());
        instrumentDetails.setIncomeLastMonth(incomeLastMonth);
        instrumentDetails.setExpensesLastMonth(expensesLastMonth);
        return instrumentDetails;
    }
}