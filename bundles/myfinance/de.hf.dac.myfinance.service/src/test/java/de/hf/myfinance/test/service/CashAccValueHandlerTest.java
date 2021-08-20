package de.hf.myfinance.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import org.junit.jupiter.api.Test;
import de.hf.dac.myfinance.service.SimpleCurveCache;
import de.hf.dac.myfinance.service.ValueCurveServiceImpl;
import de.hf.myfinance.test.mock.EndOfDayPriceDaoTestImpl;
import de.hf.myfinance.test.mock.InstrumentServiceTestImpl;
import de.hf.myfinance.test.mock.TransactionServiceMock;

public class CashAccValueHandlerTest {

    @Test
    public void firsttest(){
        ValueCurveServiceImpl valueservice = new ValueCurveServiceImpl(
            new InstrumentServiceTestImpl(), 
            new EndOfDayPriceDaoTestImpl(), 
            new SimpleCurveCache(), 
            new TransactionServiceMock());
        String yearStr = "2020";
        Year year = Year.parse(yearStr);
        LocalDate date = year.atMonth(Month.JANUARY).atDay(1);
        assertEquals(0.0, valueservice.getValue(1, date));

    }
}