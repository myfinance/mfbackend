package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.SecuritySymbols;
import de.hf.dac.myfinance.api.domain.Source;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class InstrumentDaoTestImpl implements InstrumentDao {

    @Override
    public List<Instrument> listInstruments() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Equity> getEquity(String isin) {
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
        Instrument instrument = new Giro();
        return Optional.of(instrument);
    }

    @Override
    public Optional<Source> getSource(int sourceId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Source> getActiveSources() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<LocalDate, List<Cashflow>> getInstrumentCashflowMap(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Cashflow> listInstrumentCashflows(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveInstrument(Instrument instrument) {
        // TODO Auto-generated method stub

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
    public List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveGraphEntry(InstrumentGraphEntry instrumentGraphEntry) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Instrument> getAccountPortfolio(int tenantId) {
        // TODO Auto-generated method stub
        return null;
    }

    
}