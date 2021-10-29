package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class GiroHandler extends AbsCashInstrumentHandler {
    
    public GiroHandler(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao, String description, int tenantId) {
        super(instrumentDao, auditService, valueService, recurrentTransactionDao, description, tenantId, true);
    }

    public GiroHandler(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao, Instrument giro) {
        super(instrumentDao, auditService, valueService, recurrentTransactionDao, giro);
    }

    @Override
    protected void createDomainObject(String description) {
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