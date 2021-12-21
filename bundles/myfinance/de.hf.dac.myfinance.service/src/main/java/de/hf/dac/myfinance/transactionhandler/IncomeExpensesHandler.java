package de.hf.dac.myfinance.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;

public class IncomeExpensesHandler extends AbsTransactionHandler{

    protected Instrument account;
    protected Instrument budget;
        
    public IncomeExpensesHandler(InstrumentService instrumentService, 
            TransactionDao transactionDao, 
            AuditService auditService,
            CashflowDao cashflowDao) {
        super(instrumentService, transactionDao, auditService, cashflowDao);
        transactionType = TransactionType.INCOMEEXPENSES;
    }

    public void init(int accId, 
            int budgetId,
            LocalDateTime ts, 
            String description, 
            double value,
            LocalDate transactionDate) {
        super.init(ts, description, value, transactionDate);                
        this.account = validateInstrument(accId, InstrumentType.GIRO);
        this.budget = validateInstrument(budgetId, InstrumentType.BUDGET);
        saveMsg="new transaction with type incomeExpenses saved with properties: Account "+account.getInstrumentid()+", Budget "+budget.getInstrumentid();
        validateTenant(this.account, this.budget);
    }

    @Override
    protected void updateCache() {
        //valueCurveHandler.invalidateCache(account.getInstrumentid());
        //valueCurveHandler.invalidateCache(budget.getInstrumentid());
    }

    @Override
    protected Set<Cashflow> buildCashflows(double value, Transaction transaction) {
        Cashflow accountCashflow = new Cashflow(account, value);
        accountCashflow.setTransaction(transaction);
        Cashflow budgetCashflow = new Cashflow(budget, value);
        budgetCashflow.setTransaction(transaction);
        Set<Cashflow> cashflows = new HashSet<>();
        cashflows.add(accountCashflow);
        cashflows.add(budgetCashflow);
        return cashflows;
    }  
    
    @Override
    protected void updateCashflows() {
        transaction.getCashflows().forEach(i-> {
                if(i.getValue()!=value) {
                    cashflowDao.updateCashflow(i.getCashflowid(), value);
                    //valueCurveHandler.invalidateCache(i.getInstrument().getInstrumentid());
        }});
    }
}