package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.AccountPortfolio;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class AccountPortfolioHandler extends AbsSimpleInstrumentHandler {

    public AccountPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, String description,
            int tenantId) {
        super(instrumentDao, auditService, description, tenantId);
    }

    @Override
    protected void createDomainObject(String description) {
        domainObject = new AccountPortfolio(description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "AccountPortfolio";
    }

    @Override
    protected InstrumentType getParentType() {
        return InstrumentType.TENANT;
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.ACCOUNTPORTFOLIO;
    }
}