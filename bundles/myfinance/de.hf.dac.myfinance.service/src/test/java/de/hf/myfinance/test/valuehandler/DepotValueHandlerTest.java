package de.hf.myfinance.test.valuehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import de.hf.dac.myfinance.api.domain.Currency;
import de.hf.dac.myfinance.api.domain.Depot;
import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.Source;
import de.hf.dac.myfinance.valuehandler.DepotValueHandler;
import de.hf.myfinance.test.AbsTest;
import de.hf.myfinance.test.testcasegenerator.TradeTestCaseGenerator;

public class DepotValueHandlerTest extends AbsTest{
    @Test
    public void positionBuildingTest(){
        initTest();

        LocalDate firstTransactionDate = LocalDate.parse("2020-01-01", formatter);
        LocalDate secondTransactionDate = LocalDate.parse("2020-01-03", formatter);
        LocalDate thirdTransactionDate = LocalDate.parse("2020-01-05", formatter);

        Instrument security1 = new Equity("sec1", "security1", true, ts);
        instrumentDao.saveInstrument(security1);
        int securityid1 = security1.getInstrumentid();
        Instrument security2 = new Equity("sec2", "security2", true, ts);
        instrumentDao.saveInstrument(security2);
        int securityid2 = security2.getInstrumentid();

        Instrument currency = new Currency("EUR", "Euro", true, ts);
        instrumentDao.saveInstrument(currency);
        var source = new Source("mocksource", true);

        TradeTestCaseGenerator tradeTestCaseGenerator = new TradeTestCaseGenerator();
        tradeTestCaseGenerator.addTrade(firstTransactionDate, securityid1, 2);
        tradeTestCaseGenerator.addTrade(secondTransactionDate, securityid2, 3);
        tradeTestCaseGenerator.addTrade(thirdTransactionDate, securityid1, 4);

        transactionService.setTradeTestCase(tradeTestCaseGenerator);

        var depotValueHandler = new DepotValueHandler(transactionService, valueCurveHandler);
        

        var price1 = new EndOfDayPrice(currency, security1, source, firstTransactionDate, 10.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price1);
        var price2 = new EndOfDayPrice(currency, security1, source, secondTransactionDate, 15.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price2);
        var price3 = new EndOfDayPrice(currency, security1, source, thirdTransactionDate, 12.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price3);

        var price4 = new EndOfDayPrice(currency, security2, source, firstTransactionDate, 20.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price4);
        var price5 = new EndOfDayPrice(currency, security2, source, secondTransactionDate, 30.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price5);
        var price6 = new EndOfDayPrice(currency, security2, source, thirdTransactionDate, 24.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price6);
        
        


        Map<Integer, TreeMap<LocalDate, Double>> calculatedPositionCurve = depotValueHandler.calcPositionsCurve(1);
        assertEquals(2, calculatedPositionCurve.size());
        assertEquals(2.0, calculatedPositionCurve.get(securityid1).get(firstTransactionDate));
        assertEquals(2.0, calculatedPositionCurve.get(securityid1).get(secondTransactionDate));
        assertEquals(6.0, calculatedPositionCurve.get(securityid1).get(thirdTransactionDate));

        assertEquals(0.0, calculatedPositionCurve.get(securityid2).get(firstTransactionDate));
        assertEquals(3.0, calculatedPositionCurve.get(securityid2).get(secondTransactionDate));
        assertEquals(3.0, calculatedPositionCurve.get(securityid2).get(thirdTransactionDate));

        Map<Integer, TreeMap<LocalDate, Double>> calculatedPositionValueCurve = depotValueHandler.calcPositionsValueCurve(calculatedPositionCurve);


        assertEquals(2, calculatedPositionValueCurve.size());
        assertEquals(20.0, calculatedPositionValueCurve.get(securityid1).get(firstTransactionDate));
        assertEquals(30.0, calculatedPositionValueCurve.get(securityid1).get(secondTransactionDate));
        assertEquals(72.0, calculatedPositionValueCurve.get(securityid1).get(thirdTransactionDate));

        assertEquals(90.0, calculatedPositionValueCurve.get(securityid2).get(secondTransactionDate));
        assertEquals(72.0, calculatedPositionValueCurve.get(securityid2).get(thirdTransactionDate));
    }

    @Test
    public void depotValueCalcTest(){
        initTest();
        LocalDate firstTransactionDate = LocalDate.parse("2020-01-01", formatter);
        LocalDate secondTransactionDate = LocalDate.parse("2020-01-03", formatter);
        LocalDate thirdTransactionDate = LocalDate.parse("2020-01-05", formatter);

        Instrument security1 = new Equity("sec1", "security1", true, ts);
        instrumentDao.saveInstrument(security1);
        int securityid1 = security1.getInstrumentid();
        Instrument security2 = new Equity("sec2", "security2", true, ts);
        instrumentDao.saveInstrument(security2);
        int securityid2 = security2.getInstrumentid();

        Instrument currency = new Currency("EUR", "Euro", true, ts);
        instrumentDao.saveInstrument(currency);
        var source = new Source("mocksource", true);

        
        TradeTestCaseGenerator tradeTestCaseGenerator = new TradeTestCaseGenerator();
        tradeTestCaseGenerator.addTrade(firstTransactionDate, securityid1, 2);
        tradeTestCaseGenerator.addTrade(secondTransactionDate, securityid2, 3);
        tradeTestCaseGenerator.addTrade(thirdTransactionDate, securityid1, 4);
        transactionService.setTradeTestCase(tradeTestCaseGenerator);

        var depotValueHandler = new DepotValueHandler(transactionService, valueCurveHandler);
        


        var price1 = new EndOfDayPrice(currency, security1, source, firstTransactionDate, 10.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price1);
        var price2 = new EndOfDayPrice(currency, security1, source, secondTransactionDate, 15.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price2);
        var price3 = new EndOfDayPrice(currency, security1, source, thirdTransactionDate, 12.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price3);

        var price4 = new EndOfDayPrice(currency, security2, source, firstTransactionDate, 20.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price4);
        var price5 = new EndOfDayPrice(currency, security2, source, secondTransactionDate, 30.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price5);
        var price6 = new EndOfDayPrice(currency, security2, source, thirdTransactionDate, 24.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price6);


        Depot depot = new Depot("depot", true, LocalDateTime.now());
        instrumentDao.saveInstrument(depot);
        TreeMap<LocalDate, Double> calculatedValueCurve = depotValueHandler.calcValueCurve(depot);

        assertEquals(20.0, calculatedValueCurve.get(firstTransactionDate));
        assertEquals(120.0, calculatedValueCurve.get(secondTransactionDate));
        assertEquals(144.0, calculatedValueCurve.get(thirdTransactionDate));
    }
}
