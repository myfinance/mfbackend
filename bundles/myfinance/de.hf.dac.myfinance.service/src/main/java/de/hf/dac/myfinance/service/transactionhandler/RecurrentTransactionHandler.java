package de.hf.dac.myfinance.service.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.RecurrentFrequency;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.domain.RecurrentTransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;

public class RecurrentTransactionHandler extends AbsHandler{

    protected TransactionDao transactionDao;
    protected AuditService auditService;
    private RecurrentTransactionDao recurrentTransactionDao;

    protected LocalDateTime ts;
    protected String description; 
    protected double value;
    protected LocalDate nextTransactionDate;
    protected RecurrentFrequency recurrentFrequency;
    protected Instrument firstInstrument;
    protected Instrument secondInstrument;
    protected RecurrentTransactionType recurrentTransactionType = RecurrentTransactionType.Transfer;
    protected RecurrentTransaction recurrentTransaction;
    protected int recurrentTransactionId;
    private String errorMsg;
    private static final String AUDIT_MSG_TYPE="RecurrentTransaction_User_Event";
    private boolean isNewRecurrentTransaction;
    
    public RecurrentTransactionHandler(InstrumentService instrumentService,
            TransactionDao transactionDao, 
            AuditService auditService,
            RecurrentTransactionDao recurrentTransactionDao) {
        super(instrumentService);  
        ERROR_MSG = "Recurrent Transaction not saved"; 
        this.auditService = auditService;  
        this.transactionDao = transactionDao; 
        this.recurrentTransactionDao = recurrentTransactionDao;  
    }

    public void init(String description, int srcInstrumentId, int trgInstrumentId, RecurrentFrequency recurrentFrequency, double value, LocalDate nextTransactionDate, LocalDateTime ts) {
        isNewRecurrentTransaction=true;
        this.ts = ts;
        this.description = description;
        this.value = value;
        this.recurrentFrequency = recurrentFrequency;
        this.nextTransactionDate = nextTransactionDate;
        firstInstrument = instrumentService.getInstrument(srcInstrumentId, "RecurrentTransfer not saved:");
        secondInstrument = instrumentService.getInstrument(trgInstrumentId, "RecurrentTransfer not saved:");
        errorMsg = "No Transfer allowed for this accounts:" + firstInstrument.getInstrumentid() + ", " + secondInstrument.getInstrumentid();
        validateTenant(firstInstrument, secondInstrument);
        evaluateRecurrentTransactionType();
        recurrentTransaction = new RecurrentTransaction(firstInstrument, secondInstrument, recurrentTransactionType.getValue(), description, value, nextTransactionDate, recurrentFrequency);
    }

    public void init(int recurrentTransactionId) {
        this.recurrentTransactionId = recurrentTransactionId;
        isNewRecurrentTransaction = false;
        Optional<RecurrentTransaction> transaction = recurrentTransactionDao.getRecurrentTransaction(recurrentTransactionId);
        if(!transaction.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_TRANSACTION_EXCEPTION, "RecurrentTransaction not updated: RecurrentTransaction for id:"+recurrentTransactionId + " not found");
        }
        recurrentTransaction = transaction.get();
        if(
                (RecurrentTransactionType.getRecurrentTransactionTypeById(recurrentTransaction.getRecurrencytype()) != RecurrentTransactionType.Expenses
                    && value < 0) || (
            RecurrentTransactionType.getRecurrentTransactionTypeById(recurrentTransaction.getRecurrencytype()) == RecurrentTransactionType.Expenses
                    && value > 0) ){
            throw new MFException(MFMsgKey.WRONG_TRNSACTIONTYPE_EXCEPTION, "RecurrentTransaction not updated: Type:"
                    +RecurrentTransactionType.getRecurrentTransactionTypeById(recurrentTransaction.getRecurrencytype()) +
                    " and value:"+value + " do not match");
        }
    }
    
    private RecurrentTransactionType getRecurrentTransactiontype(double value) {
        if(value <0) {
            recurrentTransactionType = RecurrentTransactionType.Expenses;
        } else {
            recurrentTransactionType = RecurrentTransactionType.Income;
        }
        return recurrentTransactionType;
    }

    protected void evaluateRecurrentTransactionType() {
        if(firstInstrument.getInstrumentType() == InstrumentType.BUDGET) {
            if(secondInstrument.getInstrumentType() == InstrumentType.BUDGET) {
                recurrentTransactionType = RecurrentTransactionType.BudgetTransfer;
            } else if( secondInstrument.getInstrumentType() == InstrumentType.GIRO ) {
                recurrentTransactionType = getRecurrentTransactiontype(value);
            } else {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, errorMsg);
            }
        } else if(firstInstrument.getInstrumentType() == InstrumentType.GIRO){
            if(secondInstrument.getInstrumentType() == InstrumentType.BUDGET) {
                recurrentTransactionType = getRecurrentTransactiontype(value);
            } else if(isAccountTransferAllowed(secondInstrument)) {
                recurrentTransactionType = RecurrentTransactionType.Transfer;
            } else {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, errorMsg);
            }
        } else if(isAccountTransferAllowed(firstInstrument)){
            if(isAccountTransferAllowed(secondInstrument)) {
                recurrentTransactionType = RecurrentTransactionType.Transfer;
            } else {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, errorMsg);
            }
        } else {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, errorMsg);
        }
    }

    public void save() {
        if(!isNewRecurrentTransaction) {
            throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION, errorMsg+": you can not use save for existing recurrent transactions. Please use update");
        }
        auditService.saveMessage("new recurrenttransaction saved for Instrument "+firstInstrument.getInstrumentid()+
            " and  "+secondInstrument.getInstrumentid()+". nextTransactionDate:" + nextTransactionDate +
            ", value:" + value + ", desc:" +description + ", frequency:" +recurrentFrequency,
            Severity.INFO, AUDIT_MSG_TYPE);
        recurrentTransactionDao.saveRecurrentTransaction(recurrentTransaction);
    }

    public void update() {
        if(isNewRecurrentTransaction) {
            throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION, errorMsg+": you can not use update for new recurrent transactions. Please use save");
        }
        
        recurrentTransactionDao.updateRecurrentTransaction(recurrentTransactionId, description, value, nextTransactionDate);

        auditService.saveMessage(" recurrenttransaction with id "+recurrentTransactionId+" ,desc: '"+recurrentTransaction.getDescription()+
                        "' ,value:" + recurrentTransaction.getValue() + "" +
                        " and next transaction:" + recurrentTransaction.getNexttransaction()
                        + "updated to desc="+description + ", date=" + nextTransactionDate +
                        " and value=" + value,
                Severity.INFO, AUDIT_MSG_TYPE);
    }

    public void delete() {
        if(isNewRecurrentTransaction) {
            throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION, errorMsg+": you can not delete new recurrent transactions.");
        }
    }
}