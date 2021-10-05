package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public abstract class AbsInstrumentHandler {
    protected final InstrumentDao instrumentDao;
    protected final InstrumentGraphHandler instrumentGraphHandler;
    protected AuditService auditService;
    protected int instrumentId;
    protected boolean initialized = false;
    protected LocalDateTime ts;

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService) {
        this.instrumentDao = instrumentDao;
        this.auditService = auditService;
        this.instrumentGraphHandler = new InstrumentGraphHandlerImpl(this.instrumentDao);
        ts = LocalDateTime.now();
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

    public Optional<Integer> getTenant() {
        checkInitStatus();
        return instrumentGraphHandler.getRootInstrument(instrumentId, EdgeType.TENANTGRAPH);
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

    public List<Instrument> getInstrumentChilds(EdgeType edgeType, int pathlength){
        checkInitStatus();
        return instrumentGraphHandler.getInstrumentChilds(instrumentId, edgeType, pathlength);
    }

    protected void saveProperty(InstrumentPropertyType instrumentPropertyType, String value, LocalDate validFrom, LocalDate validUntil) {
        checkInitStatus();
         instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, value, instrumentPropertyType.getValueType(), validFrom, validUntil));
    }

    protected void saveProperty(InstrumentPropertyType instrumentPropertyType, ValuePerDate value) {
        checkInitStatus();
        instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, String.valueOf(value.getValue()), instrumentPropertyType.getValueType(), value.getDate(), null));
   }

    protected void savePropertyList(InstrumentPropertyType instrumentPropertyType, List<ValuePerDate> values) {
        for(var value : values) {
            saveProperty(instrumentPropertyType, value);
        } 
    }

    public List<InstrumentProperties> getInstrumentProperties() {
        checkInitStatus();
        return instrumentDao.getInstrumentProperties(instrumentId);
    }

    public abstract void save();
}