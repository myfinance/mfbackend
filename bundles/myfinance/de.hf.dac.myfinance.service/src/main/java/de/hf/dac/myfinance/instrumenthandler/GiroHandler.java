package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.BudgetPortfolio;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class GiroHandler  extends AbsSimpleInstrumentHandler {
    
    public GiroHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId) {
        super(instrumentDao, auditService, description, tenantId);
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
    protected InstrumentType getParentType() {
        return InstrumentType.TENANT;
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.GIRO;
    }

    @Override
    protected void setParent(int parentId) {
        this.parentId = parentId;
    }
}