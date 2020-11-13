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
import de.hf.dac.myfinance.ValueHandler.ValueCurveService;
import de.hf.dac.myfinance.api.domain.*;
import de.hf.dac.myfinance.api.domain.Currency;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.*;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.importhandler.ImportHandler;
import lombok.Data;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class InstrumentServiceImpl implements InstrumentService {

    private InstrumentDao instrumentDao;
    private EndOfDayPriceDao endOfDayPriceDao;
    private TransactionDao transactionDao;
    private RecurrentTransactionDao recurrentTransactionDao;
    private CashflowDao cashflowDao;
    private ValueCurveService service;
    private WebRequestService webRequestService;
    private AuditService auditService;
    private static final String AUDIT_MSG_TYPE="InstrumentService_User_Event";
    private final static String DEFAULT_BUDGETGROUP_PREFIX = "budgetGroup_";
    private final static String DEFAULT_INCOMEBUDGET_PREFIX = "incomeBudget_";
    private final static String DEFAULT_ACCPF_PREFIX = "accountPf_";

    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao, EndOfDayPriceDao endOfDayPriceDao, TransactionDao transactionDao, RecurrentTransactionDao recurrentTransactionDao, CashflowDao cashflowDao, WebRequestService webRequestService, AuditService auditService){
        this.instrumentDao = instrumentDao;
        this.endOfDayPriceDao = endOfDayPriceDao;
        this.transactionDao = transactionDao;
        this.recurrentTransactionDao = recurrentTransactionDao;
        this.cashflowDao = cashflowDao;
        this.webRequestService = webRequestService;
        service = new ValueCurveService(instrumentDao, endOfDayPriceDao);
        this.auditService = auditService;
    }

    @Override
    public List<Instrument> listInstruments() {
        List<Instrument> instruments = instrumentDao.listInstruments();
        return instruments;
    }

    @Override
    public List<Instrument> listInstruments(int tenantId) {
        List<Instrument> instruments = instrumentDao.getInstrumentChilds(tenantId, EdgeType.TENANTGRAPH);
        return instruments;
    }

    @Override
    public List<Instrument> listInstruments(int tenantId, InstrumentType instrumentType) {
        List<Instrument> instruments = listInstruments(tenantId).stream().filter(i->i.getInstrumentType().equals(instrumentType)).collect(Collectors.toList());
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
    public List<EndOfDayPrice> listEodPrices(int instrumentId) {

        return endOfDayPriceDao.listEndOfDayPrices(instrumentId);
    }

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(String isin, LocalDate date){
        Optional<Equity> security = getEquity(isin);
        if(!security.isPresent()) {
            return Optional.empty();
        }
        return getEndOfDayPrice(security.get().getInstrumentid(), date);
    }

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentId, LocalDate date){
        return endOfDayPriceDao.getEndOfDayPrice(instrumentId, date);
    }

    @Override
    public Optional<Source> getSource(int sourceId){
        return instrumentDao.getSource(sourceId);
    }

    @Override
    public Map<LocalDate, Double> getValueCurve(int instrumentId){
        return service.getValueCurve(instrumentId);
    }

    @Override
    public Map<LocalDate, Double> getValueCurve(int instrumentId, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Double> adjValueCurve = new HashMap();
        if(startDate.isAfter(endDate) || startDate.getYear()<1970) return adjValueCurve;
        Map<LocalDate, Double> valueCurve = getValueCurve(instrumentId);
        for (LocalDate date:valueCurve.keySet()) {
            if(!date.isBefore(startDate) && !date.isAfter(endDate)){
                adjValueCurve.put(date, valueCurve.get(date));
            }
        }
        return valueCurve;
    }

    @Override
    public double getValue(int instrumentId, LocalDate date){
        return service.getValue(instrumentId, date);
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
    public void saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDateTime lastchanged) {
        saveEndOfDayPrice(currencyCode, isin, SourceName.MAN.getValue(), dayofprice, value, lastchanged);
    }

    protected void saveEndOfDayPrice(String currencyCode, String businesskey, int sourceId, LocalDate dayofprice, Double value, LocalDateTime lastchanged) {
        Optional<Instrument> currency = getCurrency(currencyCode);
        if(!currency.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_CURRENCY_EXCEPTION, "Currency with code "+currencyCode+" is not available");
        }
        Optional<Instrument> security = instrumentDao.getSecurity(businesskey);
        if(!security.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Security with businesskey "+businesskey+" is not available");
        }
        Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_SOURCE_EXCEPTION, "Source with id "+sourceId+" is not available");
        }
        EndOfDayPrice price = new EndOfDayPrice(currency.get(), security.get(), source.get(), dayofprice, value, lastchanged);
        auditService.saveMessage("Price inserted for instrument " + security.get().getInstrumentid() +" and date " + dayofprice + " : " + value + " " + currency.get().getBusinesskey()
            , Severity.INFO, AUDIT_MSG_TYPE);
        endOfDayPriceDao.saveEndOfDayPrice(price);
    }

    @Override
    public void importPrices(LocalDateTime ts){
        List<Source> sources = instrumentDao.getActiveSources();
        List<Instrument> secuirities = getSecurities();
        Optional<Instrument> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_CURRENCY_EXCEPTION, "Currency EUR not available");
        }

        ImportHandler handler = new ImportHandler(sources, eur.get(), webRequestService);
        for(Instrument security : secuirities){
            //all prices are in EUR so we do not need prices for this currency
            if(security.getInstrumentType()==InstrumentType.Currency && security.getBusinesskey().equals("EUR")) continue;
            LocalDate lastPricedDay = endOfDayPriceDao.getLastPricedDay(security.getInstrumentid());
            Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
            prices.putAll(handler.importSource(security, lastPricedDay, ts));
            for (EndOfDayPrice price : prices.values()) {
                endOfDayPriceDao.saveEndOfDayPrice(price);
            }
        }
    }

    @Override
    public void fillPriceHistory(int sourceId, String businesskey, LocalDateTime ts){
        Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_SOURCE_EXCEPTION, "Source with id "+sourceId+" is not available");
        }
        Optional<Instrument> security = instrumentDao.getSecurity(businesskey);
        if(!security.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Security with isin "+businesskey+" is not available");
        }
        List<Source> sources = new ArrayList<>();
        sources.add(source.get());

        //all prices are in EUR so we do not need prices for this currency
        if(security.get().getInstrumentType()==InstrumentType.Currency && security.get().getBusinesskey().equals("EUR")) {
            throw new MFException(MFMsgKey.WRONG_REQUEST_EXCEPTION, "Prices for currency EUR are not necessary");
        }

        Optional<Instrument> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_CURRENCY_EXCEPTION, "Currency EUR not available");
        }

        ImportHandler handler = new ImportHandler(sources, eur.get(), webRequestService);

        Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
        prices.putAll(handler.importSource(security.get(), LocalDate.MIN, ts));

        List<EndOfDayPrice> existingPrices = listEodPrices(security.get().getInstrumentid());
        List<LocalDate> pricedDates = new ArrayList<>();
        if (existingPrices != null) {
            for (EndOfDayPrice price:existingPrices) {
                pricedDates.add(price.getDayofprice());
            }
        }

        for (EndOfDayPrice price : prices.values()) {
            if(!pricedDates.contains(price.getDayofprice())){
                endOfDayPriceDao.saveEndOfDayPrice(price);
                pricedDates.add(price.getDayofprice());
            }

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
        if( !isActive && (newInstrument.getInstrumentType()==InstrumentType.Giro || newInstrument.getInstrumentType()==InstrumentType.Budget)
            && service.getValue(instrumentId, LocalDate.MAX)!=0.0){

            throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT_FOR_DEACTIVATION, "instrument with id:"+instrumentId + " not deactivated. The current value is not 0");
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

    @Override
    public void newIncomeExpense(String description, int accId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts){
        Optional<Instrument> account = instrumentDao.getInstrument(accId);
        if(!account.isPresent() || account.get().getInstrumentType()!=InstrumentType.Giro){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "IncomeExpense not saved: unknown account oder wrong account type:"+accId);
        }
        Optional<Instrument> budget = instrumentDao.getInstrument(budgetId);
        if(!budget.isPresent() || budget.get().getInstrumentType()!=InstrumentType.Budget){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "IncomeExpense not saved: unknown budget:"+budgetId);
        }
        Optional<Integer> tenantOfAcc = instrumentDao.getRootInstrument(accId, EdgeType.TENANTGRAPH);
        Optional<Integer> tenantOfBudget = instrumentDao.getRootInstrument(budgetId, EdgeType.TENANTGRAPH);

        if(!tenantOfAcc.isPresent()
            || !tenantOfBudget.isPresent()
            || tenantOfAcc.get()!=tenantOfBudget.get()){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION, "IncomeExpense not saved: budget and account have not the same tenant");
        }
        Transaction transaction = new Transaction(description, transactionDate, ts, TransactionType.INCOMEEXPENSES);

        Cashflow accountCashflow = new Cashflow(account.get(), value);
        accountCashflow.setTransaction(transaction);
        Cashflow budgetCashflow = new Cashflow(budget.get(), value);
        budgetCashflow.setTransaction(transaction);
        Set<Cashflow> cashflows = new HashSet<>();
        cashflows.add(accountCashflow);
        cashflows.add(budgetCashflow);

        service.updateCache(accId);
        service.updateCache(budgetId);

        transaction.setCashflows(cashflows);
        auditService.saveMessage("new transaction saved for Account "+accId+" and Budget "+budgetId+". Date:" + transactionDate + ", value:" + value + ", desc:" +description,
            Severity.INFO, AUDIT_MSG_TYPE);
        transactionDao.saveTransaction(transaction);
    }

    @Override
    public void newTransfer(String description, int srcInstrumentId, int trgInstrumentId, double value, LocalDate transactionDate, LocalDateTime ts){
        Optional<Instrument> src = instrumentDao.getInstrument(srcInstrumentId);
        TransactionType transactionType =TransactionType.TRANSFER;
        if(!src.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Transfer not saved: unknown instrument:"+srcInstrumentId);
        }
        Optional<Instrument> trg = instrumentDao.getInstrument(trgInstrumentId);
        if(!trg.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Transfer not saved: unknown instrument:"+trgInstrumentId);
        }
        if(trg.get().getInstrumentType() == InstrumentType.Budget){
            transactionType =TransactionType.BUDGETTRANSFER;
            if(src.get().getInstrumentType() != InstrumentType.Budget){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "Only transfers from budget to Budget or from Account to Account are allowed");
            }
        } else {
            if( !isAccountTransferAllowed(trg.get()) || !isAccountTransferAllowed(src.get()) ){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this accounts:");
            }
        }

        Optional<Integer> tenantSrc = instrumentDao.getRootInstrument(srcInstrumentId, EdgeType.TENANTGRAPH);
        Optional<Integer> tenantTrg = instrumentDao.getRootInstrument(trgInstrumentId, EdgeType.TENANTGRAPH);

        if(!tenantSrc.isPresent()
            || !tenantTrg.isPresent()
            || tenantSrc.get()!=tenantTrg.get()){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION, "IncomeExpense not saved: budget and account have not the same tenant");
        }
        Transaction transaction = new Transaction(description, transactionDate, ts, transactionType);

        Cashflow srcCashflow = new Cashflow(src.get(), value * -1);
        srcCashflow.setTransaction(transaction);
        Cashflow trgCashflow = new Cashflow(trg.get(), value);
        trgCashflow.setTransaction(transaction);
        Set<Cashflow> cashflows = new HashSet<>();
        cashflows.add(srcCashflow);
        cashflows.add(trgCashflow);

        service.updateCache(srcInstrumentId);
        service.updateCache(trgInstrumentId);

        transaction.setCashflows(cashflows);
        auditService.saveMessage("new transaction saved for Instrument "+srcInstrumentId+" and  "+trgInstrumentId+". Date:" + transactionDate + ", value:" + value + ", desc:" +description,
            Severity.INFO, AUDIT_MSG_TYPE);
        transactionDao.saveTransaction(transaction);
    }

    @Override
    public void updateTransaction(int transactionId, String description, double value, LocalDate transactionDate, LocalDateTime ts){
        Optional<Transaction> transaction = transactionDao.getTransaction(transactionId);
        if(!transaction.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_TRANSACTION_EXCEPTION, "Transaction not updated: Transaction for id:"+transactionId + " not found");
        }
        Transaction oldtransaction = transaction.get();
        transactionDao.updateTransaction(transactionId, description, transactionDate, ts);
        if(oldtransaction.getTransactionType() == TransactionType.INCOMEEXPENSES) {
            oldtransaction.getCashflows().forEach(i-> {
                if(i.getValue()!=value) {
                    cashflowDao.updateCashflow(i.getCashflowid(), value);
                    service.updateCache(i.getInstrument().getInstrumentid());
                }});
        } else if(oldtransaction.getTransactionType() == TransactionType.BUDGETTRANSFER ||
                oldtransaction.getTransactionType() == TransactionType.TRANSFER) {
            oldtransaction.getCashflows().forEach(i-> {
                if( (i.getValue() < 0 && value < 0) || (i.getValue() > 0 && value > 0)) {
                    cashflowDao.updateCashflow(i.getCashflowid(), value);
                } else {
                    cashflowDao.updateCashflow(i.getCashflowid(), -1 * value);
                }
                service.updateCache(i.getInstrument().getInstrumentid());
            });
        }
        auditService.saveMessage(" transaction with id "+transactionId+" ,desc: '"+oldtransaction.getDescription()+
                        "' and Transactiondate:" + oldtransaction.getTransactiondate() + "updated to desc="+description + ", date=" + transactionDate +
                        " and value=" + value,
                Severity.INFO, AUDIT_MSG_TYPE);

    }

    @Override
    public void deleteTransaction(int transactionId){
        Optional<Transaction> transaction = transactionDao.getTransaction(transactionId);
        if(transaction.isPresent()){
            transaction.get().getCashflows().forEach(i-> {
                service.updateCache(i.getInstrument().getInstrumentid());
            });
            auditService.saveMessage(transactionDao.deleteTransaction(transactionId),
                    Severity.INFO, AUDIT_MSG_TYPE);
        }
    }

    @Override
    public List<RecurrentTransaction> listRecurrentTransactions() {
        return recurrentTransactionDao.listRecurrentTransactions();
    }

    @Override
    public void newRecurrentTransaction(String description, int srcInstrumentId, int trgInstrumentId, RecurrentFrequency recurrentFrequency, double value, LocalDate nextTransactionDate, LocalDateTime ts) {
        Optional<Instrument> src = instrumentDao.getInstrument(srcInstrumentId);
        RecurrentTransactionType recurrentTransactionType = RecurrentTransactionType.Transfer;
        if(!src.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "RecurrentTransfer not saved: unknown instrument:"+srcInstrumentId);
        }
        Optional<Instrument> trg = instrumentDao.getInstrument(trgInstrumentId);
        if(!trg.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "RecurrentTransfer not saved: unknown instrument:"+trgInstrumentId);
        }
        if(src.get().getInstrumentType() == InstrumentType.Budget && trg.get().getInstrumentType() == InstrumentType.Budget) {
            recurrentTransactionType = RecurrentTransactionType.BudgetTransfer;
        } else if (src.get().getInstrumentType() == InstrumentType.Budget) {
            if( !isAccountTransferAllowed(trg.get())) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+trgInstrumentId);
            }
            recurrentTransactionType = getRecurrentTransactiontype(value);
        } else if (trg.get().getInstrumentType() == InstrumentType.Budget) {
            if( !isAccountTransferAllowed(src.get())) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+srcInstrumentId);
            }
            recurrentTransactionType = getRecurrentTransactiontype(value);
        } else {
            if( !isAccountTransferAllowed(trg.get())){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+trgInstrumentId);
            }
            if( !isAccountTransferAllowed(src.get())){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "No Transfer allowed for this account:"+srcInstrumentId);
            }
            recurrentTransactionType = RecurrentTransactionType.Transfer;
        }

        Optional<Integer> tenantSrc = instrumentDao.getRootInstrument(srcInstrumentId, EdgeType.TENANTGRAPH);
        Optional<Integer> tenantTrg = instrumentDao.getRootInstrument(trgInstrumentId, EdgeType.TENANTGRAPH);

        if(!tenantSrc.isPresent()
                || !tenantTrg.isPresent()
                || tenantSrc.get()!=tenantTrg.get()){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION, "RecurrentTransfer not saved: budget and account have not the same tenant");
        }
        RecurrentTransaction recurrentTransaction = new RecurrentTransaction(src.get(), trg.get(), recurrentTransactionType.getValue(), description, value, nextTransactionDate, recurrentFrequency);

        auditService.saveMessage("new recurrenttransaction saved for Instrument "+srcInstrumentId+
                        " and  "+trgInstrumentId+". nextTransactionDate:" + nextTransactionDate +
                        ", value:" + value + ", desc:" +description + ", frequency:" +recurrentFrequency,
                Severity.INFO, AUDIT_MSG_TYPE);
        recurrentTransactionDao.saveRecurrentTransaction(recurrentTransaction);
    }

    @Override
    public void deleteRecurrentTransaction(int recurrentTransactionId) {
        Optional<RecurrentTransaction> transaction = recurrentTransactionDao.getRecurrentTransaction(recurrentTransactionId);
        if(transaction.isPresent()){
            auditService.saveMessage(recurrentTransactionDao.deleteRecurrentTransaction(recurrentTransactionId),
                    Severity.INFO, AUDIT_MSG_TYPE);
        }
    }

    @Override
    public void updateRecurrentTransaction(int id, String description, double value, LocalDate nexttransaction, LocalDateTime ts) {
        Optional<RecurrentTransaction> transaction = recurrentTransactionDao.getRecurrentTransaction(id);
        if(!transaction.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_TRANSACTION_EXCEPTION, "RecurrentTransaction not updated: RecurrentTransaction for id:"+id + " not found");
        }
        RecurrentTransaction oldtransaction = transaction.get();
        if(
                (RecurrentTransactionType.getRecurrentTransactionTypeById(oldtransaction.getRecurrencytype()) != RecurrentTransactionType.Expenses
                    && value < 0) || (
            RecurrentTransactionType.getRecurrentTransactionTypeById(oldtransaction.getRecurrencytype()) == RecurrentTransactionType.Expenses
                    && value > 0) ){
            throw new MFException(MFMsgKey.WRONG_TRNSACTIONTYPE_EXCEPTION, "RecurrentTransaction not updated: Type:"
                    +RecurrentTransactionType.getRecurrentTransactionTypeById(oldtransaction.getRecurrencytype()) +
                    " and value:"+value + " do not match");
        }

        recurrentTransactionDao.updateRecurrentTransaction(id, description, value, nexttransaction);

        auditService.saveMessage(" recurrenttransaction with id "+id+" ,desc: '"+oldtransaction.getDescription()+
                        "' ,value:" + oldtransaction.getValue() + "" +
                        " and next transaction:" + oldtransaction.getNexttransaction()
                        + "updated to desc="+description + ", date=" + nexttransaction +
                        " and value=" + value,
                Severity.INFO, AUDIT_MSG_TYPE);
    }

    private RecurrentTransactionType getRecurrentTransactiontype(double value) {
        RecurrentTransactionType recurrentTransactionType;
        if(value <0) {
            recurrentTransactionType = RecurrentTransactionType.Expenses;
        } else {
            recurrentTransactionType = RecurrentTransactionType.Income;
        }
        return recurrentTransactionType;
    }

    protected boolean isAccountTransferAllowed(Instrument instrument){
        switch(instrument.getInstrumentType()){
            case Giro:
            case MoneyAtCall:
            case TimeDeposit:
            case BuildingsavingAccount:
            case LifeInsurance: return true;
            default: return false;
        }
    }

    @Override
    public List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate){
        List<Transaction> transactions = transactionDao.listTransactions(startDate, endDate);
        return transactions;
    }

    @Override
    public List<Cashflow> listInstrumentCashflows(int instrumentId){
        List<Cashflow> cashflows = instrumentDao.listInstrumentCashflows(instrumentId);
        return cashflows;
    }
}
