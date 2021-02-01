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
    private final static String DEFAULT_BUDGETGROUP_PREFIX = "budgetGroup_";
    private final static String DEFAULT_INCOMEBUDGET_PREFIX = "incomeBudget_";
    private final static String DEFAULT_ACCPF_PREFIX = "accountPf_";

    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao, RecurrentTransactionDao recurrentTransactionDao, WebRequestService webRequestService, AuditService auditService, ValueCurveService service ){
        this.instrumentDao = instrumentDao;
        this.auditService = auditService;
        this.recurrentTransactionDao = recurrentTransactionDao;
        this.service = service;
    }

    @Override
    public List<Instrument> listInstruments() {
        List<Instrument> instruments = instrumentDao.listInstruments();
        return instruments;
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
        List<Instrument> instruments = listInstruments(tenantId).stream().filter(
                    i->i.getInstrumentType().equals(instrumentType) && (!onlyActive || i.isIsactive())
                ).collect(Collectors.toList());
        return instruments;
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
    public List<Instrument> getSecurities(){
        return instrumentDao.getSecurities();
    }

    @Override
    public List<Instrument> listTenants(){
        return instrumentDao.listInstruments().stream().filter(i->i.getInstrumentType().equals(InstrumentType.Tenant)).collect(Collectors.toList());
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
            instrumentDao.saveInstrument(existingSec.get());
        }
    }

    @Override
    public void saveSymbol(String theisin, String thesymbol, String thecurrencyCode){

        String isin = theisin.toUpperCase();
        String symbol = thesymbol.toUpperCase();
        String currencyCode = thecurrencyCode.toUpperCase();

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

        int budgetGroupId = newBudgetGroup(DEFAULT_BUDGETGROUP_PREFIX+description, tenant.getInstrumentid(), ts);
        int incomeBudgetId = createBudget(DEFAULT_INCOMEBUDGET_PREFIX+description, budgetGroupId, ts);
        addInstrumentToGraph(incomeBudgetId, budgetGroupId, EdgeType.INCOMEBUDGET);
        int accPfId = newAccountPortfolio(DEFAULT_ACCPF_PREFIX+description, ts);
        addInstrumentToGraph(accPfId, tenant.getInstrumentid(), EdgeType.TENANTGRAPH);
    }

    protected void addInstrumentToGraph(int instrumentId, int ancestorId, EdgeType edgeType){
        List<InstrumentGraphEntry> ancestorGraphEntries = instrumentDao.getAncestorGraphEntries(ancestorId, edgeType);
        if(instrumentId!=ancestorId && ancestorGraphEntries.isEmpty()){
            throw new MFException(MFMsgKey.ANCESTOR_DOES_NOT_EXIST_EXCEPTION,
                    "Can not add instrument "+instrumentId+" to tree. Ancestor: "
                            + ancestorId + " does not exists for edgetype " + edgeType.name());
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
        if(budgetGroup.get().getInstrumentType() != InstrumentType.BudgetGroup){
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

    protected int newBudgetGroup(String description, int tenantId, LocalDateTime ts) {
        BudgetGroup budgetGroup = new BudgetGroup(description, true, ts);
        instrumentDao.saveInstrument(budgetGroup);
        int budgetGroupId = budgetGroup.getInstrumentid();
        addInstrumentToGraph(budgetGroupId, tenantId, EdgeType.TENANTGRAPH);
        addInstrumentToGraph(budgetGroupId,budgetGroupId,EdgeType.INCOMEBUDGET);
        return budgetGroupId;
    }

    protected int newAccountPortfolio(String description, LocalDateTime ts) {
        AccountPortfolio accountPortfolio = new AccountPortfolio(description, true, ts);
        instrumentDao.saveInstrument(accountPortfolio);
        return accountPortfolio.getInstrumentid();
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
    public void updateInstrument(int instrumentId, String description, boolean isActive) {
        Optional<Instrument> instrument = instrumentDao.getInstrument(instrumentId);
        if(!instrument.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Instrument not updated: instrument for id:"+instrumentId + " not found");
        }
        Instrument newInstrument = instrument.get();
        if(!isActive && newInstrument.isIsactive() && !(
            instrument.get().getInstrumentType()==InstrumentType.Tenant
            || instrument.get().getInstrumentType()==InstrumentType.Giro
            || instrument.get().getInstrumentType()==InstrumentType.Budget
            )
        ){
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "instrument with id:"+instrumentId + " not deactivated. It is not allowed for type " + instrument.get().getInstrumentType());
        }
        String oldDesc = newInstrument.getDescription();
        if( !isActive && (newInstrument.getInstrumentType()==InstrumentType.Giro || newInstrument.getInstrumentType()==InstrumentType.Budget) ){
            if (service.getValue(instrumentId, LocalDate.MAX)!=0.0){
                throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+instrumentId + " not deactivated. The current value is not 0");
            }
            for (RecurrentTransaction r : recurrentTransactionDao.listRecurrentTransactions()) {

                if( r.getInstrumentByInstrumentid1().getInstrumentid() == instrumentId ||
                        r.getInstrumentByInstrumentid2().getInstrumentid() == instrumentId ) {
                    throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+
                            instrumentId + " not deactivated. There are still recurrent transactions for this instrument");
                }
            }
        }
        instrumentDao.updateInstrument(instrumentId, description, isActive);
        if(newInstrument.getInstrumentType()==InstrumentType.Tenant) {
            List<Instrument> instruments = instrumentDao.listInstruments();
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_BUDGETGROUP_PREFIX, instruments);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_ACCPF_PREFIX, instruments);
            renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_INCOMEBUDGET_PREFIX, instruments);
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
