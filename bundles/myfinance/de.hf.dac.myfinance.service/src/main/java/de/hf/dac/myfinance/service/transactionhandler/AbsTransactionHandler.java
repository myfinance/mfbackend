package de.hf.dac.myfinance.service.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public abstract class AbsTransactionHandler {
    protected InstrumentService instrumentService;
    protected Instrument account;
    protected Instrument budget;
    
    protected static final String ERROR_MSG = "IncomeExpense not saved"; 

    protected static final String AUDIT_MSG_TYPE="TransactionHandler_User_Event";

    protected TransactionType transactionType;

    protected AbsTransactionHandler(InstrumentService instrumentService, 
            int accId, 
            int budgetId) {
        this.instrumentService = instrumentService;
        this.account = validateInstrument(accId, InstrumentType.GIRO);
        this.budget = validateInstrument(budgetId, InstrumentType.BUDGET);
        validateTenant();
    }

    protected Instrument validateInstrument(int accId, InstrumentType instrumentType) {
        var instrument = instrumentService.getInstrument(accId, ERROR_MSG + ": Unknown instrument:");
        if(instrument.getInstrumentType()!=instrumentType){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, ERROR_MSG + ": wrong instrument type:"+instrument.getInstrumentid());
        }
        return instrument;
    }

    private void validateTenant() {
        Optional<Integer> tenantOfAcc = instrumentService.getRootInstrument(account.getInstrumentid(), EdgeType.TENANTGRAPH);
        Optional<Integer> tenantOfBudget = instrumentService.getRootInstrument(budget.getInstrumentid(), EdgeType.TENANTGRAPH);

        if(!tenantOfAcc.isPresent()
            || !tenantOfBudget.isPresent()
            || !tenantOfAcc.get().equals(tenantOfBudget.get())){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION, ERROR_MSG + ": budget and account have not the same tenant");
        }
    }

    public void save(TransactionDao transactionDao, AuditService auditService, ValueCurveService service, LocalDateTime ts, String description, double value, LocalDate transactionDate){

        Transaction transaction = prepareTransaction(ts, description, value, transactionDate);
        transactionDao.saveTransaction(transaction);
        auditService.saveMessage("new transaction saved for Account "+account.getInstrumentid()+" and Budget "+budget.getInstrumentid()+". Date:" + transactionDate + ", value:" + value + ", desc:" +description,
            Severity.INFO, AUDIT_MSG_TYPE);
        service.updateCache(account.getInstrumentid());
        service.updateCache(budget.getInstrumentid());
    }

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

    protected Transaction prepareTransaction(LocalDateTime ts, String description, double value, LocalDate transactionDate) {
        Transaction transaction = new Transaction(description, transactionDate, ts, transactionType);
        var cashflows = buildCashflows(value, transaction);
        transaction.setCashflows(cashflows);
        return transaction;
    }
    
}