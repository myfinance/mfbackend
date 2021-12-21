/** ----------------------------------------------------------------------------
 *
 * ---                              ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : InstrumentServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.service;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.*;
import de.hf.dac.myfinance.api.persistence.dao.*;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueService;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.DepotHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.RealEstateHandler;
import de.hf.dac.myfinance.instrumenthandler.securityhandler.EquityHandler;
import lombok.Data;

import javax.inject.Inject;
import java.util.*;

@Data
public class InstrumentServiceImpl implements InstrumentService {

    private InstrumentFactory instrumentFactory;


    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao, RecurrentTransactionDao recurrentTransactionDao, AuditService auditService, ValueService valueService){
        this.instrumentFactory = new InstrumentFactory(instrumentDao, auditService, valueService, recurrentTransactionDao);
    }

    @Override
    public List<Instrument> listInstruments() {
        return instrumentFactory.listInstruments();
    }

    @Override
    public List<Instrument> listInstruments(int tenantId) {
        return instrumentFactory.getTenantHandler(tenantId, false).listInstruments();
    }

    @Override
    public List<Instrument> listInstruments(int tenantId, boolean onlyActive) {
        return instrumentFactory.getTenantHandler(tenantId, false).listInstruments(onlyActive);
    }

    @Override
    public List<Instrument> listInstruments(int tenantId, InstrumentType instrumentType, boolean onlyActive) {
        return instrumentFactory.getTenantHandler(tenantId, false).listInstruments(instrumentType, onlyActive);
    }

    @Override
    public Instrument getInstrument(int instrumentId) {
        return instrumentFactory.getBaseInstrumentHandler(instrumentId).getInstrument();
    }

    @Override
    public Instrument getInstrument(int instrumentId, String errMsg) {
        return instrumentFactory.getBaseInstrumentHandler(instrumentId).getInstrument(errMsg);
    }


    @Override
    public Optional<Integer> getTenant(int instrumentId) {
        return instrumentFactory.getBaseInstrumentHandler(instrumentId).getTenant();
    }

    @Override
    public List<Integer> getParentIds(int instrumentId) {
        return instrumentFactory.getBaseInstrumentHandler(instrumentId).getAncestorIds();
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength){
        return instrumentFactory.getBaseInstrumentHandler(instrumentId).getInstrumentChilds(edgeType, pathlength);
    }

    @Override
    public List<Instrument> listAccounts(int tenantId) {
        return instrumentFactory.getTenantHandler(tenantId, false).getAccounts();
    }

    @Override
    public List<Instrument> listTenants(){
        return instrumentFactory.listTenants();
    }

    @Override
    public Instrument getIncomeBudget(int budgetGroupId) {
        return instrumentFactory.getBudgetGroupHandler(budgetGroupId, false).getIncomeBudget();
    }

    @Override
    public List<InstrumentProperties> getInstrumentProperties(int instrumentId) {
        return instrumentFactory.getBaseInstrumentHandler(instrumentId).getInstrumentProperties();
    }

    @Override
    public void newTenant(String description) {
        var tenantHandler = instrumentFactory.getInstrumentHandler(InstrumentType.TENANT, description, -1, null);
        tenantHandler.save();
    }

    @Override
    public void saveEquity(String theisin, String description) {
        var equityHandler = instrumentFactory.getInstrumentHandler(InstrumentType.EQUITY, description, -1, theisin);
        equityHandler.save();
    }

    @Override
    public void saveFullEquity(String theisin, String description, List<String[]> symbols) {
        var equityHandler = (EquityHandler)instrumentFactory.getInstrumentHandler(InstrumentType.EQUITY, description, -1, theisin);
        equityHandler.saveFullEquity(symbols);
    }

    @Override
    public Optional<Instrument> getCurrency(String currencyCode){
        var currencyHandler = instrumentFactory.getInstrumentHandler(InstrumentType.CURRENCY, "", -1, currencyCode);
        return currencyHandler.getSavedDomainObject();
    }

    @Override
    public Optional<Instrument> getEquity(String isin){
        var equityHandler = instrumentFactory.getInstrumentHandler(InstrumentType.EQUITY, "", -1, isin);
        return equityHandler.getSavedDomainObject();
    }

    @Override
    public Instrument getSecurity(String businesskey){
        return getSecurity(businesskey, "");
    }

    @Override
    public Instrument getSecurity(String isin, String errMsg) {
        return instrumentFactory.getBaseSecurityHandler().getSecurity(isin, errMsg);
    }

    @Override
    public List<Instrument> getSecurities(){
        return instrumentFactory.getBaseSecurityHandler().getSecurities();
    }

    @Override
    public void saveSymbol(String theisin, String thesymbol, String thecurrencyCode){
        var equityHandler = (EquityHandler)instrumentFactory.getInstrumentHandler(InstrumentType.EQUITY, "", -1, theisin);
        equityHandler.saveSymbol(thesymbol, thecurrencyCode);
    }

    @Override
    public void deleteSymbols(String theisin){
        var equityHandler = (EquityHandler)instrumentFactory.getInstrumentHandler(InstrumentType.EQUITY, "", -1, theisin);
        equityHandler.deleteSymbols();
    }

    @Override
    public void saveCurrency(String currencyCode, String description) {
        var currencyHandler = instrumentFactory.getInstrumentHandler(InstrumentType.CURRENCY, description, -1, currencyCode);
        currencyHandler.save();
    }

    @Override
    public void newBudget(String description, int budgetGroupId) {
        var budgetHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGET, description, budgetGroupId, description);
        budgetHandler.save();
     }

    @Override
    public void newGiroAccount(String description, int tenantId) {
        var giroHandler = instrumentFactory.getInstrumentHandler(InstrumentType.GIRO, description, tenantId, description);
        giroHandler.save();
    }

    @Override
    public void newDepotAccount(String description, int tenantId, int defaultGiroId, int valueBudgetId) {
        DepotHandler depotHandler = (DepotHandler)instrumentFactory.getInstrumentHandler(InstrumentType.DEPOT, description, tenantId, description);
        depotHandler.initAdditionalFields(defaultGiroId, valueBudgetId);
        depotHandler.save();
    }

    @Override
    public void updateDepotAccount(int instrumentId, String description, boolean isActive, int defaultGiroId) {
        var depotHandler = (DepotHandler)instrumentFactory.getDepotHandler(instrumentId, true);
        depotHandler.setDefaultGiroId(defaultGiroId);
        depotHandler.setActive(isActive);
        depotHandler.setDescription(description);
        depotHandler.save();
    }

    @Override
    public void newRealEstate(String description, int tenantId, int valueBudgetId, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits) {
        var realEstateHandler = (RealEstateHandler)instrumentFactory.getInstrumentHandler(InstrumentType.REALESTATE, description, tenantId, description);
        realEstateHandler.initAdditionalFields(valueBudgetId, yieldgoals, realEstateProfits);
        realEstateHandler.save();
    }

    @Override
    public void updateRealEstate(int instrumentId, String description, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits, boolean isActive) {
        var realEstateHandler = instrumentFactory.getRealEstateHandler(instrumentId, true);
        realEstateHandler.initAdditionalFields(yieldgoals, realEstateProfits);
        realEstateHandler.setActive(isActive);
        realEstateHandler.setDescription(description);
        realEstateHandler.save();
    }

    @Override
    public void updateInstrument(int instrumentId, String description, boolean isActive) {
        var instrumentHandler = instrumentFactory.getInstrumentHandler(instrumentId);
        instrumentHandler.setActive(isActive);
        instrumentHandler.setDescription(description);
        instrumentHandler.save();
    } 
}
 