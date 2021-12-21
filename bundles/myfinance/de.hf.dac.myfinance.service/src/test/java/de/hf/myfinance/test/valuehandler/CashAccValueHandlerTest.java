package de.hf.myfinance.test.valuehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import org.junit.jupiter.api.Test;

import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.myfinance.test.AbsTest;

public class CashAccValueHandlerTest extends AbsTest {

    @Test
    public void firsttest(){
        initTest();

        Instrument giro = new Giro("sec1", true, ts);
        instrumentDao.saveInstrument(giro);

        String yearStr = "2020";
        Year year = Year.parse(yearStr);
        LocalDate date = year.atMonth(Month.JANUARY).atDay(1);
        assertEquals(0.0, valueCurveHandler.getValue(giro.getInstrumentid(), date));

    }
}