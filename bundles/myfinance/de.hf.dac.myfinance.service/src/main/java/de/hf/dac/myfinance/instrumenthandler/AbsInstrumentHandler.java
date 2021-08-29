package de.hf.dac.myfinance.instrumenthandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public abstract class AbsInstrumentHandler {
    protected final InstrumentDao instrumentDao;
    protected final InstrumentGraphHandler instrumentGraphHandler;
    protected final int instrumentId;
    protected Instrument instrument;

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, int instrumentId) {
        this.instrumentDao = instrumentDao;
        this.instrumentGraphHandler = new InstrumentGraphHandlerImpl(this.instrumentDao);
        this.instrumentId = instrumentId;
    }

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, Instrument instrument) {
        this.instrumentDao = instrumentDao;
        this.instrumentGraphHandler = new InstrumentGraphHandlerImpl(this.instrumentDao);
        this.instrument = instrument;
        this.instrumentId = instrument.getInstrumentid();
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
}