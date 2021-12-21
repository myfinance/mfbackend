package de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Budget;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.service.ValueService;

public class BudgetHandler extends AbsCashInstrumentHandler {

    public BudgetHandler(InstrumentDao instrumentDao, AuditService auditService, ValueService valueService, RecurrentTransactionDao recurrentTransactionDao, String description, int budgetGroupId, String businesskey) {
        super(instrumentDao, auditService, valueService, recurrentTransactionDao, description, budgetGroupId, businesskey);
    }

    public BudgetHandler(InstrumentDao instrumentDao, AuditService auditService, ValueService valueService, RecurrentTransactionDao recurrentTransactionDao, Instrument budget) {
        super(instrumentDao, auditService, valueService, recurrentTransactionDao, budget);
    }

    public BudgetHandler(InstrumentDao instrumentDao, AuditService auditService, ValueService valueService, RecurrentTransactionDao recurrentTransactionDao, int instrumentId) {
        super(instrumentDao, auditService, valueService, recurrentTransactionDao, instrumentId);
    }

    @Override
    protected void createDomainObject() {
        domainObject = new Budget(description, true, ts);
        domainObject.setBusinesskey(businesskey);
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

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
        super.setBusinesskey(description);
    }
}