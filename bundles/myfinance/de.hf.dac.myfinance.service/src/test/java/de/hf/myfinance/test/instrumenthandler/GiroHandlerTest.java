package de.hf.myfinance.test.instrumenthandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.GiroHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;
import de.hf.myfinance.test.mock.RecurrentTransactionDaoMockImpl;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;

public class GiroHandlerTest {
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
        assertEquals(1, tenantId);
        //Tenant, AccountPF, BudgetPF, BudgetGroup and IncomeBudget should be created
        assertEquals(5, instrumentDao.listInstruments().size());

        String desc = "testgiro";
        var giroHandler = new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, desc, tenantId, desc);
        giroHandler.save();
        
        assertEquals(6, giroHandler.getInstrumentId());

        var tenant = instrumentFactory.getBaseInstrumentHandler(giroHandler.getInstrumentId()).getTenant();
        assertEquals(tenantId, tenant.get());
        //3 parents: giro itself, accountpf and tenant
        assertEquals(3, instrumentFactory.getBaseInstrumentHandler(giroHandler.getInstrumentId()).getAncestorIds().size());

        assertEquals(1, instrumentFactory.getTenantHandler(tenantId, false).getAccounts().size());
    }
}