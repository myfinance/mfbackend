package de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.BudgetPortfolio;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BudgetPortfolioHandler extends AbsAccountableInstrumentHandler {
    
    public BudgetPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId, String businesskey) {
        super(instrumentDao, auditService, description, tenantId, businesskey);
    }

    public BudgetPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument budgetPortfolio) {
        super(instrumentDao, auditService, budgetPortfolio);
    }

    public BudgetPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
    }

    @Override
    protected void createDomainObject() {
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