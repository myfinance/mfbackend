package de.hf.dac.myfinance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.EdgeType;
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
import de.hf.dac.myfinance.service.transactionhandler.AbsTransactionHandler;
import de.hf.dac.myfinance.service.transactionhandler.AbsTransferHandler;
import de.hf.dac.myfinance.service.transactionhandler.IncomeExpensesHandler;
import de.hf.dac.myfinance.service.transactionhandler.LinkedIncomeExpensesHandler;
import de.hf.dac.myfinance.service.transactionhandler.TradeHandler;
import de.hf.dac.myfinance.service.transactionhandler.TransactionHandlerFactory;

public class TransactionServiceImpl implements TransactionService {
    private InstrumentService instrumentService;
    private ValueCurveService valueCurveService;
    private AuditService auditService;
    private TransactionDao transactionDao;
    private RecurrentTransactionDao recurrentTransactionDao;
    private CashflowDao cashflowDao;
    private TransactionHandlerFactory transactionHandlerFactory;

    private static final String AUDIT_MSG_TYPE="TransactionService_User_Event";

    @Inject
    public TransactionServiceImpl(InstrumentService instrumentService, TransactionDao transactionDao, RecurrentTransactionDao recurrentTransactionDao, CashflowDao cashflowDao, 
                AuditService auditService, ValueCurveService valueCurveService){
        this.instrumentService = instrumentService;
        this.transactionDao = transactionDao;
        this.recurrentTransactionDao = recurrentTransactionDao;
        this.cashflowDao = cashflowDao;
        this.valueCurveService = valueCurveService;
        this.auditService = auditService;
        this.transactionHandlerFactory = new TransactionHandlerFactory(this.instrumentService, this.transactionDao, this.auditService, this.valueCurveService, this.cashflowDao);
    }

    @Override
    public void newIncomeExpense(String description, int accId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts){
        IncomeExpensesHandler transactionHandler = transactionHandlerFactory.createIncomeExpensesHandler();
        transactionHandler.init(accId, budgetId, ts, description, value, transactionDate);
        transactionHandler.save();
    }

    @Override
    public void newLinkedIncomeExpense(String description, int accId, int linkedAccId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts){
        LinkedIncomeExpensesHandler transactionHandler = transactionHandlerFactory.createLinkedIncomeExpensesHandler();
        transactionHandler.init(accId, budgetId, ts, description, value, transactionDate, linkedAccId);
        transactionHandler.save();
    }

    @Override
    public void newTransfer(String description, int srcInstrumentId, int trgInstrumentId, double value, LocalDate transactionDate, LocalDateTime ts){
        var srcInstrument = instrumentService.getInstrument(srcInstrumentId, "Can not add transfer: Unknown instrument:");
        var trgInstrument = instrumentService.getInstrument(trgInstrumentId, "Can not add transfer: Unknown instrument:");
        AbsTransferHandler transactionHandler = transactionHandlerFactory.createTransferOrBudgetTransferHandler(srcInstrument);
        transactionHandler.init(srcInstrument, trgInstrument, ts, description, value, transactionDate);
        transactionHandler.save();
    }

    @Override
    public void updateTransaction(int transactionId, String description, double value, LocalDate transactionDate, LocalDateTime ts){
        AbsTransactionHandler transactionHandler = transactionHandlerFactory.createTransactionHandler(transactionId);
        transactionHandler.updateTransaction(description, value, transactionDate, ts);
    }

    @Override
    public void deleteTransaction(int transactionId){
        Optional<Transaction> transaction = transactionDao.getTransaction(transactionId);
        if(transaction.isPresent()){
            transaction.get().getCashflows().forEach(i-> {
                valueCurveService.updateCache(i.getInstrument().getInstrumentid());
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
        if(src.getInstrumentType() == InstrumentType.BUDGET && trg.getInstrumentType() == InstrumentType.BUDGET) {
            recurrentTransactionType = RecurrentTransactionType.BudgetTransfer;
        } else if (src.getInstrumentType() == InstrumentType.BUDGET) {
            if( !isAccountTransferAllowed(trg)) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+trgInstrumentId);
            }
            recurrentTransactionType = getRecurrentTransactiontype(value);
        } else if (trg.getInstrumentType() == InstrumentType.BUDGET) {
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

    @Override
    public void newTrade(String description, int depotId, String isin, double amount, int accId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts){
        var transactionHandler = transactionHandlerFactory.createTradeHandler();
        transactionHandler.init(accId, budgetId, isin, depotId, amount, ts, description, value, transactionDate);
        transactionHandler.save();
    }

}
