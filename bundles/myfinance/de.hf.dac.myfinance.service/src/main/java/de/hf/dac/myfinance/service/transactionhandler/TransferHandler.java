package de.hf.dac.myfinance.service.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class TransferHandler extends AbsTransferHandler {
   
    public TransferHandler(InstrumentService instrumentService, 
            TransactionDao transactionDao, 
            AuditService auditService,
            ValueCurveService valueCurveService,
            CashflowDao cashflowDao) {
        super(instrumentService, transactionDao, auditService, valueCurveService, cashflowDao);
        transactionType = TransactionType.TRANSFER;
    }

    @Override
    public void init(Instrument srcInstrument, 
            Instrument trgInstrument,
            LocalDateTime ts, 
            String description, 
            double value,
            LocalDate transactionDate) {
        super.init(srcInstrument, trgInstrument, ts, description, value, transactionDate);
        saveMsg="new transfer saved with properties: Source Account "+srcInstrument.getInstrumentid()+", target acoount "+trgInstrument.getInstrumentid();        
    }
    
    @Override
    protected void validateInstruments() {
        if( !isAccountTransferAllowed(trgInstrument) || !isAccountTransferAllowed(srcInstrument) ){
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this accounts:");
        }
    }
}

