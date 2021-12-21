package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentDetails;
import de.hf.dac.myfinance.api.service.ValueService;

public class ValueCurveServiceMock implements ValueService {
    boolean returnsValue = true;

    @Override
    public TreeMap<LocalDate, Double> getValueCurve(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param returnsValue the returnsValue to set
     */
    public void setReturnsValue(boolean returnsValue) {
        this.returnsValue = returnsValue;
    }

    @Override
    public double getValue(int instrumentId, LocalDate date) {
        if (!returnsValue)
            return 0.0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate firstDate = LocalDate.parse("2019-01-01", formatter);
        LocalDate secondDate = LocalDate.parse("2020-01-02", formatter);
        LocalDate thirdDate = LocalDate.parse("2020-01-04", formatter);
        double value = 0.0;
        if (date.isBefore(firstDate)) {
            value = 0.0;
        } else if (date.isBefore(secondDate)) {
            value = 10.0;
        } else if (date.isBefore(thirdDate)) {
            value = 15.0;
        } else {
            value = 12.0;
        }

        if (instrumentId == 2) {
            value *= 2;
        }
        return value;
    }


    @Override
    public Map<LocalDate, Double> getValueCurve(int instrumentId, LocalDate startDate, LocalDate endDate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Integer, InstrumentDetails> getInstrumentValues(List<Instrument> instruments, LocalDate date,
            LocalDate diffDate) {
        // TODO Auto-generated method stub
        return null;
    }
    
}