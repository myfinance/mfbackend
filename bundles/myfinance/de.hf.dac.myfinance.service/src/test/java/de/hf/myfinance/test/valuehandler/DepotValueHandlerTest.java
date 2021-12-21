package de.hf.myfinance.test.valuehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Source;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.BudgetHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.DepotHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.GiroHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.dac.myfinance.instrumenthandler.securityhandler.CurrencyHandler;
import de.hf.dac.myfinance.instrumenthandler.securityhandler.EquityHandler;
import de.hf.dac.myfinance.valuehandler.BudgetValueHandler;
import de.hf.dac.myfinance.valuehandler.DepotValueHandler;
import de.hf.myfinance.test.AbsTest;

public class DepotValueHandlerTest extends AbsTest{
    @Test
    public void positionBuildingTest(){
        initTest();

        var tenantHandler = new TenantHandler(instrumentDao, auditService, instrumentFactory, "tenant");
        tenantHandler.save();
        var tenantId = tenantHandler.getInstrumentId();
        //Tenant, AccountPF, BudgetPF, BudgetGroup and IncomeBudget should be created
        assertEquals(5, instrumentDao.listInstruments().size());
        int budgetGroupId = 0;
        var instruments = instrumentDao.listInstruments();
        for (Instrument instrument : instruments) {
            if(instrument.getInstrumentType().equals(InstrumentType.BUDGETGROUP)){
                budgetGroupId = instrument.getInstrumentid();
            }
        }
        var giroHandler = new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, "testgiro", tenantId, "testgiro");
        giroHandler.save();
        var budgetHandler = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, "testbudget", budgetGroupId, "testbudget");
        budgetHandler.save();
        var depotHandler = new DepotHandler(instrumentDao, auditService, "testdepot", tenantId, "testdepot");
        depotHandler.initAdditionalFields(giroHandler.getInstrumentId(), budgetHandler.getInstrumentId());
        depotHandler.save();

        LocalDate firstTransactionDate = LocalDate.parse("2020-01-01", formatter);
        LocalDate secondTransactionDate = LocalDate.parse("2020-01-03", formatter);
        LocalDate thirdTransactionDate = LocalDate.parse("2020-01-05", formatter);


        var eq1Handler = new EquityHandler(instrumentDao, auditService, instrumentFactory, "sec1", "security1");
        eq1Handler.save();
        var eq2Handler = new EquityHandler(instrumentDao, auditService, instrumentFactory, "sec2", "security2");
        eq2Handler.save();
        var fxHandler = new CurrencyHandler(instrumentDao, auditService, instrumentFactory, "EUR", "Euro");
        fxHandler.save();  

        transactionService.newTrade("atrade1", depotHandler.getInstrumentId(), eq1Handler.getInstrument().getBusinesskey(), 2, giroHandler.getInstrumentId(), budgetHandler.getInstrumentId(), -100, firstTransactionDate, ts);
        transactionService.newTrade("atrade2", depotHandler.getInstrumentId(), eq2Handler.getInstrument().getBusinesskey(), 3, giroHandler.getInstrumentId(), budgetHandler.getInstrumentId(), -100, secondTransactionDate, ts);
        transactionService.newTrade("atrade3", depotHandler.getInstrumentId(), eq1Handler.getInstrument().getBusinesskey(), 4, giroHandler.getInstrumentId(), budgetHandler.getInstrumentId(), -100, thirdTransactionDate, ts);

        var depotValueHandler = new DepotValueHandler(transactionService, valueCurveHandler);
        

        var source = new Source("mocksource", true);
        var price1 = new EndOfDayPrice(fxHandler.getInstrument(), eq1Handler.getInstrument(), source, firstTransactionDate, 10.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price1);
        var price2 = new EndOfDayPrice(fxHandler.getInstrument(), eq1Handler.getInstrument(), source, secondTransactionDate, 15.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price2);
        var price3 = new EndOfDayPrice(fxHandler.getInstrument(), eq1Handler.getInstrument(), source, thirdTransactionDate, 12.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price3);

        var price4 = new EndOfDayPrice(fxHandler.getInstrument(), eq2Handler.getInstrument(), source, firstTransactionDate, 20.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price4);
        var price5 = new EndOfDayPrice(fxHandler.getInstrument(), eq2Handler.getInstrument(), source, secondTransactionDate, 30.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price5);
        var price6 = new EndOfDayPrice(fxHandler.getInstrument(), eq2Handler.getInstrument(), source, thirdTransactionDate, 24.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price6);
        
        


        Map<Integer, TreeMap<LocalDate, Double>> calculatedPositionCurve = depotValueHandler.calcPositionsCurve(1);
        assertEquals(2, calculatedPositionCurve.size());
        assertEquals(2.0, calculatedPositionCurve.get(eq1Handler.getInstrumentId()).get(firstTransactionDate));
        assertEquals(2.0, calculatedPositionCurve.get(eq1Handler.getInstrumentId()).get(secondTransactionDate));
        assertEquals(6.0, calculatedPositionCurve.get(eq1Handler.getInstrumentId()).get(thirdTransactionDate));

        assertEquals(0.0, calculatedPositionCurve.get(eq2Handler.getInstrumentId()).get(firstTransactionDate));
        assertEquals(3.0, calculatedPositionCurve.get(eq2Handler.getInstrumentId()).get(secondTransactionDate));
        assertEquals(3.0, calculatedPositionCurve.get(eq2Handler.getInstrumentId()).get(thirdTransactionDate));

        Map<Integer, TreeMap<LocalDate, Double>> calculatedPositionValueCurve = depotValueHandler.calcPositionsValueCurve(calculatedPositionCurve);


        assertEquals(2, calculatedPositionValueCurve.size());
        assertEquals(20.0, calculatedPositionValueCurve.get(eq1Handler.getInstrumentId()).get(firstTransactionDate));
        assertEquals(30.0, calculatedPositionValueCurve.get(eq1Handler.getInstrumentId()).get(secondTransactionDate));
        assertEquals(72.0, calculatedPositionValueCurve.get(eq1Handler.getInstrumentId()).get(thirdTransactionDate));

        assertEquals(90.0, calculatedPositionValueCurve.get(eq2Handler.getInstrumentId()).get(secondTransactionDate));
        assertEquals(72.0, calculatedPositionValueCurve.get(eq2Handler.getInstrumentId()).get(thirdTransactionDate));
    }

    @Test
    public void depotValueCalcTest(){
        initTest();
        
        var tenantHandler = new TenantHandler(instrumentDao, auditService, instrumentFactory, "tenant");
        tenantHandler.save();
        var tenantId = tenantHandler.getInstrumentId();
        //Tenant, AccountPF, BudgetPF, BudgetGroup and IncomeBudget should be created
        assertEquals(5, instrumentDao.listInstruments().size());
        int budgetGroupId = 0;
        var instruments = instrumentDao.listInstruments();
        for (Instrument instrument : instruments) {
            if(instrument.getInstrumentType().equals(InstrumentType.BUDGETGROUP)){
                budgetGroupId = instrument.getInstrumentid();
            }
        }
        var giroHandler = new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, "testgiro", tenantId, "testgiro");
        giroHandler.save();
        var budgetHandler = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, "testbudget", budgetGroupId, "testbudget");
        budgetHandler.save();
        var depotHandler = new DepotHandler(instrumentDao, auditService, "testdepot", tenantId, "testdepot");
        depotHandler.initAdditionalFields(giroHandler.getInstrumentId(), budgetHandler.getInstrumentId());
        depotHandler.save();

        LocalDate firstTransactionDate = LocalDate.parse("2020-01-01", formatter);
        LocalDate secondTransactionDate = LocalDate.parse("2020-01-03", formatter);
        LocalDate thirdTransactionDate = LocalDate.parse("2020-01-05", formatter);


        var eq1Handler = new EquityHandler(instrumentDao, auditService, instrumentFactory, "sec1", "security1");
        eq1Handler.save();
        var eq2Handler = new EquityHandler(instrumentDao, auditService, instrumentFactory, "sec2", "security2");
        eq2Handler.save();
        var fxHandler = new CurrencyHandler(instrumentDao, auditService, instrumentFactory, "EUR", "Euro");
        fxHandler.save();  

        transactionService.newTrade("atrade1", depotHandler.getInstrumentId(), eq1Handler.getInstrument().getBusinesskey(), 2, giroHandler.getInstrumentId(), budgetHandler.getInstrumentId(), -100, firstTransactionDate, ts);
        transactionService.newTrade("atrade2", depotHandler.getInstrumentId(), eq2Handler.getInstrument().getBusinesskey(), 3, giroHandler.getInstrumentId(), budgetHandler.getInstrumentId(), -100, secondTransactionDate, ts);
        transactionService.newTrade("atrade3", depotHandler.getInstrumentId(), eq1Handler.getInstrument().getBusinesskey(), 4, giroHandler.getInstrumentId(), budgetHandler.getInstrumentId(), -100, thirdTransactionDate, ts);

        
        var depotValueHandler = new DepotValueHandler(transactionService, valueCurveHandler);
        

        var source = new Source("mocksource", true);
        var price1 = new EndOfDayPrice(fxHandler.getInstrument(), eq1Handler.getInstrument(), source, firstTransactionDate, 10.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price1);
        var price2 = new EndOfDayPrice(fxHandler.getInstrument(), eq1Handler.getInstrument(), source, secondTransactionDate, 15.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price2);
        var price3 = new EndOfDayPrice(fxHandler.getInstrument(), eq1Handler.getInstrument(), source, thirdTransactionDate, 12.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price3);

        var price4 = new EndOfDayPrice(fxHandler.getInstrument(), eq2Handler.getInstrument(), source, firstTransactionDate, 20.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price4);
        var price5 = new EndOfDayPrice(fxHandler.getInstrument(), eq2Handler.getInstrument(), source, secondTransactionDate, 30.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price5);
        var price6 = new EndOfDayPrice(fxHandler.getInstrument(), eq2Handler.getInstrument(), source, thirdTransactionDate, 24.0, ts);
        endOfDayPriceDao.saveEndOfDayPrice(price6);


 
        TreeMap<LocalDate, Double> calculatedValueCurve = depotValueHandler.calcValueCurve(depotHandler.getInstrument());

        assertEquals(20.0, calculatedValueCurve.get(firstTransactionDate));
        assertEquals(120.0, calculatedValueCurve.get(secondTransactionDate));
        assertEquals(144.0, calculatedValueCurve.get(thirdTransactionDate));

        var budgetValueHandler = new BudgetValueHandler(transactionService, instrumentService, valueCurveHandler);
        TreeMap<LocalDate, Double> calculatedBudgetValueCurve = budgetValueHandler.calcValueCurve(budgetHandler.getSavedDomainObject().get());

        assertEquals(-80.0, calculatedBudgetValueCurve.get(firstTransactionDate)); // -100 for the trade atrade1, 20 for the depotvalue
        assertEquals(-80.0, calculatedBudgetValueCurve.get(secondTransactionDate)); // -200 for the trade atrade1 and atrade2, 120 for the depotvalue
        assertEquals(-156.0, calculatedBudgetValueCurve.get(thirdTransactionDate)); // -300 for the trade atrade1, atrade2 and atrade3, 144 for the depotvalue
    }
}
