package de.hf.dac.myfinance.service.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
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

public abstract class AbsTransactionHandler extends AbsHandler{


    protected static final String AUDIT_MSG_TYPE="TransactionHandler_User_Event";
    protected String saveMsg = "Transaction saved";

    protected TransactionType transactionType;
    protected TransactionDao transactionDao;
    protected AuditService auditService;
    protected ValueCurveService valueCurveService;
    protected CashflowDao cashflowDao;

    protected LocalDateTime ts;
    protected String description; 
    protected double value;
    protected LocalDate transactionDate;
    
    protected Transaction transaction;

    protected AbsTransactionHandler(InstrumentService instrumentService,
            TransactionDao transactionDao, 
            AuditService auditService,
            ValueCurveService valueCurveService,
            CashflowDao cashflowDao) {
        super(instrumentService);
        this.auditService = auditService;  
        this.transactionDao = transactionDao;   
        this.valueCurveService = valueCurveService;
        this.cashflowDao = cashflowDao;
    }

    protected void init(LocalDateTime ts, 
            String description, 
            double value,
            LocalDate transactionDate) {
        this.ts = ts;
        this.description = description;
        this.value = value;
        this.transactionDate = transactionDate;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    protected Instrument validateInstrument(int instrumentId, InstrumentType instrumentType) {
        return validateInstrument(instrumentService.getInstrument(instrumentId, ERROR_MSG + ": Unknown instrument:"), instrumentType);
    }

    protected Instrument validateInstrument(Instrument instrument, InstrumentType instrumentType) {
        if(instrument.getInstrumentType()!=instrumentType){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, ERROR_MSG + ": wrong instrument type:"+instrument.getInstrumentid());
        }
        return instrument;
    }

    public void save(){

        prepareTransaction(ts, description, value, transactionDate);
        transactionDao.saveTransaction(transaction);
        auditService.saveMessage(saveMsg + ", Date:" + transactionDate + ", value:" + value + ", desc:" +description,
            Severity.INFO, AUDIT_MSG_TYPE);
        updateCache();
    }

    abstract void updateCache();

    abstract Set<Cashflow> buildCashflows(double value, Transaction transaction);

    protected void prepareTransaction(LocalDateTime ts, String description, double value, LocalDate transactionDate) {
        transaction = new Transaction(description, transactionDate, ts, transactionType);
        var cashflows = buildCashflows(value, transaction);
        transaction.setCashflows(cashflows);
    }

    abstract void updateCashflows();

    public void loadTransaction(int transactionId) {
        
    }
    
    public void updateTransaction( 
            String description, 
            double value, 
            LocalDate transactionDate, 
            LocalDateTime ts){

        transactionDao.updateTransaction(transaction.getTransactionid(), description, transactionDate, ts);
        this.value = value;
        updateCashflows();
        auditService.saveMessage(" transaction with id "+transaction.getTransactionid()+" ,desc: '"+transaction.getDescription()+
            "' and Transactiondate:" + transaction.getTransactiondate() + "updated to desc="+description + ", date=" + transactionDate +
            " and value=" + value, Severity.INFO, AUDIT_MSG_TYPE);
    }

}