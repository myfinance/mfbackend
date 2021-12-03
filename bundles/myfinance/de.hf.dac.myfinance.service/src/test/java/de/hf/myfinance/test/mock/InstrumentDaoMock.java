package de.hf.myfinance.test.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.SecuritySymbols;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class InstrumentDaoMock implements InstrumentDao {

    List<InstrumentGraphEntry> instrumentGraphEntries = new ArrayList<InstrumentGraphEntry>();
    Map<Integer, Instrument>  instruments = new HashMap<Integer, Instrument>();
    int maxId = 0;

    public InstrumentDaoMock() {

    }

    @Override
    public List<Instrument> listInstruments() {
        return new ArrayList<Instrument>(instruments.values()); 
    }

    @Override
    public Optional<Instrument> getEquity(String isin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Instrument> getSecurity(String businesskey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> getSecurities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Instrument> getCurrency(String currencyCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Instrument> getInstrument(int instrumentId) {
        return Optional.of(instruments.get(instrumentId));
    }

    @Override
    public void saveInstrument(Instrument instrument) {
        if(instrument.getInstrumentid()==null ) {
            maxId++;
            instrument.setInstrumentid(maxId);
        }
        instruments.put(instrument.getInstrumentid(), instrument);
    }

    @Override
    public void updateInstrument(int instrumentId, String description, boolean isActive) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveSymbol(SecuritySymbols symbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public String deleteSymbols(int symbolId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType) {
        return instrumentGraphEntries.stream().filter(i->i.getId().getDescendant()==instrumentId && i.getId().getEdgetype().equals(edgeType)).collect(Collectors.toList());
    }

    @Override
    public Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveGraphEntry(InstrumentGraphEntry instrumentGraphEntry) {
        instrumentGraphEntries.add(instrumentGraphEntry);
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType) {
        var childs = instrumentGraphEntries.stream().filter(i->i.getId().getAncestor()==instrumentId && i.getId().getEdgetype().equals(edgeType) && i.getPathlength()>0).collect(Collectors.toList());
        var childIds = new ArrayList<Integer>();
        childs.forEach(i->childIds.add(i.getId().getDescendant()));
        return instruments.values().stream().filter(i->childIds.contains(i.getInstrumentid())).collect(Collectors.toList());
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength) {
        var childs = instrumentGraphEntries.stream().filter(i->i.getId().getAncestor()==instrumentId && i.getId().getEdgetype().equals(edgeType) && i.getPathlength()==pathlength).collect(Collectors.toList());
        var childIds = new ArrayList<Integer>();
        childs.forEach(i->childIds.add(i.getId().getDescendant()));
        return instruments.values().stream().filter(i->childIds.contains(i.getInstrumentid())).collect(Collectors.toList());
    }

    @Override
    public Optional<Instrument> getAccountPortfolio(int tenantId) {
        var  childs = getInstrumentChilds(tenantId, EdgeType.TENANTGRAPH, 1);
        return childs.stream().filter(i->i.getInstrumentType().equals(InstrumentType.ACCOUNTPORTFOLIO)).findFirst();
    }

    @Override
    public Optional<Instrument> getBudgetPortfolio(int tenantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveInstrumentProperty(InstrumentProperties instrumentProperty) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<InstrumentProperties> getInstrumentProperties(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String deleteInstrumentProperty(int instrumentPropertyId) {
        // TODO Auto-generated method stub
        return null;
    }

}