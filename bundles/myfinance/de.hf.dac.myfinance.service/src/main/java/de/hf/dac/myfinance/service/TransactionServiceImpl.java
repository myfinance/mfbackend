package de.hf.dac.myfinance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.RecurrentFrequency;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.domain.RecurrentTransactionType;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class TransactionServiceImpl implements TransactionService {
    private InstrumentService instrumentService;
    private ValueCurveService service;
    private AuditService auditService;
    private TransactionDao transactionDao;
    private RecurrentTransactionDao recurrentTransactionDao;
    private CashflowDao cashflowDao;

    private static final String AUDIT_MSG_TYPE="TransactionService_User_Event";

    @Inject
    public TransactionServiceImpl(InstrumentService instrumentService, TransactionDao transactionDao, RecurrentTransactionDao recurrentTransactionDao, CashflowDao cashflowDao, 
                AuditService auditService, ValueCurveService service){
        this.instrumentService = instrumentService;
        this.transactionDao = transactionDao;
        this.recurrentTransactionDao = recurrentTransactionDao;
        this.cashflowDao = cashflowDao;
        this.service = service;
        this.auditService = auditService;
    }

    @Override
    public void newIncomeExpense(String description, int accId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts){
        var account = instrumentService.getInstrument(accId, "IncomeExpense not saved. Unknown account:");
        if(account.getInstrumentType()!=InstrumentType.Giro){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "IncomeExpense not saved: wrong instrument type:"+accId);
        }
        var budget = instrumentService.getInstrument(budgetId, "IncomeExpense not saved: unknown budget:");
        if(budget.getInstrumentType()!=InstrumentType.Budget){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "IncomeExpense not saved: wrong instrument type:"+budgetId);
        }
        Optional<Integer> tenantOfAcc = instrumentService.getRootInstrument(accId, EdgeType.TENANTGRAPH);
        Optional<Integer> tenantOfBudget = instrumentService.getRootInstrument(budgetId, EdgeType.TENANTGRAPH);

        if(!tenantOfAcc.isPresent()
            || !tenantOfBudget.isPresent()
            || tenantOfAcc.get()!=tenantOfBudget.get()){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION, "IncomeExpense not saved: budget and account have not the same tenant");
        }
        Transaction transaction = new Transaction(description, transactionDate, ts, TransactionType.INCOMEEXPENSES);

        Cashflow accountCashflow = new Cashflow(account.get(), value);
        accountCashflow.setTransaction(transaction);
        Cashflow budgetCashflow = new Cashflow(budget.get(), value);
        budgetCashflow.setTransaction(transaction);
        Set<Cashflow> cashflows = new HashSet<>();
        cashflows.add(accountCashflow);
        cashflows.add(budgetCashflow);

        service.updateCache(accId);
        service.updateCache(budgetId);

        transaction.setCashflows(cashflows);
        auditService.saveMessage("new transaction saved for Account "+accId+" and Budget "+budgetId+". Date:" + transactionDate + ", value:" + value + ", desc:" +description,
            Severity.INFO, AUDIT_MSG_TYPE);
        transactionDao.saveTransaction(transaction);
    }

    @Override
    public void newTransfer(String description, int srcInstrumentId, int trgInstrumentId, double value, LocalDate transactionDate, LocalDateTime ts){
        var src = instrumentService.getInstrument(srcInstrumentId, "Transfer not saved:");
        TransactionType transactionType =TransactionType.TRANSFER;
        var trg = instrumentService.getInstrument(trgInstrumentId, "Transfer not saved:");
        if(trg.getInstrumentType() == InstrumentType.Budget){
            transactionType =TransactionType.BUDGETTRANSFER;
            if(src.getInstrumentType() != InstrumentType.Budget){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "Only transfers from budget to Budget or from Account to Account are allowed");
            }
        } else {
            if( !isAccountTransferAllowed(trg) || !isAccountTransferAllowed(src) ){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this accounts:");
            }
        }

        Optional<Integer> tenantSrc = instrumentService.getRootInstrument(srcInstrumentId, EdgeType.TENANTGRAPH);
        Optional<Integer> tenantTrg = instrumentService.getRootInstrument(trgInstrumentId, EdgeType.TENANTGRAPH);

        if(!tenantSrc.isPresent()
            || !tenantTrg.isPresent()
            || tenantSrc.get()!=tenantTrg.get()){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION, "IncomeExpense not saved: budget and account have not the same tenant");
        }
        Transaction transaction = new Transaction(description, transactionDate, ts, transactionType);

        Cashflow srcCashflow = new Cashflow(src.get(), value * -1);
        srcCashflow.setTransaction(transaction);
        Cashflow trgCashflow = new Cashflow(trg.get(), value);
        trgCashflow.setTransaction(transaction);
        Set<Cashflow> cashflows = new HashSet<>();
        cashflows.add(srcCashflow);
        cashflows.add(trgCashflow);

        service.updateCache(srcInstrumentId);
        service.updateCache(trgInstrumentId);

        transaction.setCashflows(cashflows);
        auditService.saveMessage("new transaction saved for Instrument "+srcInstrumentId+" and  "+trgInstrumentId+". Date:" + transactionDate + ", value:" + value + ", desc:" +description,
            Severity.INFO, AUDIT_MSG_TYPE);
        transactionDao.saveTransaction(transaction);
    }

    @Override
    public void updateTransaction(int transactionId, String description, double value, LocalDate transactionDate, LocalDateTime ts){
        Optional<Transaction> transaction = transactionDao.getTransaction(transactionId);
        if(!transaction.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_TRANSACTION_EXCEPTION, "Transaction not updated: Transaction for id:"+transactionId + " not found");
        }
        Transaction oldtransaction = transaction.get();
        transactionDao.updateTransaction(transactionId, description, transactionDate, ts);
        if(oldtransaction.getTransactionType() == TransactionType.INCOMEEXPENSES) {
            oldtransaction.getCashflows().forEach(i-> {
                if(i.getValue()!=value) {
                    cashflowDao.updateCashflow(i.getCashflowid(), value);
                    service.updateCache(i.getInstrument().getInstrumentid());
                }});
        } else if(oldtransaction.getTransactionType() == TransactionType.BUDGETTRANSFER ||
                oldtransaction.getTransactionType() == TransactionType.TRANSFER) {
            oldtransaction.getCashflows().forEach(i-> {
                if( (i.getValue() < 0 && value < 0) || (i.getValue() > 0 && value > 0)) {
                    cashflowDao.updateCashflow(i.getCashflowid(), value);
                } else {
                    cashflowDao.updateCashflow(i.getCashflowid(), -1 * value);
                }
                service.updateCache(i.getInstrument().getInstrumentid());
            });
        }
        auditService.saveMessage(" transaction with id "+transactionId+" ,desc: '"+oldtransaction.getDescription()+
                        "' and Transactiondate:" + oldtransaction.getTransactiondate() + "updated to desc="+description + ", date=" + transactionDate +
                        " and value=" + value,
                Severity.INFO, AUDIT_MSG_TYPE);

    }

    @Override
    public void deleteTransaction(int transactionId){
        Optional<Transaction> transaction = transactionDao.getTransaction(transactionId);
        if(transaction.isPresent()){
            transaction.get().getCashflows().forEach(i-> {
                service.updateCache(i.getInstrument().getInstrumentid());
            });
            auditService.saveMessage(transactionDao.deleteTransaction(transactionId),
                    Severity.INFO, AUDIT_MSG_TYPE);
        }
    }

    @Override
    public List<RecurrentTransaction> listRecurrentTransactions() {
        return recurrentTransactionDao.listRecurrentTransactions();
    }

    @Override
    public void newRecurrentTransaction(String description, int srcInstrumentId, int trgInstrumentId, RecurrentFrequency recurrentFrequency, double value, LocalDate nextTransactionDate, LocalDateTime ts) {
        var src = instrumentService.getInstrument(srcInstrumentId, "RecurrentTransfer not saved:");
        RecurrentTransactionType recurrentTransactionType = RecurrentTransactionType.Transfer;
        var trg = instrumentService.getInstrument(trgInstrumentId, "RecurrentTransfer not saved:");
        if(src.getInstrumentType() == InstrumentType.Budget && trg.getInstrumentType() == InstrumentType.Budget) {
            recurrentTransactionType = RecurrentTransactionType.BudgetTransfer;
        } else if (src.getInstrumentType() == InstrumentType.Budget) {
            if( !isAccountTransferAllowed(trg)) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+trgInstrumentId);
            }
            recurrentTransactionType = getRecurrentTransactiontype(value);
        } else if (trg.getInstrumentType() == InstrumentType.Budget) {
            if( !isAccountTransferAllowed(src)) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+srcInstrumentId);
            }
            recurrentTransactionType = getRecurrentTransactiontype(value);
        } else {
            if( !isAccountTransferAllowed(trg)){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+trgInstrumentId);
            }
            if( !isAccountTransferAllowed(src)){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+srcInstrumentId);
            }
            recurrentTransactionType = RecurrentTransactionType.Transfer;
        }

        Optional<Integer> tenantSrc = instrumentService.getRootInstrument(srcInstrumentId, EdgeType.TENANTGRAPH);
        Optional<Integer> tenantTrg = instrumentService.getRootInstrument(trgInstrumentId, EdgeType.TENANTGRAPH);

        if(!tenantSrc.isPresent()
                || !tenantTrg.isPresent()
                || tenantSrc.get()!=tenantTrg.get()){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION, "RecurrentTransfer not saved: budget and account have not the same tenant");
        }
        RecurrentTransaction recurrentTransaction = new RecurrentTransaction(src, trg, recurrentTransactionType.getValue(), description, value, nextTransactionDate, recurrentFrequency);

        auditService.saveMessage("new recurrenttransaction saved for Instrument "+srcInstrumentId+
                        " and  "+trgInstrumentId+". nextTransactionDate:" + nextTransactionDate +
                        ", value:" + value + ", desc:" +description + ", frequency:" +recurrentFrequency,
                Severity.INFO, AUDIT_MSG_TYPE);
        recurrentTransactionDao.saveRecurrentTransaction(recurrentTransaction);
    }

    @Override
    public void deleteRecurrentTransaction(int recurrentTransactionId) {
        Optional<RecurrentTransaction> transaction = recurrentTransactionDao.getRecurrentTransaction(recurrentTransactionId);
        if(transaction.isPresent()){
            auditService.saveMessage(recurrentTransactionDao.deleteRecurrentTransaction(recurrentTransactionId),
                    Severity.INFO, AUDIT_MSG_TYPE);
        }
    }

    @Override
    public void updateRecurrentTransaction(int id, String description, double value, LocalDate nexttransaction, LocalDateTime ts) {
        Optional<RecurrentTransaction> transaction = recurrentTransactionDao.getRecurrentTransaction(id);
        if(!transaction.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_TRANSACTION_EXCEPTION, "RecurrentTransaction not updated: RecurrentTransaction for id:"+id + " not found");
        }
        RecurrentTransaction oldtransaction = transaction.get();
        if(
                (RecurrentTransactionType.getRecurrentTransactionTypeById(oldtransaction.getRecurrencytype()) != RecurrentTransactionType.Expenses
                    && value < 0) || (
            RecurrentTransactionType.getRecurrentTransactionTypeById(oldtransaction.getRecurrencytype()) == RecurrentTransactionType.Expenses
                    && value > 0) ){
            throw new MFException(MFMsgKey.WRONG_TRNSACTIONTYPE_EXCEPTION, "RecurrentTransaction not updated: Type:"
                    +RecurrentTransactionType.getRecurrentTransactionTypeById(oldtransaction.getRecurrencytype()) +
                    " and value:"+value + " do not match");
        }

        recurrentTransactionDao.updateRecurrentTransaction(id, description, value, nexttransaction);

        auditService.saveMessage(" recurrenttransaction with id "+id+" ,desc: '"+oldtransaction.getDescription()+
                        "' ,value:" + oldtransaction.getValue() + "" +
                        " and next transaction:" + oldtransaction.getNexttransaction()
                        + "updated to desc="+description + ", date=" + nexttransaction +
                        " and value=" + value,
                Severity.INFO, AUDIT_MSG_TYPE);
    }

    private RecurrentTransactionType getRecurrentTransactiontype(double value) {
        RecurrentTransactionType recurrentTransactionType;
        if(value <0) {
            recurrentTransactionType = RecurrentTransactionType.Expenses;
        } else {
            recurrentTransactionType = RecurrentTransactionType.Income;
        }
        return recurrentTransactionType;
    }

    protected boolean isAccountTransferAllowed(Instrument instrument){
        switch(instrument.getInstrumentType()){
            case Giro:
            case MoneyAtCall:
            case TimeDeposit:
            case BuildingsavingAccount:
            case LifeInsurance: return true;
            default: return false;
        }
    }

    @Override
    public List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate){
        return transactionDao.listTransactions(startDate, endDate);
    }

    @Override
    public List<Cashflow> listInstrumentCashflows(int instrumentId){
        return listInstrumentCashflows(instrumentId);
    }  

    @Override
    public  Map<LocalDate, List<Cashflow>> getInstrumentCashflowMap(int instrumentId){
        return transactionDao.getInstrumentCashflowMap(instrumentId);
    }
    
    @Override
    public void bookRecurrentTransactions(LocalDateTime ts){
        final String Processstep = "bookRecurrentTransactions";
        List<RecurrentTransaction> recurrentTransactions = recurrentTransactionDao.listRecurrentTransactions();
        LocalDateTime endTs = LocalDateTime.now();
        if(recurrentTransactions!=null && !recurrentTransactions.isEmpty()) {
            recurrentTransactions.forEach(i-> {
                LocalDate nextTransaction = i.getNexttransaction();
                while(nextTransaction.isBefore(ts.toLocalDate())) {
                    if(i.getRecurrencytype() == RecurrentTransactionType.Expenses.getValue() ||
                            i.getRecurrencytype() == RecurrentTransactionType.Income.getValue()) {
                        newIncomeExpense(i.getDescription(), i.getInstrumentByInstrumentid1().getInstrumentid(),
                                i.getInstrumentByInstrumentid2().getInstrumentid(), i.getValue(), nextTransaction, ts);
                    } else {
                        newTransfer(i.getDescription(), i.getInstrumentByInstrumentid1().getInstrumentid(),
                                i.getInstrumentByInstrumentid2().getInstrumentid(), i.getValue(), nextTransaction, ts);
                    }
                    nextTransaction = calcNextTransaction(nextTransaction, i.getRecurrentfrequence());
                }
                updateRecurrentTransaction(i.getRecurrenttransactionid(), i.getDescription(), i.getValue(),
                        nextTransaction, ts);
            });
            auditService.saveMessage("recurrent transactions booked", Severity.INFO, AUDIT_MSG_TYPE);
            endTs = LocalDateTime.now();
        } else {
            auditService.saveMessage("no recurrent transactions to book", Severity.INFO, AUDIT_MSG_TYPE);
        }
        auditService.saveSuccessfulJournalEntry(Processstep, "", "NA", ts, endTs);
    }

    private LocalDate calcNextTransaction(LocalDate lastTransaction, RecurrentFrequency frequency) {
        switch(frequency) {
            case Monthly:
                return lastTransaction.plusMonths(1);
            case Quaterly:
                return lastTransaction.plusMonths(3);
            case Yearly:
                return lastTransaction.plusYears(1);
            default:
                return lastTransaction.plusMonths(1);
        }
    }
}