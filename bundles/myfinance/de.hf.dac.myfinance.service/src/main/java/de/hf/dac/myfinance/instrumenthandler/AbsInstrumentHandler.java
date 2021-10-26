package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

public abstract class AbsInstrumentHandler extends BaseInstrumentHandler{
    protected AuditService auditService;
    protected LocalDateTime ts;
    protected Instrument domainObject;
    private int parentId;
    private static final String AUDIT_MSG_TYPE="AbsSimpleInstrument_User_Event";
    protected String domainObjectName;

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int parentId) {
        super(instrumentDao);
        init(auditService);
        createDomainObject(description);
        setParent(parentId);
        validateParent();
    }

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId, boolean addToAccountPf) {
        this(instrumentDao, auditService, description, tenantId);
        setParentToAccountPf();
    }

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao);
        init(auditService);
        setInstrumentId(instrumentId);
    }

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument instrument) {
        super(instrumentDao);
        init(auditService);
        setInstrumentId(instrument.getInstrumentid());
        if (!instrument.getInstrumentType().equals(getInstrumentType())) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create "+domainObjectName+" for instrumentType:"+instrument.getInstrumentType());
        }
        this.domainObject = instrument;
    }

    protected void init(AuditService auditService) {
        this.auditService = auditService;
        ts = LocalDateTime.now();
    }

    protected void checkDomainObjectInitStatus() {
        checkInitStatus();
        if(this.domainObject==null) {
            throw new MFException(MFMsgKey.OBJECT_NOT_INITIALIZED_EXCEPTION, "instrument is not set:");
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

    public void setTreeLastChanged(LocalDateTime ts){
        this.ts = ts;
    }

    public void save() {
        instrumentDao.saveInstrument(domainObject);
        instrumentId = domainObject.getInstrumentid();
        updateParent();
        instrumentGraphHandler.addInstrumentToGraph(instrumentId, parentId);
        auditService.saveMessage(domainObjectName+" inserted:" + domainObject.getDescription(), Severity.INFO, AUDIT_MSG_TYPE);
    }


    private void validateParent() {
        Optional<Instrument> parent = instrumentDao.getInstrument(parentId);
        if(!parent.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_PARENT_EXCEPTION, domainObjectName+" not saved: unknown parent:"+parentId);
        }
        if(parent.get().getInstrumentType() != getParentType()){
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION,  domainObjectName+" not saved: Instrument with Id "+parentId + " has the wrong type");
        }
    }

    protected void setParent(int parentId) {
        this.parentId = parentId;
    }

    /**
     * used to override the parent during the save function. E.G. Tentant sets the parent to himself 
     */
    protected void updateParent() {
    } 

    abstract protected void createDomainObject(String description);
    abstract protected void setDomainObjectName();
    abstract protected InstrumentType getInstrumentType();

    protected InstrumentType getParentType() {
        return InstrumentType.TENANT;
    }

    private void setParentToAccountPf() {
        Optional<Instrument> accportfolio = instrumentDao.getAccountPortfolio(parentId);
        if(!accportfolio.isPresent()) {
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION,  "Account not saved: account portfolio for the tenant:"+parentId+" does not exists");
        }
        this.parentId = accportfolio.get().getInstrumentid();
    }

    public void updateInstrument(String description, boolean isActive) {
        checkDomainObjectInitStatus();
        validateInstrument4Inactivation(instrumentId, domainObject.getInstrumentType(), domainObject.isIsactive(), isActive);
        String oldDesc = domainObject.getDescription();
        instrumentDao.updateInstrument(instrumentId, description, isActive);
        if(domainObject.getInstrumentType()==InstrumentType.TENANT) {
            List<Instrument> instruments = instrumentGraphHandler.getAllInstrumentChilds(instrumentId);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_BUDGETGROUP_PREFIX, instruments);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_ACCPF_PREFIX, instruments);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_INCOMEBUDGET_PREFIX, instruments);
        }
    }

    protected void validateInstrument4Inactivation(int instrumentId, InstrumentType instrumentType, boolean isActiveBeforeUpdate,  boolean isActiveAfterUpdate) {
        // try to deactivate instrument ?
        if(!isActiveAfterUpdate && isActiveBeforeUpdate) {
            validateInstrumentValue4Inactivation(instrumentId, instrumentType);
            validateRecurrentTransactions4InstrumentInactivation(instrumentId, instrumentType);
        }        
    }

    private void validateInstrumentValue4Inactivation(int instrumentId, InstrumentType instrumentType) {
        if( (instrumentType==InstrumentType.GIRO || instrumentType==InstrumentType.BUDGET) 
            && service.getValue(instrumentId, LocalDate.MAX)!=0.0 ){
            throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+instrumentId + " not deactivated. The current value is not 0");
        } else if(instrumentType==InstrumentType.REALESTATE) {
            for(Instrument budgetGroup : getInstrumentChilds(instrumentId, EdgeType.REALESTATEBUDGETGROUP, 1)) {
                validateInstrumentValue4Inactivation(budgetGroup.getInstrumentid(), budgetGroup.getInstrumentType());
                instrumentDao.updateInstrument(budgetGroup.getInstrumentid(), budgetGroup.getDescription(), false);
            }
        } else if (instrumentType==InstrumentType.BUDGETGROUP) {
            for(Instrument budget : getInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, 1)) {
                updateInstrument(budget.getInstrumentid(), budget.getDescription(), false);
            }
        }
    }
}