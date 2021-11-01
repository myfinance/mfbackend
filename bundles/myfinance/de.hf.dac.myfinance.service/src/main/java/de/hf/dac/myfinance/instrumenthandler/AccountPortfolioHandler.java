package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.AccountPortfolio;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class AccountPortfolioHandler extends AbsAccountableInstrumentHandler implements AccountableInstrumentHandler {

    public AccountPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, String description,
            int tenantId) {
        super(instrumentDao, auditService, description, tenantId);
    }

    public AccountPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument accountPortfolio) {
        super(instrumentDao, auditService, accountPortfolio);
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
    protected InstrumentType getInstrumentType() {
        return InstrumentType.ACCOUNTPORTFOLIO;
    }
}