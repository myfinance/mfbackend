package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDate;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public abstract class AbsCashInstrumentHandler extends AbsInstrumentHandler {

    private ValueCurveService valueService;
    private RecurrentTransactionDao recurrentTransactionDao;

    public AbsCashInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao, String description, int tenantId) {
        super(instrumentDao, auditService, description, tenantId);
        this.valueService = valueService;
        this.recurrentTransactionDao = recurrentTransactionDao;
    }

    public AbsCashInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao, Instrument cashInstrument) {
        super(instrumentDao, auditService, cashInstrument);
        this.valueService = valueService;
        this.recurrentTransactionDao = recurrentTransactionDao;
    }

    protected AbsCashInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao, String description, int tenantId, boolean addToAccountPf) {
        super(instrumentDao, auditService, description, tenantId, addToAccountPf);
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