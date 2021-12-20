package de.hf.dac.myfinance.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;

public class BudgetTransferHandler extends AbsTransferHandler {
   

    
    public BudgetTransferHandler(InstrumentService instrumentService, 
            TransactionDao transactionDao, 
            AuditService auditService,
            ValueCurveHandler valueCurveService,
            CashflowDao cashflowDao) {
        super(instrumentService, transactionDao, auditService, valueCurveService, cashflowDao);
        transactionType = TransactionType.BUDGETTRANSFER;
    }

    @Override
    public void init(Instrument srcInstrument, 
            Instrument trgInstrument,
            LocalDateTime ts, 
            String description, 
            double value,
            LocalDate transactionDate) {
        super.init(srcInstrument, trgInstrument, ts, description, value, transactionDate);      
        saveMsg="new budget transfer saved with properties: Source Budget "+srcInstrument.getInstrumentid()+", target budget "+trgInstrument.getInstrumentid();     
    }
    
    @Override
    protected void validateInstruments() {
        if(srcInstrument.getInstrumentType() != InstrumentType.BUDGET){
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "Only transfers from budget to Budget or from Account to Account are allowed");
        }
    }    
}