package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.myfinance.api.domain.BudgetGroup;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BudgetGroupHandler extends AbsInstrumentHandler {
    protected BudgetGroup budgetGroup;

    public BudgetGroupHandler(InstrumentDao instrumentDao, int budgetGroupId) {
        super(instrumentDao, budgetGroupId);
    }

    public BudgetGroupHandler(InstrumentDao instrumentDao, Instrument budgetGroup) {
        super(instrumentDao, budgetGroup.getInstrumentid());
        if(!budgetGroup.getInstrumentType().equals(InstrumentType.BUDGETGROUP)) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create BudgetGroupHandler for instrumentType:"+budgetGroup.getInstrumentType());
        }
        this.budgetGroup = (BudgetGroup)budgetGroup;
    }

    public Instrument getIncomeBudget() {
        instrumentGraphHandler.getInstrumentFirstLevelChildsWithType(instrumentId, instrumentType, onlyActive)
        List<Instrument> incomeBudgets = instrumentDao.getInstrumentChilds(budgetGroupId, EdgeType.INCOMEBUDGET);
        if(incomeBudgets == null || incomeBudgets.isEmpty()) {
            throw new MFException(MFMsgKey.NO_INCOMEBUDGET_DEFINED_EXCEPTION, "No IncomeBudget defined for budgetGroupId:"+budgetGroupId);
        }
        return incomeBudgets.get(0);
    }
}