package de.hf.dac.myfinance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.RecurrentFrequency;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.domain.RecurrentTransactionType;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.persistence.dao.TradeDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;
import de.hf.dac.myfinance.transactionhandler.AbsTransactionHandler;
import de.hf.dac.myfinance.transactionhandler.AbsTransferHandler;
import de.hf.dac.myfinance.transactionhandler.IncomeExpensesHandler;
import de.hf.dac.myfinance.transactionhandler.LinkedIncomeExpensesHandler;
import de.hf.dac.myfinance.transactionhandler.RecurrentTransactionHandler;
import de.hf.dac.myfinance.transactionhandler.TransactionHandlerFactory;

public class TransactionServiceImpl implements TransactionService {
    private InstrumentService instrumentService;
    private AuditService auditService;
    private TransactionDao transactionDao;
    private RecurrentTransactionDao recurrentTransactionDao;
    private CashflowDao cashflowDao;
    private TradeDao tradeDao;
    private TransactionHandlerFactory transactionHandlerFactory;
    private RecurrentTransactionHandler recurrentTransactionHandler;

    private static final String AUDIT_MSG_TYPE="TransactionService_User_Event";

    @Inject
    public TransactionServiceImpl(InstrumentService instrumentService, TransactionDao transactionDao, RecurrentTransactionDao recurrentTransactionDao, CashflowDao cashflowDao, 
        TradeDao tradeDao,AuditService auditService){
        this.instrumentService = instrumentService;
        this.transactionDao = transactionDao;
        this.recurrentTransactionDao = recurrentTransactionDao;
        this.cashflowDao = cashflowDao;
        this.tradeDao = tradeDao;
        this.auditService = auditService;
        this.transactionHandlerFactory = new TransactionHandlerFactory(this.instrumentService, this.transactionDao, this.auditService, this.cashflowDao, this.tradeDao);
        this.recurrentTransactionHandler = new RecurrentTransactionHandler(instrumentService, transactionDao, auditService, recurrentTransactionDao);
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
        AbsTransactionHandler transactionHandler = transactionHandlerFactory.createTransactionHandler(transactionId);
        transactionHandler.deleteTransaction();
    }

    @Override
    public List<RecurrentTransaction> listRecurrentTransactions() {
        return recurrentTransactionDao.listRecurrentTransactions();
    }

    @Override
    public void newRecurrentTransaction(String description, int srcInstrumentId, int trgInstrumentId, RecurrentFrequency recurrentFrequency, double value, LocalDate nextTransactionDate, LocalDateTime ts) {
        recurrentTransactionHandler.init(description, srcInstrumentId, trgInstrumentId, recurrentFrequency, value, nextTransactionDate, ts);
        recurrentTransactionHandler.save();
    }

    @Override
    public void deleteRecurrentTransaction(int recurrentTransactionId) {
        recurrentTransactionHandler.init(recurrentTransactionId);
        recurrentTransactionHandler.delete();
    }

    @Override
    public void updateRecurrentTransaction(int id, String description, double value, LocalDate nexttransaction, LocalDateTime ts) {
        recurrentTransactionHandler.init(id);
        recurrentTransactionHandler.update();
    }

    @Override
    public List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate){
        return transactionDao.listTransactions(startDate, endDate);
    }

    @Override
    public List<Cashflow> listInstrumentCashflows(int instrumentId){
        return transactionDao.listInstrumentCashflows(instrumentId);
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

    @Override
    public void updateTrade(int tradsactionid, String description, double amount, double value, LocalDate transactionDate, LocalDateTime ts) {
        var tradeHandler = transactionHandlerFactory.createTradeHandler(tradsactionid);
        tradeHandler.updateTrade(description, amount, value, transactionDate, ts);
    }

    @Override
    public List<Transaction> getTrades(int depotId){
        return transactionDao.getTrades(depotId);
    }
}
