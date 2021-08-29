package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class InstrumentFactory {

    private final InstrumentDao instrumentDao;

    public InstrumentFactory(InstrumentDao instrumentDao) {
        this.instrumentDao = instrumentDao;
    }

    public AbsInstrumentHandler getInstrumentHandler(int instrumentId) {
        var instrument =  getInstrument(instrumentId, "");
        switch(instrument.getInstrumentType()){
            case TENANT: 
                return new TenantHandler(instrumentDao, instrument);                           
            default:
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "can not create Instrumenthandler for instrumentType:"+instrument.getInstrumentType());
        }
    }

    public TenantHandler getTenantHandler(int instrumentId, boolean validate) {
        if(validate) {
            var instrument =  getInstrument(instrumentId, "");
            if(!instrument.getInstrumentType().equals(InstrumentType.TENANT)) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create tenantthandler for instrumentid:"+instrumentId);
            }
            return new TenantHandler(instrumentDao, instrument);   
        } 
        return new TenantHandler(instrumentDao, instrumentId);   
    }

    public Instrument getInstrument(int instrumentId) {
        return getInstrument(instrumentId, "");
    }


    public Instrument getInstrument(int instrumentId, String errMsg) {
        var instrument = instrumentDao.getInstrument(instrumentId);
        if(!instrument.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, errMsg + " Instrument for id:"+instrumentId + " not found");
        }
        return instrument.get();
    }
}