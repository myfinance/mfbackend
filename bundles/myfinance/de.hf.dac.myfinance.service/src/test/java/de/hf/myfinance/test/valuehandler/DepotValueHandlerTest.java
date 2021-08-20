package de.hf.myfinance.test.valuehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import de.hf.dac.myfinance.api.domain.Depot;
import de.hf.dac.myfinance.valuehandler.DepotValueHandler;
import de.hf.myfinance.test.mock.TransactionServiceMock;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;
import de.hf.myfinance.test.testcasegenerator.TradeTestCaseGenerator;

public class DepotValueHandlerTest {
    @Test
    public void positionBuildingTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate firstTransactionDate = LocalDate.parse("2020-01-01", formatter);
        LocalDate secondTransactionDate = LocalDate.parse("2020-01-03", formatter);
        LocalDate thirdTransactionDate = LocalDate.parse("2020-01-05", formatter);
        int securityid1 = 1;
        int securityid2 = 2;
        TradeTestCaseGenerator tradeTestCaseGenerator = new TradeTestCaseGenerator();
        tradeTestCaseGenerator.addTrade(firstTransactionDate, securityid1, 2);
        tradeTestCaseGenerator.addTrade(secondTransactionDate, securityid2, 3);
        tradeTestCaseGenerator.addTrade(thirdTransactionDate, securityid1, 4);

        var valueCurveService = new ValueCurveServiceMock();
        var transactionService = new TransactionServiceMock();
        transactionService.setTradeTestCase(tradeTestCaseGenerator);
        var depotValueHandler = new DepotValueHandler(transactionService, valueCurveService);


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
        assertEquals(20.0, calculatedPositionValueCurve.get(securityid1).get(LocalDate.parse("2020-01-01", formatter)));
        assertEquals(30.0, calculatedPositionValueCurve.get(securityid1).get(LocalDate.parse("2020-01-02", formatter)));
        assertEquals(30.0, calculatedPositionValueCurve.get(securityid1).get(LocalDate.parse("2020-01-03", formatter)));
        assertEquals(24.0, calculatedPositionValueCurve.get(securityid1).get(LocalDate.parse("2020-01-04", formatter)));
        assertEquals(72.0, calculatedPositionValueCurve.get(securityid1).get(LocalDate.parse("2020-01-05", formatter)));

        assertEquals(0.0, calculatedPositionValueCurve.get(securityid2).get(LocalDate.parse("2020-01-01", formatter)));
        assertEquals(0.0, calculatedPositionValueCurve.get(securityid2).get(LocalDate.parse("2020-01-02", formatter)));
        assertEquals(90.0, calculatedPositionValueCurve.get(securityid2).get(LocalDate.parse("2020-01-03", formatter)));
        assertEquals(72.0, calculatedPositionValueCurve.get(securityid2).get(LocalDate.parse("2020-01-04", formatter)));
        assertEquals(72.0, calculatedPositionValueCurve.get(securityid2).get(LocalDate.parse("2020-01-05", formatter)));
    }

    @Test
    public void depotValueCalcTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate firstTransactionDate = LocalDate.parse("2020-01-01", formatter);
        LocalDate secondTransactionDate = LocalDate.parse("2020-01-03", formatter);
        LocalDate thirdTransactionDate = LocalDate.parse("2020-01-05", formatter);
        int securityid1 = 1;
        int securityid2 = 2;
        TradeTestCaseGenerator tradeTestCaseGenerator = new TradeTestCaseGenerator();
        tradeTestCaseGenerator.addTrade(firstTransactionDate, securityid1, 2);
        tradeTestCaseGenerator.addTrade(secondTransactionDate, securityid2, 3);
        tradeTestCaseGenerator.addTrade(thirdTransactionDate, securityid1, 4);

        var valueCurveService = new ValueCurveServiceMock();
        var transactionService = new TransactionServiceMock();
        transactionService.setTradeTestCase(tradeTestCaseGenerator);
        var depotValueHandler = new DepotValueHandler(transactionService, valueCurveService);

        valueCurveService.setPositionValueCurve(depotValueHandler.calcPositionsValueCurve(depotValueHandler.calcPositionsCurve(1)));
        depotValueHandler = new DepotValueHandler(transactionService, valueCurveService);

        Depot depot = new Depot("depot", true, LocalDateTime.now());
        depot.setInstrumentid(1);
        TreeMap<LocalDate, Double> calculatedValueCurve = depotValueHandler.calcValueCurve(depot);

        assertEquals(20.0, calculatedValueCurve.get(LocalDate.parse("2020-01-01", formatter)));
        assertEquals(30.0, calculatedValueCurve.get(LocalDate.parse("2020-01-02", formatter)));
        assertEquals(120.0, calculatedValueCurve.get(LocalDate.parse("2020-01-03", formatter)));
        assertEquals(96.0, calculatedValueCurve.get(LocalDate.parse("2020-01-04", formatter)));
        assertEquals(144.0, calculatedValueCurve.get(LocalDate.parse("2020-01-05", formatter)));
    }
}
