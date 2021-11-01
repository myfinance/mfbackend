package de.hf.dac.myfinance.instrumenthandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

/**
 * This abstract class is the base for all Instruments a Tenant can be directly connected with and the Tenant it self.
 * Securities like Equities  and Bonds are only connected via Trades and so use a different base class
 */
public abstract class AbsAccountableInstrumentHandler extends AbsInstrumentHandler{
    protected final InstrumentGraphHandler instrumentGraphHandler;
    private int parentId;

    protected AbsAccountableInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int parentId) {
        super(instrumentDao, auditService);
        this.instrumentGraphHandler = new InstrumentGraphHandlerImpl(this.instrumentDao);
        createDomainObject(description);
        setParent(parentId);
        validateParent();
    }

    protected AbsAccountableInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId, boolean addToAccountPf) {
        this(instrumentDao, auditService, description, tenantId);
        setParentToAccountPf();
    }

    protected AbsAccountableInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
        this.instrumentGraphHandler = new InstrumentGraphHandlerImpl(this.instrumentDao);
    }

    protected AbsAccountableInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument instrument) {
        super(instrumentDao, auditService, instrument);
        this.instrumentGraphHandler = new InstrumentGraphHandlerImpl(this.instrumentDao);
    }

    public void save() {
        super.save();
        updateParent();
        instrumentGraphHandler.addInstrumentToGraph(instrumentId, parentId);
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

    public Optional<Integer> getTenant() {
        checkInitStatus();
        return instrumentGraphHandler.getRootInstrument(instrumentId, EdgeType.TENANTGRAPH);
    }

    public List<Instrument> getInstrumentChilds(EdgeType edgeType, int pathlength){
        checkInitStatus();
        return instrumentGraphHandler.getInstrumentChilds(instrumentId, edgeType, pathlength);
    }

    public List<Integer> getAncestorIds() {
        checkInitStatus();
        var ids = new ArrayList<Integer>();
        final List<InstrumentGraphEntry> ancestorGraphEntries = instrumentDao.getAncestorGraphEntries(instrumentId, EdgeType.TENANTGRAPH);
        if (ancestorGraphEntries != null && !ancestorGraphEntries.isEmpty()) {
            for (final InstrumentGraphEntry entry : ancestorGraphEntries) {
                ids.add(entry.getId().getAncestor());
            }
        }                
        return ids;
    }

    @Override
    protected void validateInstrument(Instrument instrument, InstrumentType instrumentType, String errMsg) {
        super.validateInstrument(instrument, instrumentType, errMsg);
        Optional<Integer> tenantOfInstrument = instrumentGraphHandler.getRootInstrument(instrument.getInstrumentid(), EdgeType.TENANTGRAPH);
        if(!tenantOfInstrument.isPresent()
            || !tenantOfInstrument.get().equals(getTenant().get())){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION,  errMsg+" instrument has not the same tenant");
        }
    }
}