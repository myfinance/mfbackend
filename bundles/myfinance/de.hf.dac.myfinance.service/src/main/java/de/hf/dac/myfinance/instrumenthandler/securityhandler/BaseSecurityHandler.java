package de.hf.dac.myfinance.instrumenthandler.securityhandler;

import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BaseSecurityHandler extends SecurityHandler {

    public BaseSecurityHandler(InstrumentDao instrumentDao, AuditService auditService) {
        super(instrumentDao, auditService, -1);
    }

    @Override
    protected Optional<Instrument> getSecurity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void createDomainObject() {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  " domainobject can not be spezified for BaseSecurityHandler");

    }

    @Override
    protected void setDomainObjectName() {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  " domainobject can not be spezified for BaseSecurityHandler");

    }

    @Override
    protected InstrumentType getInstrumentType() {
        throw new MFException(MFMsgKey.WRONG_OPERATION_EXCEPTION,  " InstrumentType can not be spezified for BaseSecurityHandler");
    }

}