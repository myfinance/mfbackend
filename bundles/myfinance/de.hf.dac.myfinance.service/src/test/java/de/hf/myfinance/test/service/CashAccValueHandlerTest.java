package de.hf.myfinance.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import org.junit.jupiter.api.Test;

import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.service.ValueCurveCache;
import de.hf.dac.myfinance.service.SimpleCurveCache;
import de.hf.dac.myfinance.service.ValueCurveServiceImpl;
import de.hf.myfinance.test.mock.EndOfDayPriceDaoTestImpl;
import de.hf.myfinance.test.mock.InstrumentDaoTestImpl;

public class CashAccValueHandlerTest {

    @Test
    public void firsttest(){
        InstrumentDao instrumentDao = new InstrumentDaoTestImpl();
        EndOfDayPriceDao endOfDayPriceDao = new EndOfDayPriceDaoTestImpl();
        ValueCurveCache cache = new SimpleCurveCache();
        ValueCurveServiceImpl valueservice = new ValueCurveServiceImpl(instrumentDao, endOfDayPriceDao, cache);
        String yearStr = "2020";
        Year year = Year.parse(yearStr);
        LocalDate date = year.atMonth(Month.JANUARY).atDay(1);
        assertEquals(0.0,new ValueCurveServiceImpl(new InstrumentDaoTestImpl(), new EndOfDayPriceDaoTestImpl(), new SimpleCurveCache()).getValue(1, LocalDate.now()));

    }
}