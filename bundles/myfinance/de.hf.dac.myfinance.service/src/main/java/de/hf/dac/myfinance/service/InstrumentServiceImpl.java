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
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.api.io.web.WebRequestService;
import de.hf.dac.myfinance.api.domain.*;
import de.hf.dac.myfinance.api.domain.Currency;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.*;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveService;
import de.hf.dac.myfinance.instrumenthandler.DepotHandler;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.instrumenthandler.RealEstateHandler;
import lombok.Data;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;

@Data
public class InstrumentServiceImpl implements InstrumentService {

    private InstrumentDao instrumentDao;
    private AuditService auditService;
    private InstrumentFactory instrumentFactory;
    private static final String AUDIT_MSG_TYPE="InstrumentService_User_Event";


    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao, RecurrentTransactionDao recurrentTransactionDao, WebRequestService webRequestService, AuditService auditService, ValueCurveService valueService){
        this.instrumentDao = instrumentDao;
        this.auditService = auditService;
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
        var tenantHandler = instrumentFactory.getInstrumentHandler(InstrumentType.TENANT, description, -1);
        tenantHandler.save();
    }

    @Override
    public void saveEquity(String theisin, String description) {
        String isin = theisin.toUpperCase();
        Optional<Equity> existingSec = getEquity(isin);
        if(!existingSec.isPresent()) {
            Equity equity = new Equity(isin, description, true, LocalDateTime.now());
            auditService.saveMessage("Equity inserted:" + theisin, Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.saveInstrument(equity);
        } else {
            existingSec.get().setDescription(description);
            auditService.saveMessage("Equity " + theisin +" updated with new description: " + description, Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.updateInstrument(existingSec.get().getInstrumentid(), description, true);
        }
    }

    @Override
    public void saveFullEquity(String theisin, String description, List<String[]> symbols) {
        saveEquity(theisin, description);
        deleteSymbols(theisin);
        for (String[] symbol : symbols) {
            saveSymbol(theisin, symbol[0], symbol[1]);
        }
    }

    @Override
    public Optional<Instrument> getCurrency(String currencyCode){
        return instrumentDao.getCurrency(currencyCode);
    }

    @Override
    public Optional<Equity> getEquity(String isin){
        return instrumentDao.getEquity(isin);
    }

    @Override
    public Instrument getSecurity(String businesskey){
        return getSecurity(businesskey, "");
    }

    @Override
    public Instrument getSecurity(String isin, String errMsg) {
        var instrument = instrumentDao.getSecurity(isin);
        if(!instrument.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, errMsg + " Instrument for isin:"+isin + " not found");
        }
        return instrument.get();
    }

    @Override
    public List<Instrument> getSecurities(){
        return instrumentDao.getSecurities();
    }

    @Override
    public void saveSymbol(String theisin, String thesymbol, String thecurrencyCode){

        String isin = theisin.toUpperCase();
        String symbol = thesymbol.toUpperCase();
        String currencyCode = thecurrencyCode.toUpperCase().trim();

        Optional<Instrument> currency = getCurrency(currencyCode);
        if(!currency.isPresent()) {
            throw new MFException(MFMsgKey.UNKNOWN_CURRENCY_EXCEPTION, "Symbol not saved: unknown currency:"+currencyCode);
        }
        Optional<Equity> existingSec = getEquity(isin);
        if(!existingSec.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Symbol not saved: unknown security:"+isin);
        }
        Set<SecuritySymbols> symbols = existingSec.get().getSymbols();
        SecuritySymbols newSymbol = new SecuritySymbols(currency.get(), existingSec.get().getInstrumentid(), symbol);
        if(symbols!=null && !symbols.isEmpty()){
            Optional<SecuritySymbols> existingSymbol = symbols.stream().filter(i->i.getSymbol().equals(symbol)).findFirst();
            if(existingSymbol.isPresent()) {
                newSymbol = existingSymbol.get();
                newSymbol.setCurrency(currency.get());
                auditService.saveMessage("Symbol " + thesymbol +" updated with new currency: " + thecurrencyCode, Severity.INFO, AUDIT_MSG_TYPE);
            }
            else {
                auditService.saveMessage("Symbol with currency "+thecurrencyCode+" inserted:" + thesymbol, Severity.INFO, AUDIT_MSG_TYPE);
            }
        }
        instrumentDao.saveSymbol(newSymbol);
    }

    @Override
    public void deleteSymbols(String theisin){

        String isin = theisin.toUpperCase();

        Optional<Equity> existingSec = getEquity(isin);
        if(!existingSec.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Symbols not deleted: unknown security:"+isin);
        }
        Set<SecuritySymbols> symbols = existingSec.get().getSymbols();
        for (SecuritySymbols securitySymbol : symbols) {
            auditService.saveMessage(instrumentDao.deleteSymbols(securitySymbol.getSymbolid()), Severity.INFO, AUDIT_MSG_TYPE);
        }
    }

    @Override
    public void saveCurrency(String currencyCode, String description) {
        String curCode = currencyCode.toUpperCase();
        Optional<Instrument> existingCur = getCurrency(curCode);
        if(!existingCur.isPresent()) {
            Instrument currency = new Currency(currencyCode, description, true, LocalDateTime.now());
            auditService.saveMessage("Currency inserted:" + currencyCode, Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.saveInstrument(currency);
        } else {
            auditService.saveMessage("Currency " + currencyCode +" updated with new description: " + description, Severity.INFO, AUDIT_MSG_TYPE);
            existingCur.get().setDescription(description);
        }
    }

    @Override
    public void newBudget(String description, int budgetGroupId) {
        var budgetHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGET, description, budgetGroupId);
        budgetHandler.save();
     }

    @Override
    public void newGiroAccount(String description, int tenantId) {
        var giroHandler = instrumentFactory.getInstrumentHandler(InstrumentType.GIRO, description, tenantId);
        giroHandler.save();
    }

    @Override
    public void newDepotAccount(String description, int tenantId, int defaultGiroId, int valueBudgetId) {
        DepotHandler depotHandler = (DepotHandler)instrumentFactory.getInstrumentHandler(InstrumentType.DEPOT, description, tenantId);
        depotHandler.initAdditionalFields(defaultGiroId, valueBudgetId);
        depotHandler.save();
    }

    @Override
    public void updateDepotAccount(int instrumentId, String description, boolean isActive, int defaultGiroId) {
        var depotHandler = (DepotHandler)instrumentFactory.getDepotHandler(instrumentId, true);
        depotHandler.setDefaultGiroId(defaultGiroId);
        depotHandler.updateInstrument(description, isActive);
    }

    @Override
    public void newRealEstate(String description, int tenantId, int valueBudgetId, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits) {
        var realEstateHandler = (RealEstateHandler)instrumentFactory.getInstrumentHandler(InstrumentType.REALESTATE, description, tenantId);
        realEstateHandler.initAdditionalFields(valueBudgetId, yieldgoals, realEstateProfits);
        realEstateHandler.save();
    }

    @Override
    public void updateRealEstate(int instrumentId, String description, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits, boolean isActive) {
        var realEstateHandler = instrumentFactory.getRealEstateHandler(instrumentId, true);
        realEstateHandler.initAdditionalFields(-1, yieldgoals, realEstateProfits);
        realEstateHandler.updateInstrument(description, isActive);
    }

    @Override
    public void updateInstrument(int instrumentId, String description, boolean isActive) {
        var instrumentHandler = instrumentFactory.getInstrumentHandler(instrumentId);
        instrumentHandler.updateInstrument(description, isActive);
    } 
}
