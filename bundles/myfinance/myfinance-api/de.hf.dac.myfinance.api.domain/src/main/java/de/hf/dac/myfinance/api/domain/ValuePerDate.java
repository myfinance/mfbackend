package de.hf.dac.myfinance.api.domain;

import java.time.LocalDate;

public class ValuePerDate {

    final protected double value;
    final protected LocalDate date;

    public ValuePerDate(String valuePerDateString) {
        String[] values = valuePerDateString.split(",");
        this.value = Double.parseDouble(values[0]);
        this.date = LocalDate.parse(values[1]);
    }

    public double getValue() {
        return value;
    }

}