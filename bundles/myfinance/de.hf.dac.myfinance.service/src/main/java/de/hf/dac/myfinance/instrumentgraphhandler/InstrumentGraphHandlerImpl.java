package de.hf.dac.myfinance.instrumentgraphhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    public List<Instrument> getInstrumentChilds(int instrumentId, int pathlength){
        return getInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, pathlength);
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId, final boolean onlyActive) {
        return getAllInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, onlyActive);
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId, final EdgeType edgeType, final boolean onlyActive){
        return getAllInstrumentChilds(instrumentId, edgeType, false, InstrumentType.UNKNOWN, onlyActive);
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId, final InstrumentType instrumentType, final boolean onlyActive){
        return getAllInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, false, instrumentType, onlyActive);
    }
    
    @Override
    public List<Instrument> getAllInstrumentChildsWithType(final int instrumentId, final InstrumentType instrumentType){
        return getAllInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, true, instrumentType, false);
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId){
        return getAllInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH);
    }

    @Override
    public Stream<Instrument> getFirstLevelChildsPerType(final int instrumentId, final InstrumentType instrumentType) {
        return getInstrumentFirstLevelChilds(instrumentId).stream().filter(i -> i.getInstrumentType().equals(instrumentType));
    }

    @Override
    public Optional<Instrument> getFirstLevelChildsPerTypeFirstmatch(final int instrumentId, final InstrumentType instrumentType) {
        return getFirstLevelChildsPerType(instrumentId, instrumentType).findFirst();
    }

    @Override
    public Optional<Instrument> getAccountPortfolio(final int tenantId) {
        return getFirstLevelChildsPerTypeFirstmatch(tenantId, InstrumentType.ACCOUNTPORTFOLIO);
    }

    @Override
    public List<Instrument> getAccounts(final int tenantId) {
        final Optional<Instrument> accPF = getAccountPortfolio(tenantId);
        if(!accPF.isPresent()) {
            return new ArrayList<>();
        }
        return getInstrumentFirstLevelChilds(accPF.get().getInstrumentid());
    }
    
    @Override
    public int getAncestorId(final int instrumentId) {
        return getAncestorId(instrumentId, EdgeType.TENANTGRAPH);
    }

}