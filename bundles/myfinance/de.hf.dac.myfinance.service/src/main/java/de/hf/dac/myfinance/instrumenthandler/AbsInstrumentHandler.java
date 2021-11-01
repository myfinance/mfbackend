package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

/**
 * Base class for alle instrument handler
 */
public abstract class AbsInstrumentHandler {
    protected InstrumentDao instrumentDao;
    protected int instrumentId;
    protected boolean initialized = false;
    protected Instrument domainObject;
    private List<InstrumentProperties> properties;
    protected boolean isPropertyInit = false;
    protected AuditService auditService;
    protected LocalDateTime ts;
    private static final String AUDIT_MSG_TYPE="InstrumentHandler_User_Event";
    protected String domainObjectName;
    protected String oldDesc;

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService) {
        setBaseValues(instrumentDao, auditService);
    }

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        setBaseValues(instrumentDao, auditService);
        setInstrumentId(instrumentId);
    }

    private void setBaseValues(InstrumentDao instrumentDao, AuditService auditService) {
        this.instrumentDao = instrumentDao;
        this.auditService = auditService;
        ts = LocalDateTime.now();
    }

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument instrument) {
        this(instrumentDao, auditService, instrument.getInstrumentid());

        if (!instrument.getInstrumentType().equals(getInstrumentType())) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create "+domainObjectName+" for instrumentType:"+instrument.getInstrumentType());
        }
        this.domainObject = instrument;
    }

    public void validateInstrument() {
        loadInstrument();
        if(!domainObject.getInstrumentType().equals(getInstrumentType())) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create instrumenthandler for instrumentid:"+instrumentId);
        }
    }

    public void setTreeLastChanged(LocalDateTime ts){
        this.ts = ts;
    }

    public List<InstrumentProperties> getInstrumentProperties() {
        checkInitStatus();
        if(!isPropertyInit) {
            properties = instrumentDao.getInstrumentProperties(instrumentId);
        }
        return properties;
    }

    public List<InstrumentProperties> getInstrumentProperties(InstrumentPropertyType instrumentPropertyType) {
        return getInstrumentProperties().stream().filter(i->i.getPropertyname().equals(instrumentPropertyType.name())).collect(Collectors.toList());
    }
    
    public void setInstrumentId(int instrumentId) {
        initialized = true;
        this.instrumentId = instrumentId;
    }
    public int getInstrumentId() {
        return this.instrumentId;
    }

    protected void checkInitStatus() {
        if(!initialized) {
            throw new MFException(MFMsgKey.OBJECT_NOT_INITIALIZED_EXCEPTION, "instrumentId is not set:");
        }
    }

    protected void checkDomainObjectInitStatus() {
        checkInitStatus();
        if(this.domainObject==null) {
            throw new MFException(MFMsgKey.OBJECT_NOT_INITIALIZED_EXCEPTION, "instrument is not set:");
        }
    }

    public Instrument getInstrument(String errMsg) {
        return getInstrument(instrumentId, errMsg);
    }

    public Instrument getInstrument() {
        return getInstrument(instrumentId, "");
    }

    protected void loadInstrument() {
        if(this.domainObject==null) {
            checkInitStatus();
            domainObject = getInstrument();
        }
    }

    /**
     * get and validate an Instrument for another id. the instrumentId of the Instrumenthandler will not change
     * @param instrumentId the id
     * @return the instrument for the id
     */
    protected Instrument getInstrument(int instrumentId) {
        return getInstrument(instrumentId, "");
    }

    protected Instrument getInstrument(int instrumentId, String errMsg) {
        var instrument = instrumentDao.getInstrument(instrumentId);
        if(!instrument.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, errMsg + " Instrument for id:"+instrumentId + " not found");
        }
        return instrument.get();
    }

    protected void validateInstrument(Instrument instrument, InstrumentType instrumentType, String errMsg) {
        if(instrument.getInstrumentType()!=instrumentType){
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, errMsg+" instrument has wrong type:"+instrument.getInstrumentType());
        }
    }

    protected void saveProperty(InstrumentPropertyType instrumentPropertyType, String value, LocalDate validFrom, LocalDate validUntil) {
        checkInitStatus();
         instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, value, instrumentPropertyType.getValueType(), validFrom, validUntil));
    }

    protected void saveProperty(InstrumentPropertyType instrumentPropertyType, ValuePerDate value) {
        checkInitStatus();
        instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, String.valueOf(value.getValue()), instrumentPropertyType.getValueType(), value.getDate(), null));
   }

   protected void saveProperty(InstrumentPropertyType instrumentPropertyType, int value) {
        checkInitStatus();
        instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, String.valueOf(value), instrumentPropertyType.getValueType(), null, null));
    }

    protected void savePropertyList(InstrumentPropertyType instrumentPropertyType, List<ValuePerDate> values) {
        for(var value : values) {
            saveProperty(instrumentPropertyType, value);
        } 
    }

    protected void checkInstrumentInactivation(boolean isActiveBeforeUpdate,  boolean isActiveAfterUpdate) {
        // try to deactivate instrument ?
        if(!isActiveAfterUpdate && isActiveBeforeUpdate) {
            validateInstrument4Inactivation();
        }        
    } 

    protected void validateInstrument4Inactivation() {
        throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+instrumentId + " not deactivated. Instruments with type:"+ domainObject.getInstrumentType() + " can not deactivated");
    }

    protected void deleteInstrumentPropertyList() {
        var instrumentProperties = instrumentDao.getInstrumentProperties(instrumentId);
        for (InstrumentProperties instrumentProperty : instrumentProperties) {
            auditService.saveMessage(instrumentDao.deleteInstrumentProperty(instrumentProperty.getPropertyid()),
                Severity.INFO, AUDIT_MSG_TYPE);
        }    
    }

    public void save() {
        checkDomainObjectInitStatus();
        instrumentDao.saveInstrument(domainObject);
        instrumentId = domainObject.getInstrumentid();
        auditService.saveMessage(domainObjectName+" inserted:" + domainObject.getDescription(), Severity.INFO, AUDIT_MSG_TYPE);
    }

    public void updateInstrument(boolean isActive) {
        loadInstrument();
        updateInstrument(domainObject.getDescription(), isActive);
    }

    public void updateInstrument(String description, boolean isActive) {
        loadInstrument();
        checkInstrumentInactivation(domainObject.isIsactive(), isActive);
        oldDesc = domainObject.getDescription();
        instrumentDao.updateInstrument(instrumentId, description, isActive);
    }

    abstract protected void createDomainObject(String description);
    abstract protected void setDomainObjectName();
    abstract protected InstrumentType getInstrumentType();
}