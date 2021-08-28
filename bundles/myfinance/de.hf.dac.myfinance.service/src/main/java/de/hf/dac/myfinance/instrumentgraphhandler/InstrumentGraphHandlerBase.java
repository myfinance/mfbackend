package de.hf.dac.myfinance.instrumentgraphhandler;

import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;

public abstract class InstrumentGraphHandlerBase implements InstrumentGraphHandler{
    final InstrumentDao instrumentDao;

    public InstrumentGraphHandlerBase(final InstrumentDao instrumentDao) {
            this.instrumentDao = instrumentDao;
    }

    @Override
    public void addInstrumentToGraph(final int instrumentId, final int ancestorId, final EdgeType edgeType){
        List<InstrumentGraphEntry> ancestorGraphEntries = instrumentDao.getAncestorGraphEntries(ancestorId, edgeType);
        if(instrumentId!=ancestorId && ancestorGraphEntries.isEmpty()){
            final InstrumentGraphEntry newEntry = new InstrumentGraphEntry(ancestorId, ancestorId, edgeType);
            newEntry.setPathlength(0);
            instrumentDao.saveGraphEntry(newEntry);
            ancestorGraphEntries = instrumentDao.getAncestorGraphEntries(ancestorId, edgeType);
        }
        for (final InstrumentGraphEntry entry : ancestorGraphEntries) {
            final InstrumentGraphEntry newEntry = new InstrumentGraphEntry(entry.getId().getAncestor(), instrumentId, edgeType);
            newEntry.setPathlength(entry.getPathlength()+1);
            instrumentDao.saveGraphEntry(newEntry);
        }
        final InstrumentGraphEntry newEntry = new InstrumentGraphEntry(instrumentId, instrumentId, edgeType);
        newEntry.setPathlength(0);
        instrumentDao.saveGraphEntry(newEntry);
    }

    @Override
    public Optional<Integer> getRootInstrument(final int instrumentId, final EdgeType edgeType) {
        return instrumentDao.getRootInstrument(instrumentId, edgeType);
    }

    @Override
    public List<Instrument> getInstrumentChilds(final int instrumentId, final EdgeType edgeType, final int pathlength){
        List<Instrument>  childs = instrumentDao.getInstrumentChilds(instrumentId, edgeType, pathlength);
        if(childs==null) {
            childs = new ArrayList<>();
        }
        return childs;
    }

    @Override
    public List<Instrument> getAllInstrumentChilds(final int instrumentId, final EdgeType edgeType){
        List<Instrument>  childs = instrumentDao.getInstrumentChilds(instrumentId, edgeType);
        if(childs==null) {
            childs = new ArrayList<>();
        }
        return childs;
    }

    protected List<Instrument> getAllInstrumentChilds(final int instrumentId, final EdgeType edgeType, final boolean filterInstrumentType, final InstrumentType instrumentType, final boolean onlyActive){
        List<Instrument> instruments = getAllInstrumentChilds(instrumentId, edgeType);
        if ( instruments!= null && !instruments.isEmpty()) {
            instruments = instruments.stream().filter(i->(
                    (!filterInstrumentType || i.getInstrumentType().equals(instrumentType))
                    && (!onlyActive || i.isIsactive())
                )
            ).collect(Collectors.toList());
        }
        return instruments;
    }

    @Override
    public int getAncestorId(final int instrumentId, final EdgeType edgeType) {
        final var ancestors = instrumentDao.getAncestorGraphEntries(instrumentId, edgeType);
        if(ancestors == null || ancestors.isEmpty()) {
            throw new MFException(MFMsgKey.ANCESTOR_DOES_NOT_EXIST_EXCEPTION, "no ancestor for id:"+instrumentId);
        }
        return ancestors.stream().findFirst().get().getId().getAncestor();
    }
}