package de.hf.dac.myfinance.service.transactionhandler;

import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.service.InstrumentService;

public class IncomeExpensesHandler extends AbsTransactionHandler{
    
    public IncomeExpensesHandler(InstrumentService instrumentService, 
            int accId, 
            int budgetId) {
        super(instrumentService, accId, budgetId);
        transactionType = TransactionType.INCOMEEXPENSES;
    }
    
}