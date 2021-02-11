package de.hf.myfinance.test.mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.service.InstrumentService;

public class InstrumentServiceTestImpl implements InstrumentService {

    @Override
    public List<Instrument> listInstruments() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> listInstruments(int tenantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> listInstruments(int tenantId, boolean onlyActive) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> listInstruments(int tenantId, InstrumentType instrumentType, boolean onlyActive) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> listAccounts(int tenantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> listTenants() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Instrument> getInstrument(int instrumentId) {
        Instrument instrument = new Giro("testgiro", true, LocalDateTime.now());
        instrument.setInstrumentid(instrumentId);
        return Optional.of(instrument);
    }

    @Override
    public Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Instrument getIncomeBudget(int tenantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void newTenant(String description, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateInstrument(int instrumentId, String description, boolean isActive) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newBudget(String description, int budgetGroupId, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newGiroAccount(String description, int tenantId, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<Instrument> getCurrency(String currencyCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Equity> getEquity(String isin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Instrument> getSecurities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveEquity(String isin, String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveSymbol(String isin, String symbol, String currencyCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveCurrency(String currencyCode, String description) {
        // TODO Auto-generated method stub

    }
    
}