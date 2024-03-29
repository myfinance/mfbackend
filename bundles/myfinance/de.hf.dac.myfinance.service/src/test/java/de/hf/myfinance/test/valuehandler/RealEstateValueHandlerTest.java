package de.hf.myfinance.test.valuehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.BudgetHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.RealEstateHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.dac.myfinance.valuehandler.BudgetValueHandler;
import de.hf.dac.myfinance.valuehandler.RealEstateValueHandler;
import de.hf.myfinance.test.AbsTest;

public class RealEstateValueHandlerTest extends AbsTest{
    @Test
    public void realestateValueCalcTest(){
        initTest();

        String tenantDesc = "testtenant";
        var tenantHandler = new TenantHandler(instrumentDao, auditService, instrumentFactory, tenantDesc);
        tenantHandler.save();
        var tenantId = tenantHandler.getInstrumentId();
        var instruments = instrumentDao.listInstruments();
        //Tenant, AccountPF, BudgetPF, BudgetGroup and IncomeBudget should be created
        assertEquals(5, instrumentDao.listInstruments().size());
        int budgetGroupId = 0;
        for (Instrument instrument : instruments) {
            if(instrument.getInstrumentType().equals(InstrumentType.BUDGETGROUP)){
                budgetGroupId = instrument.getInstrumentid();
            }
        }

        String budgetDesc = "testvaluebudget";
        var budgetHandler = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, budgetDesc, budgetGroupId, budgetDesc);
        budgetHandler.save();
        var valueBudgetId = budgetHandler.getInstrumentId();

        List<ValuePerDate> yieldgoals = new ArrayList<ValuePerDate>();
        // I want 10% for my investment
        yieldgoals.add(new ValuePerDate("0.1,2020-01-01"));
        List<ValuePerDate> realEstateProfits = new ArrayList<ValuePerDate>();
        // a profit of 1000 per month
        realEstateProfits.add(new ValuePerDate("1000,2020-01-01"));

        String realestatedesc = "testrealestate";
        var realEstateHandler = (RealEstateHandler)instrumentFactory.getInstrumentHandler(InstrumentType.REALESTATE, realestatedesc, tenantId, realestatedesc);
        realEstateHandler.initAdditionalFields(valueBudgetId, yieldgoals, realEstateProfits);
        realEstateHandler.save();
        
        var realEstateValueHandler = new RealEstateValueHandler(instrumentService, valueCurveHandler);


        TreeMap<LocalDate, Double> calculatedValueCurve = realEstateValueHandler.calcValueCurve(realEstateHandler.getSavedDomainObject().get());

        assertEquals(120000.0, calculatedValueCurve.get(LocalDate.parse("2020-01-01", formatter)));

        var budgetValueHandler = new BudgetValueHandler(transactionService, instrumentService, valueCurveHandler);

        TreeMap<LocalDate, Double> calculatedBudgetValueCurve = budgetValueHandler.calcValueCurve(budgetHandler.getSavedDomainObject().get());

        assertEquals(120000.0, calculatedBudgetValueCurve.get(LocalDate.parse("2020-01-01", formatter)));
    }
}