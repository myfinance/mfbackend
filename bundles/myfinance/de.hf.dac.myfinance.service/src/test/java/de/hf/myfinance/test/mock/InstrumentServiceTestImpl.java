package de.hf.myfinance.test.mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.Budget;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
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
    public void newTenant(String description) {
        // TODO Auto-generated method stub
    }

    @Override
    public void updateInstrument(int instrumentId, String description, boolean isActive) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newBudget(String description, int budgetGroupId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newGiroAccount(String description, int tenantId) {
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

    @Override
    public Instrument getInstrument(int instrumentId) {
        Instrument instrument = new Giro("testgiro", true, LocalDateTime.now());
        instrument.setInstrumentid(instrumentId);
        return instrument;
    }

    @Override
    public Instrument getInstrument(int instrumentId, String errMsg) {
        switch (instrumentId) {
            case 1:
                Instrument giro = new Giro("testgiro", true, LocalDateTime.now());
                giro.setInstrumentid(instrumentId);
                return giro;
            case 2:
                Instrument budget = new Budget("testbudget", true, LocalDateTime.now());        
                budget.setInstrumentid(instrumentId);
                return budget;    
            case 3:
                Instrument giro2 = new Giro("testgiro2", true, LocalDateTime.now());
                giro2.setInstrumentid(instrumentId);
                return giro2; 
            case 4:
                Instrument budget2 = new Budget("testbudget2", true, LocalDateTime.now());        
                budget2.setInstrumentid(instrumentId);
                return budget2;                                      
            default:
                break;
        }
        return null;
    }

    @Override
    public void newRealEstate(String description, int tenantId, int valueBudgetId,
            List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateRealEstate(int instrumentId, String description, List<ValuePerDate> yieldgoals,
            List<ValuePerDate> realEstateProfits, boolean isActive) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<InstrumentProperties> getInstrumentProperties(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteSymbols(String theisin) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveFullEquity(String theisin, String description, List<String[]> symbols) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newDepotAccount(String description, int tenantId, int defaultGiroId, int valueBudgetId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Instrument getSecurity(String isin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Instrument getSecurity(String isin, String errMsg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateDepotAccount(int instrumentId, String description, boolean isActive, int defaultGiroId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<Integer> getTenant(int instrumentId) {
        return Optional.of(1);
    }

    @Override
    public List<Integer> getParentIds(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}