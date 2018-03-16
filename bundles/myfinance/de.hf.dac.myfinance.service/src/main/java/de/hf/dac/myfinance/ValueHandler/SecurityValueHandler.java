/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SecurityValueHandler.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 16.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.ValueHandler;

import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class SecurityValueHandler implements ValueHandler{
    private InstrumentDao instrumentDao;

    public SecurityValueHandler(InstrumentDao instrumentDao){
        this.instrumentDao = instrumentDao;
    }


    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument){
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();

        Map<LocalDate, EndOfDayPrice> prices = instrumentDao.listEndOfDayPrices(instrument.getInstrumentid()).stream().collect(
            Collectors.toMap(x->x.getDayofprice(), x->x));

        SortedSet<LocalDate> sortedDates = new TreeSet<>(prices.keySet());

        LocalDate minDate = sortedDates.first();
        LocalDate nextDate = minDate;
        double lastPrice = 0.0;
        Iterator<LocalDate> iter = sortedDates.iterator();
        while(iter.hasNext()){
            LocalDate nextExistingDate = iter.next();
            EndOfDayPrice price = prices.get(nextExistingDate);
            double valueInEUr;
            if(price.getCurrency().getCurrencycode().equals("EUR")){
                valueInEUr = price.getValue();
            } else {
                valueInEUr=convertValueToEur(price);
            }
            valueCurve.put(nextExistingDate, valueInEUr);

            if(nextDate.isBefore(nextExistingDate)){
                long diff = nextDate.until(nextExistingDate, DAYS)+1;
                double valueAddOn = 0.0;
                if(lastPrice>0.0) {
                    valueAddOn=(valueInEUr-lastPrice)/diff;
                }
                while(nextDate.isBefore(nextExistingDate)){
                    lastPrice+=valueAddOn;
                    valueCurve.put(nextDate, lastPrice);
                    nextDate = nextDate.plusDays(1);
                }
            }
            nextDate = nextDate.plusDays(1);
            lastPrice=valueInEUr;
        }

        return valueCurve;
    }

    private double convertValueToEur(EndOfDayPrice price){
        double valueInEUr;
        ValueCurveService service = new ValueCurveService(instrumentDao);
        double curValue = service.getValue(price.getCurrency().getInstrumentid(), price.getDayofprice());
        valueInEUr = price.getValue() * curValue;
        return valueInEUr;
    }
}
