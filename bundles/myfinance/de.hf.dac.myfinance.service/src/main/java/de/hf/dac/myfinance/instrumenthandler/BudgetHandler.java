package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Budget;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BudgetHandler extends AbsSimpleInstrumentHandler {

    public BudgetHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int budgetGroupId) {
        super(instrumentDao, auditService, description, budgetGroupId);
    }

    public BudgetHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument budget) {
        super(instrumentDao, auditService, budget);
    }

    @Override
    protected void createDomainObject(String description) {
        domainObject = new Budget(description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "Budget";
    }

    @Override
    protected InstrumentType getParentType() {
        return InstrumentType.BUDGETGROUP;
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.BUDGET;
    }
}