package de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler;

import java.time.LocalDate;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.service.ValueService;

public abstract class AbsCashInstrumentHandler extends AbsAccountableInstrumentHandler {

    private ValueService valueService;
    private RecurrentTransactionDao recurrentTransactionDao;

    public AbsCashInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, ValueService valueService, RecurrentTransactionDao recurrentTransactionDao, String description, int tenantId, String businesskey) {
        super(instrumentDao, auditService, description, tenantId, businesskey);
        this.valueService = valueService;
        this.recurrentTransactionDao = recurrentTransactionDao;
    }

    public AbsCashInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, ValueService valueService, RecurrentTransactionDao recurrentTransactionDao, Instrument cashInstrument) {
        super(instrumentDao, auditService, cashInstrument);
        this.valueService = valueService;
        this.recurrentTransactionDao = recurrentTransactionDao;
    }

    public AbsCashInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, ValueService valueService, RecurrentTransactionDao recurrentTransactionDao, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
        this.valueService = valueService;
        this.recurrentTransactionDao = recurrentTransactionDao;
    }

    protected AbsCashInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, ValueService valueService, RecurrentTransactionDao recurrentTransactionDao, String description, int tenantId, boolean addToAccountPf, String businesskey) {
        super(instrumentDao, auditService, description, tenantId, addToAccountPf, businesskey);
        this.valueService = valueService;
        this.recurrentTransactionDao = recurrentTransactionDao;
    }

    @Override
    protected void validateInstrument4Inactivation() {
        if( valueService.getValue(instrumentId, LocalDate.MAX)!=0.0 ){
            throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+instrumentId + " not deactivated. The current value is not 0");
        } 
        validateRecurrentTransactions4InstrumentInactivation();
    }

    
    private void validateRecurrentTransactions4InstrumentInactivation() {
        for (RecurrentTransaction r : recurrentTransactionDao.listRecurrentTransactions()) {

            if( r.getInstrumentByInstrumentid1().getInstrumentid() == instrumentId ||
                    r.getInstrumentByInstrumentid2().getInstrumentid() == instrumentId ) {
                throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+
                        instrumentId + " not deactivated. There are still recurrent transactions for this instrument");
            }
        }
    }
}