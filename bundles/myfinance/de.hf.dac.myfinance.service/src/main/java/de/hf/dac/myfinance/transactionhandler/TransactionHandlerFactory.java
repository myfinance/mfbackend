package de.hf.dac.myfinance.transactionhandler;

import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.TradeDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;

public class TransactionHandlerFactory {
    private InstrumentService instrumentService;
    private TransactionDao transactionDao;
    private AuditService auditService;
    private CashflowDao cashflowDao;
    private TradeDao tradeDao;

    public TransactionHandlerFactory(InstrumentService instrumentService,
            TransactionDao transactionDao, 
            AuditService auditService,
            CashflowDao cashflowDao,
            TradeDao tradeDao) {
        this.instrumentService = instrumentService;
        this.transactionDao = transactionDao;
        this.auditService = auditService;
        this.cashflowDao = cashflowDao;
        this.tradeDao = tradeDao;
    }

    public AbsTransactionHandler createTransactionHandler(int transactionId) {
        Optional<Transaction> transaction = transactionDao.getTransaction(transactionId);
        if(!transaction.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_TRANSACTION_EXCEPTION, "Transaction not updated: Transaction for id:"+transactionId + " not found");
        }
        Transaction oldtransaction = transaction.get();
        AbsTransactionHandler transactionHandler = createTransactionHandler(oldtransaction.getTransactionType());
        transactionHandler.setTransaction(oldtransaction);
        return transactionHandler;
    }

    public AbsTransactionHandler createTransactionHandler(TransactionType transactionType) {
        switch(transactionType){
            case INCOMEEXPENSES: 
                return new IncomeExpensesHandler(instrumentService, transactionDao, auditService, cashflowDao);
            case LINKEDINCOMEEXPENSES: 
                return new LinkedIncomeExpensesHandler(instrumentService, transactionDao, auditService, cashflowDao);                
            case TRADE: 
                return new TradeHandler(instrumentService, transactionDao, auditService, cashflowDao, tradeDao);
            case TRANSFER: 
                return new TransferHandler(instrumentService, transactionDao, auditService, cashflowDao);       
            case BUDGETTRANSFER: 
                return new BudgetTransferHandler(instrumentService, transactionDao, auditService, cashflowDao);                            
            default:
                throw new MFException(MFMsgKey.UNKNOWN_TRNSACTIONTYPE_EXCEPTION, "can not create Transactionhandler for transactionType:"+transactionType);
        }
    }

    public IncomeExpensesHandler createIncomeExpensesHandler() {
        return new IncomeExpensesHandler(instrumentService, transactionDao, auditService, cashflowDao);
    }

    public LinkedIncomeExpensesHandler createLinkedIncomeExpensesHandler() {
        return new LinkedIncomeExpensesHandler(instrumentService, transactionDao, auditService, cashflowDao);    
    }

    public TradeHandler createTradeHandler() {
        return new TradeHandler(instrumentService, transactionDao, auditService, cashflowDao, tradeDao);    
    }

    public TradeHandler createTradeHandler(int transactionId) {
        Optional<Transaction> transaction = transactionDao.getTransaction(transactionId);
        if(!transaction.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_TRANSACTION_EXCEPTION, "Transaction not updated: Transaction for id:"+transactionId + " not found");
        }
        Transaction oldtransaction = transaction.get();
        if(!oldtransaction.getTransactionType().equals(TransactionType.TRADE)) {
            throw new MFException(MFMsgKey.WRONG_TRNSACTIONTYPE_EXCEPTION, "Transactiontype is " + oldtransaction.getTransactionType() + " but trade was expected, Transaction for id:"+transactionId + " not updated");
        }
        TradeHandler transactionHandler = new TradeHandler(instrumentService, transactionDao, auditService, cashflowDao, tradeDao);
        transactionHandler.setTransaction(oldtransaction);
        return transactionHandler;
    }

    public AbsTransferHandler createTransferOrBudgetTransferHandler(Instrument instrument) {
        if(instrument.getInstrumentType() == InstrumentType.BUDGET){
            return new BudgetTransferHandler(instrumentService, transactionDao, auditService, cashflowDao);
        } else {
            return new TransferHandler(instrumentService, transactionDao, auditService, cashflowDao);  
        }  
    }
    
}