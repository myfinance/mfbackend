package de.hf.dac.myfinance.api.restservice;

import java.io.Serializable;
import java.time.LocalDate;

import de.hf.dac.api.rest.model.ModelBase;
import io.swagger.annotations.ApiModel;

@ApiModel
public class ValuePerDateModel  extends ModelBase implements Serializable {

    final protected double value;
    final protected LocalDate date;

    public ValuePerDateModel(double value, String datestring) {
        this.value = value;
        this.date = LocalDate.parse(datestring);
    }

    public double getValue() {
        return value;
    }

}