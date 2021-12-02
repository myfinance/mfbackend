package de.hf.dac.myfinance.instrumenthandler;

import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public abstract class SecurityHandler extends AbsInstrumentHandlerWithProperty implements InstrumentHandler {

    public SecurityHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
    }

    public SecurityHandler(InstrumentDao instrumentDao, AuditService auditService, String businesskey, String description) {
        super(instrumentDao, auditService, description, businesskey.toUpperCase());
    }

    @Override
    protected Instrument getExistingObject() {
        var existingSec = getSecurity(businesskey);
        if (existingSec.isPresent()) {
            exists = true;
            return existingSec.get(); 
            
        } 
        exists = false;
        return null;
    }

    protected abstract Optional<Instrument> getSecurity(String businesskey);
}