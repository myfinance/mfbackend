package de.hf.dac.myfinance.instrumenthandler;

import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.BudgetGroup;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BudgetGroupHandler extends AbsInstrumentHandler {
    private  final InstrumentFactory instrumentFactory;
    private static final String DEFAULT_INCOMEBUDGET_PREFIX = "incomeBudget_";

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, int budgetGroupId) {
        super(instrumentDao, auditService, budgetGroupId);
        this.instrumentFactory = instrumentFactory;
    }

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, Instrument budgetGroup) {
        super(instrumentDao, auditService, budgetGroup);
        this.instrumentFactory = instrumentFactory;
    }

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, String description, int budgetPFId) {
        super(instrumentDao, auditService, description, budgetPFId);
        this.instrumentFactory = instrumentFactory;
    }

    public Instrument getIncomeBudget() {
        var properties = getInstrumentProperties();
        Optional<InstrumentProperties> incomeBudgetIdProperty = properties.stream().filter(i->i.getPropertyname().equals(InstrumentPropertyType.INCOMEBUDGETID.getStringValue())).findFirst();
        if(!incomeBudgetIdProperty.isPresent()) {
            throw new MFException(MFMsgKey.NO_INCOMEBUDGET_DEFINED_EXCEPTION, "No IncomeBudget defined for budgetGroupId:"+instrumentId);
        }
        int incomeBudgetId = Integer.parseInt(incomeBudgetIdProperty.get().getValue());
        var incomeBudget = instrumentDao.getInstrument(incomeBudgetId);
        if(!incomeBudget.isPresent()) {
            throw new MFException(MFMsgKey.NO_INCOMEBUDGET_DEFINED_EXCEPTION, "the IncomeBudget with id:"+incomeBudgetId+" does not exists");
        }
        
        return incomeBudget.get();
    } 

    @Override
    public void save(){
        super.save();
        var budgetHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGET, DEFAULT_INCOMEBUDGET_PREFIX + domainObject.getDescription(), instrumentId);
        budgetHandler.setTreeLastChanged(ts);
        budgetHandler.save();
        saveProperty(InstrumentPropertyType.INCOMEBUDGETID, budgetHandler.getInstrumentId());
    }

    @Override
    protected void createDomainObject(String description) {
        domainObject = new BudgetGroup(description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "BudgetGroup";
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.BUDGETGROUP;
    }
}