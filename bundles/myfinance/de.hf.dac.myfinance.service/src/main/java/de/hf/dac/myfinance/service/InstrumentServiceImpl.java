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
import lombok.Data;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class InstrumentServiceImpl implements InstrumentService {

    private InstrumentDao instrumentDao;
    private RecurrentTransactionDao recurrentTransactionDao;
    private ValueCurveService service;
    private AuditService auditService;
    private static final String AUDIT_MSG_TYPE="InstrumentService_User_Event";
    private static final String DEFAULT_BUDGETGROUP_PREFIX = "budgetGroup_";
    private static final String DEFAULT_INCOMEBUDGET_PREFIX = "incomeBudget_";
    private static final String DEFAULT_ACCPF_PREFIX = "accountPf_";
    private static final String DEFAULT_BUDGETPF_PREFIX = "budgetPf_";

    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao, RecurrentTransactionDao recurrentTransactionDao, WebRequestService webRequestService, AuditService auditService, ValueCurveService service ){
        this.instrumentDao = instrumentDao;
        this.auditService = auditService;
        this.recurrentTransactionDao = recurrentTransactionDao;
        this.service = service;
    }

    @Override
    public List<Instrument> listInstruments() {
        return instrumentDao.listInstruments();
    }

    @Override
    public List<Instrument> listInstruments(int tenantId) {
        return instrumentDao.getInstrumentChilds(tenantId, EdgeType.TENANTGRAPH);
    }

    @Override
    public List<Instrument> listInstruments(int tenantId, boolean onlyActive) {
        List<Instrument> instruments = listInstruments(tenantId);
        if ( instruments!= null && !instruments.isEmpty()) {
            instruments = instruments.stream().filter(
                    i->(!onlyActive || i.isIsactive())
            ).collect(Collectors.toList());
        }
        return instruments;
    }

    @Override
    public List<Instrument> listInstruments(int tenantId, InstrumentType instrumentType, boolean onlyActive) {
        return listInstruments(tenantId).stream().filter(
                    i->i.getInstrumentType().equals(instrumentType) && (!onlyActive || i.isIsactive())
                ).collect(Collectors.toList());
    }

    @Override
    public Instrument getInstrument(int instrumentId) {
        return getInstrument(instrumentId, "");
    }

    @Override
    public Instrument getInstrument(int instrumentId, String errMsg) {
        var instrument = instrumentDao.getInstrument(instrumentId);
        if(!instrument.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, errMsg + " Instrument for id:"+instrumentId + " not found");
        }
        return instrument.get();
    }


    @Override
    public Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType) {
        return instrumentDao.getRootInstrument(instrumentId, edgeType);
    }

    @Override
    public List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType) {
        return instrumentDao.getAncestorGraphEntries(instrumentId, edgeType);
     }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength){
        return instrumentDao.getInstrumentChilds(instrumentId, edgeType, pathlength);
    }

    @Override
    public List<Instrument> listAccounts(int tenantId) {
        List<Instrument> accs = new ArrayList<>();
        List<Instrument>  childs = instrumentDao.getInstrumentChilds(tenantId, EdgeType.TENANTGRAPH, 1);
        if(childs==null || childs.isEmpty()) {
            return accs;
        }
        Optional<Instrument> accPF = childs.stream().filter(i -> i.getInstrumentType().equals(InstrumentType.ACCOUNTPORTFOLIO)).findFirst();
        if(!accPF.isPresent()) {
            return accs;
        }
        return instrumentDao.getInstrumentChilds(accPF.get().getInstrumentid(), EdgeType.TENANTGRAPH, 1);
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
    public Instrument getSecurity(String isin){
        return getSecurity(isin, "");
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
    public List<Instrument> listTenants(){
        return instrumentDao.listInstruments().stream().filter(i->i.getInstrumentType().equals(InstrumentType.TENANT)).collect(Collectors.toList());
    }

    @Override
    public Instrument getIncomeBudget(int budgetGroupId) {
        List<Instrument> incomeBudgets = instrumentDao.getInstrumentChilds(budgetGroupId, EdgeType.INCOMEBUDGET);
        if(incomeBudgets == null || incomeBudgets.isEmpty()) {
            throw new MFException(MFMsgKey.NO_INCOMEBUDGET_DEFINED_EXCEPTION, "No IncomeBudget defined for budgetGroupId:"+budgetGroupId);
        }
        return incomeBudgets.get(0);
    }

    @Override
    public List<InstrumentProperties> getInstrumentProperties(int instrumentId) {
        return instrumentDao.getInstrumentProperties(instrumentId);
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
    public void newTenant(String description, LocalDateTime ts) {
        Tenant tenant = new Tenant(description, true, ts);
        instrumentDao.saveInstrument(tenant);
        auditService.saveMessage("Tenant inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        addInstrumentToGraph(tenant.getInstrumentid(),tenant.getInstrumentid(),EdgeType.TENANTGRAPH);

        int budgetPfId = newBudgetPortfolio(DEFAULT_BUDGETPF_PREFIX+description, ts);
        addInstrumentToGraph(budgetPfId, tenant.getInstrumentid(), EdgeType.TENANTGRAPH);
        newBudgetGroup(description, budgetPfId, ts);
        int accPfId = newAccountPortfolio(DEFAULT_ACCPF_PREFIX+description, ts);
        addInstrumentToGraph(accPfId, tenant.getInstrumentid(), EdgeType.TENANTGRAPH);
    }

    protected void addInstrumentToGraph(int instrumentId, int ancestorId, EdgeType edgeType){
        List<InstrumentGraphEntry> ancestorGraphEntries = instrumentDao.getAncestorGraphEntries(ancestorId, edgeType);
        if(instrumentId!=ancestorId && ancestorGraphEntries.isEmpty()){
            InstrumentGraphEntry newEntry = new InstrumentGraphEntry(ancestorId, ancestorId, edgeType);
            newEntry.setPathlength(0);
            instrumentDao.saveGraphEntry(newEntry);
            ancestorGraphEntries = instrumentDao.getAncestorGraphEntries(ancestorId, edgeType);
        }
        for (InstrumentGraphEntry entry : ancestorGraphEntries) {
            InstrumentGraphEntry newEntry = new InstrumentGraphEntry(entry.getId().getAncestor(), instrumentId, edgeType);
            newEntry.setPathlength(entry.getPathlength()+1);
            instrumentDao.saveGraphEntry(newEntry);
        }
        InstrumentGraphEntry newEntry = new InstrumentGraphEntry(instrumentId, instrumentId, edgeType);
        newEntry.setPathlength(0);
        instrumentDao.saveGraphEntry(newEntry);
    }

    @Override
    public void newBudget(String description, int budgetGroupId, LocalDateTime ts) {
        Optional<Instrument> budgetGroup = instrumentDao.getInstrument(budgetGroupId);
        if(!budgetGroup.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_BUDGETGROUP_EXCEPTION, "Budget not saved: unknown budgetGroupId:"+budgetGroupId);
        }
        if(budgetGroup.get().getInstrumentType() != InstrumentType.BUDGETGROUP){
            throw new MFException(MFMsgKey.UNKNOWN_BUDGETGROUP_EXCEPTION,  "Budget not saved: Instrument with Id "+budgetGroupId + " is not a Budgetgroup");
        }
        createBudget(description, budgetGroupId, ts);
    }

    protected int createBudget(String description, int budgetGroupId, LocalDateTime ts) {
        Budget budget = new Budget(description, true, ts);
        auditService.saveMessage("budget inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveInstrument(budget);
        int budgetId = budget.getInstrumentid();
        addInstrumentToGraph(budgetId, budgetGroupId, EdgeType.TENANTGRAPH);
        return budgetId;
    }

    protected int newBudgetGroup(String description, int budgetPFId, LocalDateTime ts) {
        BudgetGroup budgetGroup = new BudgetGroup(DEFAULT_BUDGETGROUP_PREFIX+description, true, ts);
        instrumentDao.saveInstrument(budgetGroup);
        int budgetGroupId = budgetGroup.getInstrumentid();
        addInstrumentToGraph(budgetGroupId, budgetPFId, EdgeType.TENANTGRAPH);
        addInstrumentToGraph(budgetGroupId,budgetGroupId,EdgeType.INCOMEBUDGET);
        int incomeBudgetId = createBudget(DEFAULT_INCOMEBUDGET_PREFIX+description, budgetGroupId, ts);
        addInstrumentToGraph(incomeBudgetId, budgetGroupId, EdgeType.INCOMEBUDGET);
        return budgetGroupId;
    }

    protected int newAccountPortfolio(String description, LocalDateTime ts) {
        AccountPortfolio accountPortfolio = new AccountPortfolio(description, true, ts);
        instrumentDao.saveInstrument(accountPortfolio);
        return accountPortfolio.getInstrumentid();
    }

    protected int newBudgetPortfolio(String description, LocalDateTime ts) {
        var budgetPortfolio = new  BudgetPortfolio(description, true, ts);
        instrumentDao.saveInstrument(budgetPortfolio);
        return budgetPortfolio.getInstrumentid();
    }

    @Override
    public void newGiroAccount(String description, int tenantId, LocalDateTime ts) {
        Optional<Instrument> accportfolio = instrumentDao.getAccountPortfolio(tenantId);
        if(!accportfolio.isPresent()) {
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION,  "Giro not saved: tenant for the id:"+tenantId+" not exists or has no accountPortfolio");
        }
        Giro giro = new Giro(description, true, ts);
        auditService.saveMessage("new giro inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveInstrument(giro);
        addInstrumentToGraph(giro.getInstrumentid(), accportfolio.get().getInstrumentid(), EdgeType.TENANTGRAPH);
    }

    @Override
    public void newDepotAccount(String description, int tenantId, LocalDateTime ts, int defaultGiroId, int valueBudgetId) {
        Optional<Instrument> accportfolio = instrumentDao.getAccountPortfolio(tenantId);
        if(!accportfolio.isPresent()) {
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION,  "Depot not saved: tenant for the id:"+tenantId+" not exists or has no accountPortfolio");
        }
        var giro = getInstrument(defaultGiroId, "Depot not saved: Unknown default giro account:");
        if(giro.getInstrumentType()!=InstrumentType.GIRO){
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "Depot not saved: default giro has wrong instrument type:");
        }
        Optional<Integer> tenantOfGiro = getRootInstrument(giro.getInstrumentid(), EdgeType.TENANTGRAPH);
        if(!tenantOfGiro.isPresent()
            || !tenantOfGiro.get().equals(tenantId)){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION,  "Depot not saved: default giro has not the same tenant");
        }

        Depot depot = new Depot(description, true, ts);
        auditService.saveMessage("new depot inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveInstrument(depot);
        addInstrumentToGraph(depot.getInstrumentid(), accportfolio.get().getInstrumentid(), EdgeType.TENANTGRAPH);

        instrumentDao.saveInstrumentProperty(new InstrumentProperties(InstrumentPropertyType.DEFAULTGIROID.name(), depot.getInstrumentid(), String.valueOf(defaultGiroId), InstrumentPropertyType.DEFAULTGIROID.getValueType()));
        addInstrumentToGraph(depot.getInstrumentid(), valueBudgetId, EdgeType.VALUEBUDGET);
    }

    @Override
    public void updateDepotAccount(int instrumentId, String description, boolean isActive, int defaultGiroId) {
        updateInstrument(instrumentId, description, isActive);
        deleteInstrumentPropertyList(instrumentId);
        instrumentDao.saveInstrumentProperty(new InstrumentProperties(InstrumentPropertyType.DEFAULTGIROID.name(), instrumentId, String.valueOf(defaultGiroId), InstrumentPropertyType.DEFAULTGIROID.getValueType()));
    }

    @Override
    public void newRealEstate(String description, int tenantId, int valueBudgetId, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits, LocalDateTime ts) {
        Optional<Instrument> accportfolio = instrumentDao.getAccountPortfolio(tenantId);
        if(!accportfolio.isPresent()) {
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION,  "RealEstate not saved: tenant for the id:"+tenantId+" not exists or has no accountPortfolio");
        }
        Optional<Instrument> budgetportfolio = instrumentDao.getBudgetPortfolio(tenantId);
        if(!budgetportfolio.isPresent()) {
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION,  "RealEstate not saved: tenant for the id:"+tenantId+" not exists or has no budgetPortfolio");
        }
        RealEstate realEstate = new RealEstate(description, true, ts);
        auditService.saveMessage("new RealEstate inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveInstrument(realEstate);
        addInstrumentToGraph(realEstate.getInstrumentid(), accportfolio.get().getInstrumentid(), EdgeType.TENANTGRAPH);
        saveYieldgoals(realEstate.getInstrumentid(), yieldgoals);
        saveRealestateProfits(realEstate.getInstrumentid(), realEstateProfits);
        var budgetGroupId = newBudgetGroup(description, budgetportfolio.get().getInstrumentid(), ts);
        addInstrumentToGraph(realEstate.getInstrumentid(), realEstate.getInstrumentid(), EdgeType.REALESTATEBUDGETGROUP);
        addInstrumentToGraph(budgetGroupId, realEstate.getInstrumentid(), EdgeType.REALESTATEBUDGETGROUP);
        addInstrumentToGraph(realEstate.getInstrumentid(), valueBudgetId, EdgeType.VALUEBUDGET);
    }

    @Override
    public void updateRealEstate(int instrumentId, String description, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits, boolean isActive) {
        updateInstrument(instrumentId, description, isActive);
        deleteInstrumentPropertyList(instrumentId);
        saveYieldgoals(instrumentId, yieldgoals);
        saveRealestateProfits(instrumentId, realEstateProfits);
    }

    private void deleteInstrumentPropertyList(int instrumentId) {
        var instrumentProperties = instrumentDao.getInstrumentProperties(instrumentId);
        for (InstrumentProperties instrumentProperty : instrumentProperties) {
            auditService.saveMessage(instrumentDao.deleteInstrumentProperty(instrumentProperty.getPropertyid()),
                Severity.INFO, AUDIT_MSG_TYPE);
        }
        
    }

    private void saveYieldgoals(int instrumentId, List<ValuePerDate> values) {
        for(var value : values) {
            instrumentDao.saveInstrumentProperty(new InstrumentProperties(InstrumentPropertyType.YIELDGOAL.name(), instrumentId, String.valueOf(value.getValue()), InstrumentPropertyType.YIELDGOAL.getValueType(), value.getDate(), null));
        } 
    }

    private void saveRealestateProfits(int instrumentId, List<ValuePerDate> values) {
        for(var value : values) {
            instrumentDao.saveInstrumentProperty(new InstrumentProperties(InstrumentPropertyType.REALESTATEPROFITS.name(), instrumentId, String.valueOf(value.getValue()), InstrumentPropertyType.REALESTATEPROFITS.getValueType(), value.getDate(), null));
        } 
    }

    @Override
    public void updateInstrument(int instrumentId, String description, boolean isActive) {
        var instrument = getInstrument(instrumentId, "Instrument not updated:");
        validateInstrument4Inactivation(instrument.getInstrumentid(), instrument.getInstrumentType(), instrument.isIsactive(),  isActive);
        String oldDesc = instrument.getDescription();
        instrumentDao.updateInstrument(instrumentId, description, isActive);
        if(instrument.getInstrumentType()==InstrumentType.TENANT) {
            List<Instrument> instruments = instrumentDao.listInstruments();
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_BUDGETGROUP_PREFIX, instruments);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_ACCPF_PREFIX, instruments);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_INCOMEBUDGET_PREFIX, instruments);
        }
    }

    private void validateInstrument4Inactivation(int instrumentId, InstrumentType instrumentType, boolean isActiveBeforeUpdate,  boolean isActiveAfterUpdate) {
        // try to deactivate instrument ?
        if(!isActiveAfterUpdate && isActiveBeforeUpdate) {
            if( !(
                instrumentType==InstrumentType.TENANT
                || instrumentType==InstrumentType.GIRO
                || instrumentType==InstrumentType.BUDGET
                || instrumentType==InstrumentType.REALESTATE
                )
            ) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "instrument with id:"+instrumentId + " not deactivated. It is not allowed for type " + instrumentType);
            }
            validateInstrumentValue4Inactivation(instrumentId, instrumentType);
            validateRecurrentTransactions4InstrumentInactivation(instrumentId, instrumentType);
        }        
    }

    private void validateInstrumentValue4Inactivation(int instrumentId, InstrumentType instrumentType) {
        if( (instrumentType==InstrumentType.GIRO || instrumentType==InstrumentType.BUDGET) 
            && service.getValue(instrumentId, LocalDate.MAX)!=0.0 ){
            throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+instrumentId + " not deactivated. The current value is not 0");
        } else if(instrumentType==InstrumentType.REALESTATE) {
            for(Instrument budgetGroup : getInstrumentChilds(instrumentId, EdgeType.REALESTATEBUDGETGROUP, 1)) {
                validateInstrumentValue4Inactivation(budgetGroup.getInstrumentid(), budgetGroup.getInstrumentType());
                instrumentDao.updateInstrument(budgetGroup.getInstrumentid(), budgetGroup.getDescription(), false);
            }
        } else if (instrumentType==InstrumentType.BUDGETGROUP) {
            for(Instrument budget : getInstrumentChilds(instrumentId, EdgeType.TENANTGRAPH, 1)) {
                updateInstrument(budget.getInstrumentid(), budget.getDescription(), false);
            }
        }
    }

    private void validateRecurrentTransactions4InstrumentInactivation(int instrumentId, InstrumentType instrumentType) {
        if(instrumentType==InstrumentType.GIRO || instrumentType==InstrumentType.BUDGET) {
            for (RecurrentTransaction r : recurrentTransactionDao.listRecurrentTransactions()) {

                if( r.getInstrumentByInstrumentid1().getInstrumentid() == instrumentId ||
                        r.getInstrumentByInstrumentid2().getInstrumentid() == instrumentId ) {
                    throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+
                            instrumentId + " not deactivated. There are still recurrent transactions for this instrument");
                }
            }
        }
    }

    private void renameDefaultTenantChild(int instrumentId, String newDesc, String oldDesc, String defaultDescPrefix, List<Instrument> instruments) {
        //look by description for default instruments of the tenant to rename
        instruments.stream().filter(i->i.getDescription().equals(defaultDescPrefix+oldDesc)).forEach(i->{
            //doublecheck if the instrument is part of the tenant
            if(instrumentDao.getAncestorGraphEntries(i.getInstrumentid(), EdgeType.TENANTGRAPH).stream().anyMatch(j->j.getId().getAncestor()==instrumentId)){
                i.setDescription(defaultDescPrefix+newDesc);
                instrumentDao.updateInstrument(i.getInstrumentid(), defaultDescPrefix+newDesc, i.isIsactive());
            }
        });
    }
}
