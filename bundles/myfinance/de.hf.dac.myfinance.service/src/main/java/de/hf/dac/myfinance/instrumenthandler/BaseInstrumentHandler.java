package de.hf.dac.myfinance.instrumenthandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class BaseInstrumentHandler {
    protected final InstrumentDao instrumentDao;
    protected final InstrumentGraphHandler instrumentGraphHandler;
    protected int instrumentId;
    protected boolean initialized = false;
    protected Instrument domainObject;
    private List<InstrumentProperties> properties;
    protected boolean isPropertyInit = false;

    public BaseInstrumentHandler(InstrumentDao instrumentDao, int instrumentId) {
        this(instrumentDao);
        setInstrumentId(instrumentId);
    }

    protected BaseInstrumentHandler(InstrumentDao instrumentDao) {
        this.instrumentDao = instrumentDao;
        this.instrumentGraphHandler = new InstrumentGraphHandlerImpl(this.instrumentDao);
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
        Optional<Integer> tenantOfGiro = instrumentGraphHandler.getRootInstrument(instrument.getInstrumentid(), EdgeType.TENANTGRAPH);
        if(!tenantOfGiro.isPresent()
            || !tenantOfGiro.get().equals(getTenant().get())){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION,  errMsg+" instrument has not the same tenant");
        }
    }
}