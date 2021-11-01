package de.hf.dac.myfinance.instrumenthandler;

import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class InstrumentGraphHandlerImpl extends InstrumentGraphHandlerBase{

    public InstrumentGraphHandlerImpl(final InstrumentDao instrumentDao) {
        super(instrumentDao);
    }

    @Override
    public void addInstrumentToGraph(int instrumentId, int ancestorId){
        addInstrumentToGraph(instrumentId, ancestorId, EdgeType.TENANTGRAPH);
    }

    @Override
    public Optional<Integer> getRootInstrument(int instrumentId) {
        return getRootInstrument(instrumentId, EdgeType.TENANTGRAPH);
    }

    @Override
    public List<Instrument> getInstrumentFirstLevelChilds(int instrumentId){
        return getInstrumentFirstLevelChilds(instrumentId, EdgeType.TENANTGRAPH);
    }

    @Override
    public List<Instrument> getInstrumentFirstLevelChilds(int instrumentId, EdgeType edgeType){
        return getInstrumentChilds(instrumentId, edgeType, 1);
    }

    @Override
    public List<Instrument> getInstrumentFirstLevelChildsWithType(int instrumentId, final InstrumentType instrumentType, final boolean onlyActive){
        return filterInstruments(getInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, 1), true, instrumentType, onlyActive);
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, int pathlength){
        return getInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, pathlength);
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId, final boolean onlyActive) {
        return getAllInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, onlyActive);
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId, final EdgeType edgeType, final boolean onlyActive){
        return filterInstruments(getAllInstrumentChilds(instrumentId, edgeType), false, InstrumentType.UNKNOWN, onlyActive);
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId, final InstrumentType instrumentType, final boolean onlyActive){
        return filterInstruments(getAllInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH), false, instrumentType, onlyActive);
    }
    
    @Override
    public List<Instrument> getAllInstrumentChildsWithType(final int instrumentId, final InstrumentType instrumentType){
        return filterInstruments(getAllInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH), true, instrumentType, false);
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId){
        return getAllInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH);
    }

    @Override
    public Instrument getFirstLevelChildsPerTypeFirstmatch(final int instrumentId, final InstrumentType instrumentType) {
        var instruments = getInstrumentFirstLevelChildsWithType(instrumentId, instrumentType, true);
        if(instruments == null || instruments.isEmpty()) return null;
        return instruments.get(0);
    }
    
    @Override
    public int getAncestorId(final int instrumentId) {
        return getAncestorId(instrumentId, EdgeType.TENANTGRAPH);
    }

}