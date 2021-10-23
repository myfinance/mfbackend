package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class GiroHandler  extends AbsSimpleInstrumentHandler {
    
    public GiroHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId) {
        super(instrumentDao, auditService, description, tenantId, true);
    }

    public GiroHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument giro) {
        super(instrumentDao, auditService, giro);
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