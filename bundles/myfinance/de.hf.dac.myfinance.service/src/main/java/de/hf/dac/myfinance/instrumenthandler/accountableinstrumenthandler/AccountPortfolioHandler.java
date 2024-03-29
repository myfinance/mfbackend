package de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.AccountPortfolio;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class AccountPortfolioHandler extends AbsAccountableInstrumentHandler {

    public AccountPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, String description,
            int tenantId, String businesskey) {
        super(instrumentDao, auditService, description, tenantId, businesskey);
    }

    public AccountPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument accountPortfolio) {
        super(instrumentDao, auditService, accountPortfolio);
    }

    public AccountPortfolioHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
    }

    @Override
    protected void createDomainObject() {
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