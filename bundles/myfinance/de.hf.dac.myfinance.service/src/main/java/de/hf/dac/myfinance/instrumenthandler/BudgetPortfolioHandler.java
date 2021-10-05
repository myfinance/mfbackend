package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.BudgetPortfolio;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BudgetPortfolioHandler extends AbsInstrumentHandler {

    protected BudgetPortfolio budgetPortfolio;
    private int tenantId;
    private static final String AUDIT_MSG_TYPE="BudgetPortfolioHandler_User_Event";

    
    public BudgetPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId) {
        super(instrumentDao, auditService);
        budgetPortfolio = new BudgetPortfolio(description, true, ts);
        this.tenantId = tenantId;
    }

    
    @Override
    public void save() {
        instrumentDao.saveInstrument(budgetPortfolio);
        instrumentId = budgetPortfolio.getInstrumentid();
        instrumentGraphHandler.addInstrumentToGraph(instrumentId, tenantId);
        auditService.saveMessage("budgetPortfolio inserted:" + budgetPortfolio.getDescription(), Severity.INFO, AUDIT_MSG_TYPE);
    }

}