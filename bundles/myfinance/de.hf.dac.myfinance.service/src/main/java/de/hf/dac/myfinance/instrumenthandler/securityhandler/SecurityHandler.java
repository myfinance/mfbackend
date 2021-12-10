package de.hf.dac.myfinance.instrumenthandler.securityhandler;

import java.util.List;
import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.instrumenthandler.AbsInstrumentHandlerWithProperty;

public abstract class SecurityHandler extends AbsInstrumentHandlerWithProperty{

    public SecurityHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
    }

    public SecurityHandler(InstrumentDao instrumentDao, AuditService auditService, String businesskey, String description) {
        super(instrumentDao, auditService, description, businesskey.toUpperCase());
    }

    @Override
    protected Instrument getExistingObject() {
        var existingSec = getSecurity();
        if (existingSec.isPresent()) {
            exists = true;
            return existingSec.get(); 
            
        } 
        exists = false;
        return null;
    }

    protected abstract Optional<Instrument> getSecurity();

    public Instrument getSecurity(String businesskey){
        return getSecurity(businesskey, "");
    }

    public Instrument getSecurity(String isin, String errMsg) {
        var instrument = instrumentDao.getSecurity(isin);
        if(!instrument.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, errMsg + " Instrument for isin:"+isin + " not found");
        }
        return instrument.get();
    }

    public List<Instrument> getSecurities(){
        return instrumentDao.getSecurities();
    }
}