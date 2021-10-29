package de.hf.myfinance.test.instrumentgraphhandler;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.api.domain.AccountPortfolio;
import de.hf.dac.myfinance.api.domain.Budget;
import de.hf.dac.myfinance.api.domain.BudgetPortfolio;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.Tenant;
import de.hf.dac.myfinance.instrumenthandler.InstrumentGraphHandlerImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;

public class InstrumentGraphHandlerTest {
    @Test
    public void paddInstrumentToGraphTest(){

        Instrument tenant = new Tenant("testtenant", true, LocalDateTime.now());
        tenant.setInstrumentid(1);
        Instrument accpf = new AccountPortfolio("accountpf", true, LocalDateTime.now());
        accpf.setInstrumentid(2);
        Instrument giro = new Giro("testgiro", true, LocalDateTime.now());
        giro.setInstrumentid(3);
        Instrument budgetpf = new BudgetPortfolio("budgetpf", true, LocalDateTime.now());
        budgetpf.setInstrumentid(4);
        Instrument budget = new Budget("testbudget", true, LocalDateTime.now());
        budget.setInstrumentid(5);

        var instrumentDao = new InstrumentDaoMock();
        instrumentDao.saveInstrument(tenant);
        instrumentDao.saveInstrument(accpf);
        instrumentDao.saveInstrument(giro);
        instrumentDao.saveInstrument(budgetpf);
        instrumentDao.saveInstrument(budget);

        var instrumentGraphHandler = new InstrumentGraphHandlerImpl(instrumentDao);
        instrumentGraphHandler.addInstrumentToGraph(accpf.getInstrumentid(), tenant.getInstrumentid());
        instrumentGraphHandler.addInstrumentToGraph(budgetpf.getInstrumentid(), tenant.getInstrumentid());
        instrumentGraphHandler.addInstrumentToGraph(giro.getInstrumentid(), accpf.getInstrumentid());
        instrumentGraphHandler.addInstrumentToGraph(budget.getInstrumentid(), budgetpf.getInstrumentid());

        assertEquals(4, instrumentGraphHandler.getAllInstrumentChilds(tenant.getInstrumentid()).size());
    }
}