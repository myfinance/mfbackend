package de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class GiroHandler extends AbsCashInstrumentHandler {
    
    public GiroHandler(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao, String description, int tenantId, String businesskey) {
        super(instrumentDao, auditService, valueService, recurrentTransactionDao, description, tenantId, true, businesskey);
    }

    public GiroHandler(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao, Instrument giro) {
        super(instrumentDao, auditService, valueService, recurrentTransactionDao, giro);
    }

    public GiroHandler(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao, int instrumentId) {
        super(instrumentDao, auditService, valueService, recurrentTransactionDao, instrumentId);
    }

    @Override
    protected void createDomainObject() {
        domainObject = new Giro(description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "Giro";
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.GIRO;
    }
}