package de.hf.myfinance.test.instrumenthandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.BudgetHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;
import de.hf.myfinance.test.mock.RecurrentTransactionDaoMockImpl;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;
import de.hf.dac.myfinance.api.exceptions.MFException;

public class BudgetHandlerTest {
    @Test
    public void saveInstrumentTest(){

        var instrumentDao = new InstrumentDaoMock();
        var auditService = new AuditServiceMockImpl();
        var valueService = new ValueCurveServiceMock();
        valueService.setReturnsValue(false);
        var recurrentTransactionDao = new RecurrentTransactionDaoMockImpl();

        var instrumentFactory = new InstrumentFactory(instrumentDao, auditService, valueService, recurrentTransactionDao);

        String tenantDesc = "testtenant";
        var tenantHandler = new TenantHandler(instrumentDao, auditService, instrumentFactory, tenantDesc);
        tenantHandler.save();
        
        var tenantId = tenantHandler.getInstrumentId();
        assertEquals(1, tenantId);
        var instruments = instrumentDao.listInstruments();
        //Tenant, AccountPF, BudgetPF, BudgetGroup and IncomeBudget should be created
        assertEquals(5, instrumentDao.listInstruments().size());
        int budgetGroupId = 0;
        for (Instrument instrument : instruments) {
            if(instrument.getInstrumentType().equals(InstrumentType.BUDGETGROUP)){
                budgetGroupId = instrument.getInstrumentid();
            }
        }
        String desc = "testbudget";
        var budgetHandler = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, desc, budgetGroupId, null);
        budgetHandler.save();
        
        assertEquals(6, budgetHandler.getInstrumentId());
        assertEquals(desc, budgetHandler.getInstrument().getBusinesskey());

        var tenant = instrumentFactory.getBaseInstrumentHandler(budgetHandler.getInstrumentId()).getTenant();
        assertEquals(tenantId, tenant.get());
        //4 parents: budget itself, budgetgroup, budgetpf and tenant
        assertEquals(4, instrumentFactory.getBaseInstrumentHandler(budgetHandler.getInstrumentId()).getAncestorIds().size());

        var newbudgetHandler = instrumentFactory.getInstrumentHandler(budgetHandler.getInstrumentId());
        newbudgetHandler.setActive(true);
        newbudgetHandler.setDescription("a new desc");
        newbudgetHandler.save();


        var updatedBudgetHandler = instrumentFactory.getInstrumentHandler(budgetHandler.getInstrumentId());
        assertEquals("a new desc", updatedBudgetHandler.getInstrument().getDescription());
        assertEquals("anewdesc", updatedBudgetHandler.getInstrument().getBusinesskey());
        assertEquals(true, updatedBudgetHandler.getInstrument().isIsactive());

        updatedBudgetHandler.setActive(false);
        updatedBudgetHandler.save();

        updatedBudgetHandler = instrumentFactory.getInstrumentHandler(budgetHandler.getInstrumentId());
        assertEquals(false, updatedBudgetHandler.getInstrument().isIsactive());
    }

    @Test
    public void inactivationFailureTest(){

        var instrumentDao = new InstrumentDaoMock();
        var auditService = new AuditServiceMockImpl();
        var valueService = new ValueCurveServiceMock();

        var recurrentTransactionDao = new RecurrentTransactionDaoMockImpl();

        var instrumentFactory = new InstrumentFactory(instrumentDao, auditService, valueService, recurrentTransactionDao);

        String tenantDesc = "testtenant";
        var tenantHandler = new TenantHandler(instrumentDao, auditService, instrumentFactory, tenantDesc);
        tenantHandler.save();
        
        var tenantId = tenantHandler.getInstrumentId();
        assertEquals(1, tenantId);
        var instruments = instrumentDao.listInstruments();
        //Tenant, AccountPF, BudgetPF, BudgetGroup and IncomeBudget should be created
        assertEquals(5, instrumentDao.listInstruments().size());
        int budgetGroupId = 0;
        for (Instrument instrument : instruments) {
            if(instrument.getInstrumentType().equals(InstrumentType.BUDGETGROUP)){
                budgetGroupId = instrument.getInstrumentid();
            }
        }
        String desc = "testbudget";
        var budgetHandler = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, desc, budgetGroupId, null);
        budgetHandler.save();
        
        assertEquals(6, budgetHandler.getInstrumentId());
        assertEquals(desc, budgetHandler.getInstrument().getBusinesskey());

        var tenant = instrumentFactory.getBaseInstrumentHandler(budgetHandler.getInstrumentId()).getTenant();
        assertEquals(tenantId, tenant.get());
        //4 parents: budget itself, budgetgroup, budgetpf and tenant
        assertEquals(4, instrumentFactory.getBaseInstrumentHandler(budgetHandler.getInstrumentId()).getAncestorIds().size());

        var newbudgetHandler = instrumentFactory.getInstrumentHandler(budgetHandler.getInstrumentId());
        newbudgetHandler.setActive(true);
        newbudgetHandler.setDescription("a new desc");
        newbudgetHandler.save();


        var updatedBudgetHandler = instrumentFactory.getInstrumentHandler(budgetHandler.getInstrumentId());
        assertEquals("a new desc", updatedBudgetHandler.getInstrument().getDescription());
        assertEquals("anewdesc", updatedBudgetHandler.getInstrument().getBusinesskey());
        assertEquals(true, updatedBudgetHandler.getInstrument().isIsactive());

        updatedBudgetHandler.setActive(false);
        assertThrows(MFException.class, () -> {
            updatedBudgetHandler.save();
        });
    }
}