package de.hf.dac.myfinance.instrumenthandler;

import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public abstract class AbsSimpleInstrumentHandler extends AbsInstrumentHandler {

    protected Instrument domainObject;
    private int parentId;
    private static final String AUDIT_MSG_TYPE="AbsSimpleInstrument_User_Event";
    protected String domainObjectName;

    
    public AbsSimpleInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int parentId) {
        super(instrumentDao, auditService);
        createDomainObject(description);
        setParent(parentId);
        validateParent();
    }

    public AbsSimpleInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId, boolean addToAccountPf) {
        this(instrumentDao, auditService, description, tenantId);
        setParentToAccountPf();
    }

    public AbsSimpleInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService);
        setInstrumentId(instrumentId);
    }

    public AbsSimpleInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument instrument) {
        super(instrumentDao, auditService);
        setInstrumentId(instrument.getInstrumentid());
        if (!instrument.getInstrumentType().equals(getInstrumentType())) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create "+domainObjectName+" for instrumentType:"+instrument.getInstrumentType());
        }
        this.domainObject = instrument;
    }

    
    @Override
    public void save() {
        instrumentDao.saveInstrument(domainObject);
        instrumentId = domainObject.getInstrumentid();
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

        validateInstrument4Inactivation(instrument.getInstrumentid(), instrument.getInstrumentType(), instrument.isIsactive(), isActive);
        String oldDesc = instrument.getDescription();
        instrumentDao.updateInstrument(instrumentId, description, isActive);
        if(instrument.getInstrumentType()==InstrumentType.TENANT) {
            List<Instrument> instruments = instrumentGraphHandler.getAllInstrumentChilds(instrumentId);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_BUDGETGROUP_PREFIX, instruments);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_ACCPF_PREFIX, instruments);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_INCOMEBUDGET_PREFIX, instruments);
        }
    }
}