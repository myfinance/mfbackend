package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.BudgetGroup;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BudgetGroupHandler extends AbsInstrumentHandler {
    protected BudgetGroup budgetGroup;
    private int budgetPFId;
    private  final InstrumentFactory instrumentFactory;
    private static final String DEFAULT_INCOMEBUDGET_PREFIX = "incomeBudget_";
    private static final String AUDIT_MSG_TYPE="BudgetGroupHandler_User_Event";

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, int budgetGroupId) {
        super(instrumentDao, auditService);
        this.instrumentFactory = instrumentFactory;
        setInstrumentId(budgetGroupId);
    }

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, Instrument budgetGroup) {
        super(instrumentDao, auditService);
        this.instrumentFactory = instrumentFactory;
        setInstrumentId(budgetGroup.getInstrumentid());
        if(!budgetGroup.getInstrumentType().equals(InstrumentType.BUDGETGROUP)) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create BudgetGroupHandler for instrumentType:"+budgetGroup.getInstrumentType());
        }
        this.budgetGroup = (BudgetGroup)budgetGroup;
    }

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, String description, int budgetPFId) {
        super(instrumentDao, auditService);
        this.instrumentFactory = instrumentFactory;
        budgetGroup = new BudgetGroup(description, true, ts); 
        this.budgetPFId = budgetPFId;
    }

    public Instrument getIncomeBudget() {
        instrumentGraphHandler.getInstrumentFirstLevelChildsWithType(instrumentId, instrumentType, onlyActive)
        List<Instrument> incomeBudgets = instrumentDao.getInstrumentChilds(budgetGroupId, EdgeType.INCOMEBUDGET);
        if(incomeBudgets == null || incomeBudgets.isEmpty()) {
            throw new MFException(MFMsgKey.NO_INCOMEBUDGET_DEFINED_EXCEPTION, "No IncomeBudget defined for budgetGroupId:"+budgetGroupId);
        }
        return incomeBudgets.get(0);
    }

    @Override
    public void save(){
        instrumentDao.saveInstrument(budgetGroup);
        instrumentId = budgetGroup.getInstrumentid();
        instrumentGraphHandler.addInstrumentToGraph(instrumentId, budgetPFId);
        var budgetHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGET, DEFAULT_INCOMEBUDGET_PREFIX+budgetGroup.getDescription(), instrumentId);
        budgetHandler.setTreeLastChanged(ts);
        budgetHandler.save();
        saveProperty(InstrumentPropertyType.INCOMEBUDGETID, budgetHandler.getInstrumentId());
        auditService.saveMessage("budgetGroup inserted:" + budgetGroup.getDescription(), Severity.INFO, AUDIT_MSG_TYPE);
        
    }
}