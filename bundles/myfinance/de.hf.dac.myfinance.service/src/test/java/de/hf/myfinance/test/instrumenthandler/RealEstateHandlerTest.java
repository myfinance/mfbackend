package de.hf.myfinance.test.instrumenthandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.BudgetHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.RealEstateHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;
import de.hf.myfinance.test.mock.RecurrentTransactionDaoMockImpl;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;

public class RealEstateHandlerTest {
    @Test
    public void saveInstrumentTest(){

        var instrumentDao = new InstrumentDaoMock();
        var auditService = new AuditServiceMockImpl();
        var valueService = new ValueCurveServiceMock();
        var recurrentTransactionDao = new RecurrentTransactionDaoMockImpl();

        var instrumentFactory = new InstrumentFactory(instrumentDao, auditService, valueService, recurrentTransactionDao);

        String tenantDesc = "testtenant";
        var tenantHandler = new TenantHandler(instrumentDao, auditService, instrumentFactory, tenantDesc);
        tenantHandler.save();
        var tenantId = tenantHandler.getInstrumentId();

        int budgetGroupId = 0;
        var instruments = instrumentDao.listInstruments();
        for (Instrument instrument : instruments) {
            if(instrument.getInstrumentType().equals(InstrumentType.BUDGETGROUP)){
                budgetGroupId = instrument.getInstrumentid();
            }
        }
        String budgetdesc = "testbudget";
        var budgetHandler = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, budgetdesc, budgetGroupId, null);
        budgetHandler.save();
        assertEquals(6, budgetHandler.getInstrumentId());

        String realestatedesc = "testRealEstate";
        List<ValuePerDate> yieldgoals = new ArrayList<ValuePerDate>();
        yieldgoals.add(new ValuePerDate("4.5,2020-01-01"));
        List<ValuePerDate> realEstateProfits = new ArrayList<ValuePerDate>();
        realEstateProfits.add(new ValuePerDate("2000,2020-01-01"));
        var realEstateHandler = (RealEstateHandler)instrumentFactory.getInstrumentHandler(InstrumentType.REALESTATE, realestatedesc, tenantId, realestatedesc);
        realEstateHandler.initAdditionalFields(budgetHandler.getInstrumentId(), yieldgoals, realEstateProfits);
        realEstateHandler.save();

        assertEquals(7, realEstateHandler.getInstrumentId());

        var tenant = instrumentFactory.getBaseInstrumentHandler(realEstateHandler.getInstrumentId()).getTenant();
        assertEquals(tenantId, tenant.get());
        //3 parents: realestate itself, accountpf and tenant
        assertEquals(3, instrumentFactory.getBaseInstrumentHandler(realEstateHandler.getInstrumentId()).getAncestorIds().size());
        assertEquals(1, instrumentFactory.getTenantHandler(tenantId, false).getAccounts().size());
        var properties = instrumentFactory.getBaseInstrumentHandler(realEstateHandler.getInstrumentId()).getInstrumentProperties();
        assertEquals(3, properties.size());


        // update

        var newrealEstateHandler = instrumentFactory.getRealEstateHandler(realEstateHandler.getInstrumentId(), true);
        List<ValuePerDate> newyieldgoals = new ArrayList<ValuePerDate>();
        newyieldgoals.add(new ValuePerDate("4.5,2020-01-01"));
        newyieldgoals.add(new ValuePerDate("5.5,2020-01-02"));
        List<ValuePerDate> newrealEstateProfits = new ArrayList<ValuePerDate>();
        newrealEstateProfits.add(new ValuePerDate("2001,2020-01-01"));
        newrealEstateProfits.add(new ValuePerDate("2010,2020-01-02"));
        newrealEstateHandler.initAdditionalFields(newyieldgoals, newrealEstateProfits);
        newrealEstateHandler.setActive(true);
        newrealEstateHandler.setDescription("a new desc");
        newrealEstateHandler.save();




        var updatedrealestateHandler = instrumentFactory.getRealEstateHandler(realEstateHandler.getInstrumentId(), true);
        var updatedproperties = updatedrealestateHandler.getInstrumentProperties();
        assertEquals(5, updatedproperties.size());
        assertEquals(8, updatedrealestateHandler.getBudgetGroupId());
        assertEquals("a new desc", updatedrealestateHandler.getInstrument().getDescription());
        assertEquals("anewdesc", updatedrealestateHandler.getInstrument().getBusinesskey());
        assertEquals(true, updatedrealestateHandler.getInstrument().isIsactive());
    }
}