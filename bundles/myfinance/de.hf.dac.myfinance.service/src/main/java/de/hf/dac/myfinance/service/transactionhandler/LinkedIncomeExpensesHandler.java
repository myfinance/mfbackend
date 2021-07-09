package de.hf.dac.myfinance.service.transactionhandler;

import java.util.Set;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.service.InstrumentService;

public class LinkedIncomeExpensesHandler extends AbsTransactionHandler {
    private Instrument linkedaccount;
    
    public LinkedIncomeExpensesHandler(InstrumentService instrumentService, 
            int accId, 
            int budgetId, 
            int linkedAccId, 
            boolean isLinkedTransaction) {
        super(instrumentService, accId, budgetId);
        transactionType = TransactionType.INCOMEEXPENSES;
        this.linkedaccount = validateLinkedAccount(linkedAccId, isLinkedTransaction);
    }

    private Instrument validateLinkedAccount(int linkedAccId, boolean isLinkedTransaction) {
        Instrument linkedacc = null;
        if(isLinkedTransaction) {
            linkedacc = instrumentService.getInstrument(linkedAccId, ERROR_MSG + ": Unknown linked account:");
            if(linkedacc.getInstrumentType()!=InstrumentType.REALESTATE && linkedacc.getInstrumentType()!=InstrumentType.EQUITY && linkedacc.getInstrumentType()!=InstrumentType.BOND){
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, ERROR_MSG + ": wrong linked instrument type:" + linkedacc.getInstrumentid());
            }
        }
        return linkedacc;
    }

    @Override
    protected Set<Cashflow> buildCashflows(double value, Transaction transaction) {
        var cashflows = super.buildCashflows(value, transaction);
        var linkedAccCashflow = new Cashflow(linkedaccount, value);
        linkedAccCashflow.setTransaction(transaction);
        cashflows.add(linkedAccCashflow);
        return cashflows;
    }
}