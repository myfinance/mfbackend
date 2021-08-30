package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public abstract class AbsInstrumentHandler {
    protected final InstrumentDao instrumentDao;
    protected final InstrumentGraphHandler instrumentGraphHandler;
    protected final int instrumentId;

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, int instrumentId) {
        this.instrumentDao = instrumentDao;
        this.instrumentGraphHandler = new InstrumentGraphHandlerImpl(this.instrumentDao);
        this.instrumentId = instrumentId;
    }



    public Optional<Integer> getTenant() {
        return instrumentGraphHandler.getRootInstrument(instrumentId, EdgeType.TENANTGRAPH);
    }

    public List<Integer> getAncestorIds() {
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
        return instrumentGraphHandler.getInstrumentChilds(instrumentId, edgeType, pathlength);
    }

    protected void saveProperty(InstrumentPropertyType instrumentPropertyType, String value, LocalDate validFrom, LocalDate validUntil) {
         instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, value, instrumentPropertyType.getValueType(), validFrom, validUntil));
    }

    protected void saveProperty(InstrumentPropertyType instrumentPropertyType, ValuePerDate value) {
        instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, String.valueOf(value.getValue()), instrumentPropertyType.getValueType(), value.getDate(), null));
   }

    protected void savePropertyList(InstrumentPropertyType instrumentPropertyType, List<ValuePerDate> values) {
        for(var value : values) {
            saveProperty(instrumentPropertyType, value);
        } 
    }

    public List<InstrumentProperties> getInstrumentProperties() {
        return instrumentDao.getInstrumentProperties(instrumentId);
    }
}