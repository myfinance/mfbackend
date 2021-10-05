package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.BudgetGroup;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BudgetGroupHandler extends AbsInstrumentHandler {
    protected BudgetGroup budgetGroup;
    private int budgetPFId;

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, int budgetGroupId) {
        super(instrumentDao, auditService);
        setInstrumentId(budgetGroupId);
    }

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument budgetGroup) {
        super(instrumentDao, auditService);
        setInstrumentId(budgetGroup.getInstrumentid());
        if(!budgetGroup.getInstrumentType().equals(InstrumentType.BUDGETGROUP)) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create BudgetGroupHandler for instrumentType:"+budgetGroup.getInstrumentType());
        }
        this.budgetGroup = (BudgetGroup)budgetGroup;
    }

    public BudgetGroupHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int budgetPFId) {
        super(instrumentDao, auditService);
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

        // stattdessen instrumentproperty anlegen
        instrumentGraphHandler.addInstrumentToGraph(budgetGroupId,budgetGroupId,EdgeType.INCOMEBUDGET);
        int incomeBudgetId = createBudget(DEFAULT_INCOMEBUDGET_PREFIX+description, budgetGroupId, ts);
        instrumentGraphHandler.addInstrumentToGraph(incomeBudgetId, budgetGroupId, EdgeType.INCOMEBUDGET);
        
    }
}