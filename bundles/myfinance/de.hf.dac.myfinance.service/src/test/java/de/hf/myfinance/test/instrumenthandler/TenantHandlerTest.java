package de.hf.myfinance.test.instrumenthandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;
import de.hf.myfinance.test.mock.RecurrentTransactionDaoMockImpl;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;

public class TenantHandlerTest {
    @Test
    public void saveInstrumentTest(){

        var instrumentDao = new InstrumentDaoMock();
        var auditService = new AuditServiceMockImpl();
        var valueService = new ValueCurveServiceMock();
        var recurrentTransactionDao = new RecurrentTransactionDaoMockImpl();

        var instrumentFactory = new InstrumentFactory(instrumentDao, auditService, valueService, recurrentTransactionDao);

        String tenantDesc = "testtenantxxxxxxxxxxxxxxxxxxxxxxxxxx";
        var tenantHandler = new TenantHandler(instrumentDao, auditService, instrumentFactory, tenantDesc);
        tenantHandler.save();
        
        var tenantId = tenantHandler.getInstrumentId();
        assertEquals(1, tenantId);
        var instruments = instrumentDao.listInstruments();
        //Tenant, AccountPF, BudgetPF, BudgetGroup and IncomeBudget should be created
        assertEquals(5, instruments.size());
        for (Instrument instrument : instruments) {
            assertTrue(instrument.getBusinesskey().length()<32);
            switch(instrument.getInstrumentType()){
                case TENANT: 
                    assertEquals("testte", instrument.getBusinesskey());   
                    break;
                case BUDGETPORTFOLIO: 
                    assertEquals("budgetPf_testte", instrument.getBusinesskey());  
                    break;   
                case ACCOUNTPORTFOLIO: 
                    assertEquals("accountPf_testte", instrument.getBusinesskey());  
                    break;                                     
                case BUDGETGROUP: 
                    assertEquals("budgetGroup_testte", instrument.getBusinesskey());  
                    break;   
                case BUDGET: 
                    assertEquals("incomeBudget_budgetGroup_testte", instrument.getBusinesskey());  
                    break;                                                              
                default:
                    assertEquals(0,1);
                    break;
            }
        }

        var tenantchilds = instrumentDao.getInstrumentChilds(tenantId,EdgeType.TENANTGRAPH, 1);
        assertEquals(2, tenantchilds.size());
        boolean hasBudgetPortfolio = false;
        boolean hasAccountPortfolio = false;
        for (Instrument instrument : tenantchilds) {
            switch(instrument.getInstrumentType()){
                case BUDGETPORTFOLIO: 
                    assertEquals("budgetPf_testte", instrument.getBusinesskey());  
                    hasBudgetPortfolio = true;
                    var budgetPfChilds = instrumentDao.getInstrumentChilds(instrument.getInstrumentid(),EdgeType.TENANTGRAPH, 1);
                    assertEquals(1, budgetPfChilds.size());
                    var budgetgroup = budgetPfChilds.get(0);
                    var budgetGroupChilds = instrumentDao.getInstrumentChilds(budgetgroup.getInstrumentid(),EdgeType.TENANTGRAPH, 1);
                    assertEquals(1, budgetGroupChilds.size());
                    var budget = budgetGroupChilds.get(0);
                    assertEquals(InstrumentType.BUDGET, budget.getInstrumentType());
                    assertEquals(budget.getInstrumentid(), instrumentFactory.getBudgetGroupHandler(budgetgroup.getInstrumentid(), false).getIncomeBudget().getInstrumentid());
                    break;   
                case ACCOUNTPORTFOLIO: 
                    assertEquals("accountPf_testte", instrument.getBusinesskey());  
                    hasAccountPortfolio = true;
                    break;                                                                     
                default:
                    assertEquals(0,1);
                    break;
            }
        }
        assertTrue(hasBudgetPortfolio && hasAccountPortfolio);
        
    }
}