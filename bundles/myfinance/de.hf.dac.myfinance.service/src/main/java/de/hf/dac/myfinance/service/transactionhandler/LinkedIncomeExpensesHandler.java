package de.hf.dac.myfinance.service.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class LinkedIncomeExpensesHandler extends IncomeExpensesHandler {
    private Instrument linkedaccount;
    
    public LinkedIncomeExpensesHandler(InstrumentService instrumentService, 
            TransactionDao transactionDao, 
            AuditService auditService,
            ValueCurveService valueCurveService,
            CashflowDao cashflowDao) {
        super(instrumentService, transactionDao, auditService, valueCurveService, cashflowDao);
        transactionType = TransactionType.LINKEDINCOMEEXPENSES;
    }

    public void init(int accId, 
            int budgetId,
            LocalDateTime ts, 
            String description, 
            double value,
            LocalDate transactionDate, 
            int linkedAccId) {
        super.init(accId, budgetId, ts, description, value, transactionDate);                
        this.linkedaccount = validateLinkedAccount(linkedAccId);
    }


    private Instrument validateLinkedAccount(int linkedAccId) {
        Instrument linkedacc = instrumentService.getInstrument(linkedAccId, ERROR_MSG + ": Unknown linked account:");
        if(linkedacc.getInstrumentType()!=InstrumentType.REALESTATE && linkedacc.getInstrumentType()!=InstrumentType.EQUITY && linkedacc.getInstrumentType()!=InstrumentType.BOND){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, ERROR_MSG + ": wrong linked instrument type:" + linkedacc.getInstrumentid());
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