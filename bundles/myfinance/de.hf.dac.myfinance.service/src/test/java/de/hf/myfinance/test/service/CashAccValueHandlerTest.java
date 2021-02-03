package de.hf.myfinance.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals(60,60);
    }
}