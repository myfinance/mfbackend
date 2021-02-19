package de.hf.dac.myfinance.api.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstrumentDetails {

    Map<String, String> valuemap = new HashMap<>();
    List<String[]> expensesLastMonth;
    List<String[]> incomeLastMonth;

    public Map<String, String> getValuemap() {
        return valuemap;
    }

    public String getValue(String propertyName) {
        return valuemap.get(propertyName);
    }

    public String putValue(String propertyName, String value) {
        return valuemap.put(propertyName, value);
    }

    public List<String[]> getExpensesLastMonth() {
        return expensesLastMonth;
    }

    public void setExpensesLastMonth(List<String[]> expensesLastMonth) {
        this.expensesLastMonth = expensesLastMonth;
    }

    public List<String[]> getIncomeLastMonth() {
        return incomeLastMonth;
    }

    public void setIncomeLastMonth(List<String[]> incomeLastMonth) {
        this.incomeLastMonth = incomeLastMonth;
    }
}