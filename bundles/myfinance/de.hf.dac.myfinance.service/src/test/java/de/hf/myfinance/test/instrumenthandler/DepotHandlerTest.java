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
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.DepotHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.GiroHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.RealEstateHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;
import de.hf.myfinance.test.mock.RecurrentTransactionDaoMockImpl;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;

public class DepotHandlerTest {
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

        String girodesc = "testgiro";
        var giroHandler = new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, girodesc, tenantId, girodesc);
        giroHandler.save();
        assertEquals(7, giroHandler.getInstrumentId());


        String depotDesc = "testdepot";
        DepotHandler depotHandler = (DepotHandler)instrumentFactory.getInstrumentHandler(InstrumentType.DEPOT, depotDesc, tenantId, depotDesc);
        depotHandler.initAdditionalFields(giroHandler.getInstrumentId(), budgetHandler.getInstrumentId());
        depotHandler.save();

        assertEquals(8, depotHandler.getInstrumentId());

        var tenant = instrumentFactory.getBaseInstrumentHandler(depotHandler.getInstrumentId()).getTenant();
        assertEquals(tenantId, tenant.get());
        //3 parents: depot itself, accountpf and tenant
        assertEquals(3, instrumentFactory.getBaseInstrumentHandler(depotHandler.getInstrumentId()).getAncestorIds().size());
        assertEquals(2, instrumentFactory.getTenantHandler(tenantId, false).getAccounts().size());
        var properties = instrumentFactory.getBaseInstrumentHandler(depotHandler.getInstrumentId()).getInstrumentProperties();
        assertEquals(1, properties.size());
    }
}