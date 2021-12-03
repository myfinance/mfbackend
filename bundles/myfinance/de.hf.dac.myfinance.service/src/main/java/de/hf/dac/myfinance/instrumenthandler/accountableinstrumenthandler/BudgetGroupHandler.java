package de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler;

import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.BudgetGroup;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;

public class BudgetGroupHandler extends AbsAccountableInstrumentHandler {
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
        super(instrumentDao, auditService, description, budgetPFId, description);
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
    protected void saveNewInstrument(){
        super.saveNewInstrument();
        var budgetHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGET, DEFAULT_INCOMEBUDGET_PREFIX + domainObject.getDescription(), instrumentId, DEFAULT_INCOMEBUDGET_PREFIX + domainObject.getDescription());
        budgetHandler.setTreeLastChanged(ts);
        budgetHandler.save();
        saveProperty(InstrumentPropertyType.INCOMEBUDGETID, budgetHandler.getInstrumentId());
    }

    @Override
    protected void createDomainObject() {
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

    @Override
    protected void updateInstrument() {
        super.updateInstrument();
        var incomeBudget = getIncomeBudget();
        var handler = instrumentFactory.getInstrumentHandler(incomeBudget.getInstrumentid());
        handler.setDescription(DEFAULT_INCOMEBUDGET_PREFIX + domainObject.getDescription());
        handler.save();
    }

    @Override
    protected void validateInstrument4Inactivation() {
        for(Instrument budget : getInstrumentChilds(EdgeType.TENANTGRAPH, 1)) {
            var budgetHandler = instrumentFactory.getInstrumentHandler(budget.getInstrumentid());
            budgetHandler.setActive(false);
            budgetHandler.save();
        }
    }

    @Override
    protected InstrumentType getParentType() {
        return InstrumentType.BUDGETPORTFOLIO;
    }
} 