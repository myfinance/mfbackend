package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDateTime;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException; 
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

/**
 * this is the most simple instrumenthandler to get type independent informations for the instrument and his tenant without loading it. You only need the instrumentId
 */
public final class BaseAccountableInstrumentHandlerImpl extends AbsAccountableInstrumentHandler implements BaseAccountableInstrumentHandler{


    public BaseAccountableInstrumentHandlerImpl(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);     
    }

    @Override
    protected void createDomainObject(String description) {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  " domainobject can not be spezified for BaseAccountableInstrumentHandler");
    }

    @Override
    protected void setDomainObjectName() {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  " domainobject can not be spezified for BaseAccountableInstrumentHandler");

    }

    @Override
    protected InstrumentType getInstrumentType() {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  " instrumenttype can not be spezified for BaseAccountableInstrumentHandler");
    }

    public void save() {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  "BaseAccountableInstrumentHandler can not be saved");
    }

    
    public void validateInstrument() {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  "BaseAccountableInstrumentHandler can not be loaded and validated");
    }

    public void setTreeLastChanged(LocalDateTime ts){
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  "TimeStamp can not be set for BaseAccountableInstrumentHandler");
    }

    public void updateInstrument(boolean isActive) {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  "BaseAccountableInstrumentHandler can not be updated");
    }

    public void updateInstrument(String description, boolean isActive) {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  "BaseAccountableInstrumentHandler can not be updated");
    }
}