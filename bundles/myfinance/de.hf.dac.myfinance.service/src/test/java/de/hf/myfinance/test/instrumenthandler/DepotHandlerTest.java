package de.hf.myfinance.test.instrumenthandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.BudgetHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.DepotHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.GiroHandler;
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
        assertEquals("DEFAULTGIROID", properties.get(0).getPropertyname());
        assertEquals("7", properties.get(0).getValue());


        // update
        var newgiroHandler = new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, girodesc, tenantId, "newtestgiro");
        newgiroHandler.save();
        assertEquals(9, newgiroHandler.getInstrumentId());

        var newdepotHandler = (DepotHandler)instrumentFactory.getDepotHandler(depotHandler.getInstrumentId(), true);
        newdepotHandler.setDefaultGiroId(newgiroHandler.getInstrumentId());
        newdepotHandler.setActive(true);
        newdepotHandler.setDescription("a new desc");
        newdepotHandler.save();


        var updateddepotHandler = (DepotHandler)instrumentFactory.getDepotHandler(depotHandler.getInstrumentId(), true);
        var updatedproperties = updateddepotHandler.getInstrumentProperties();
        assertEquals(1, updatedproperties.size());
        assertEquals("DEFAULTGIROID", updatedproperties.get(0).getPropertyname());
        assertEquals("9", updatedproperties.get(0).getValue());
        assertEquals("a new desc", updateddepotHandler.getInstrument().getDescription());
        assertEquals("anewdesc", updateddepotHandler.getInstrument().getBusinesskey());
        assertEquals(true, updateddepotHandler.getInstrument().isIsactive());

        /*newdepotHandler.setActive(false);
        newdepotHandler.save();

        updateddepotHandler = (DepotHandler)instrumentFactory.getDepotHandler(depotHandler.getInstrumentId(), true);
        assertEquals(false, updateddepotHandler.getInstrument().isIsactive());*/
    }
}