package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.BudgetPortfolio;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BudgetPortfolioHandler extends AbsSimpleInstrumentHandler {
    
    public BudgetPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId) {
        super(instrumentDao, auditService, description, tenantId);
    }

    public BudgetPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument budgetPortfolio) {
        super(instrumentDao, auditService, budgetPortfolio);
    }

    @Override
    protected void createDomainObject(String description) {
        domainObject = new BudgetPortfolio(description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "BudgetPortfolio";
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.BUDGETPORTFOLIO;
    }
}