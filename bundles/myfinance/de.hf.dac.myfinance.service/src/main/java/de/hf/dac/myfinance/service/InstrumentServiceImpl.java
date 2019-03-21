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
import de.hf.dac.myfinance.api.exceptions.MDException;
import de.hf.dac.myfinance.api.exceptions.MDMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.importhandler.ImportHandler;
import lombok.Data;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
public class InstrumentServiceImpl implements InstrumentService {

    private InstrumentDao instrumentDao;
    private ValueCurveService service;
    private WebRequestService webRequestService;
    private AuditService auditService;
    private static final String AUDIT_MSG_TYPE="InstrumentService_User_Event";

    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao, WebRequestService webRequestService, AuditService auditService){
        this.instrumentDao = instrumentDao;
        this.webRequestService = webRequestService;
        service = new ValueCurveService(instrumentDao);
        this.auditService = auditService;
    }

    @Override
    public List<Instrument> listInstruments() {

        List<Instrument> instruments = instrumentDao.listInstruments();
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
    public List<EndOfDayPrice> listEodPrices(int instrumentId) {

        return instrumentDao.listEndOfDayPrices(instrumentId);
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
        return instrumentDao.getEndOfDayPrice(instrumentId, date);
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
    public String saveEquity(String theisin, String description) {
        String isin = theisin.toUpperCase();
        Optional<Equity> existingSec = getEquity(isin);
        if(!existingSec.isPresent()) {
            Equity equity = new Equity(isin, description, true, LocalDateTime.now());
            auditService.saveMessage("Equity inserted:" + theisin, Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.saveInstrument(equity);
            return "new security saved sucessfully";
        } else {
            existingSec.get().setDescription(description);
            auditService.saveMessage("Equity " + theisin +" updated with new description: " + description, Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.saveInstrument(existingSec.get());
            return "security updated sucessfully";
        }
    }

    @Override
    public String saveSymbol(String theisin, String thesymbol, String thecurrencyCode){

        String isin = theisin.toUpperCase();
        String symbol = thesymbol.toUpperCase();
        String currencyCode = thecurrencyCode.toUpperCase();

        Optional<Instrument> currency = getCurrency(currencyCode);
        if(!currency.isPresent()) {
            return "Symbol not saved: unknown currency:"+currencyCode;
        }
        Optional<Equity> existingSec = getEquity(isin);
        if(!existingSec.isPresent()){
            return "Symbol not saved: unknown security:"+isin;
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
        return "Symbol saved";
    }

    @Override
    public String saveCurrency(String currencyCode, String description) {
        String curCode = currencyCode.toUpperCase();
        Optional<Instrument> existingCur = getCurrency(curCode);
        if(!existingCur.isPresent()) {
            Instrument currency = new Currency(currencyCode, description, true, LocalDateTime.now());
            auditService.saveMessage("Currency inserted:" + currencyCode, Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.saveInstrument(currency);
            return "new currency saved sucessfully";
        } else {
            auditService.saveMessage("Currency " + currencyCode +" updated with new description: " + description, Severity.INFO, AUDIT_MSG_TYPE);
            existingCur.get().setDescription(description);
            return "security updated sucessfully";
        }
    }

    @Override
    public String saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDateTime lastchanged) {
        return saveEndOfDayPrice(currencyCode, isin, SourceName.MAN.getValue(), dayofprice, value, lastchanged);
    }

    protected String saveEndOfDayPrice(String currencyCode, String businesskey, int sourceId, LocalDate dayofprice, Double value, LocalDateTime lastchanged) {
        Optional<Instrument> currency = getCurrency(currencyCode);
        if(!currency.isPresent()){
            return "Currency with code "+currencyCode+" is not available";
        }
        Optional<Instrument> security = instrumentDao.getSecurity(businesskey);
        if(!security.isPresent()){
            return "Security with businesskey "+businesskey+" is not available";
        }
        Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            return "Source with id "+sourceId+" is not available";
        }
        EndOfDayPrice price = new EndOfDayPrice(currency.get(), security.get(), source.get(), dayofprice, value, lastchanged);
        auditService.saveMessage("Price inserted for instrument " + security.get().getInstrumentid() +" and date " + dayofprice + " : " + value + " " + currency.get().getBusinesskey()
            , Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveEndOfDayPrice(price);
        return("Saved");
    }

    @Override
    public String importPrices(LocalDateTime ts){
        List<Source> sources = instrumentDao.getActiveSources();
        List<Instrument> secuirities = getSecurities();
        Optional<Instrument> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            return "Currency EUR not available";
        }

        ImportHandler handler = new ImportHandler(sources, eur.get(), webRequestService);
        for(Instrument security : secuirities){
            //all prices are in EUR so we do not need prices for this currency
            if(security.getInstrumentType()==InstrumentType.Currency && security.getBusinesskey().equals("EUR")) continue;
            LocalDate lastPricedDay = instrumentDao.getLastPricedDay(security.getInstrumentid());
            Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
            prices.putAll(handler.importSource(security, lastPricedDay, ts));
            for (EndOfDayPrice price : prices.values()) {
                instrumentDao.saveEndOfDayPrice(price);
            }
        }

        return "sucessful";
    }

    @Override
    public String fillPriceHistory(int sourceId, String businesskey, LocalDateTime ts){
        Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            return "Source with id "+sourceId+" is not available";
        }
        Optional<Instrument> security = instrumentDao.getSecurity(businesskey);
        if(!security.isPresent()){
            return "Security with isin "+businesskey+" is not available";
        }
        List<Source> sources = new ArrayList<>();
        sources.add(source.get());

        //all prices are in EUR so we do not need prices for this currency
        if(security.get().getInstrumentType()==InstrumentType.Currency && security.get().getBusinesskey().equals("EUR")) {
            return "Prices for currency EUR are not necessary";
        }

        Optional<Instrument> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            return "Currency EUR not available";
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
                instrumentDao.saveEndOfDayPrice(price);
                pricedDates.add(price.getDayofprice());
            }

        }
        return "sucessful";
    }

    @Override
    public String newTenant(String description, LocalDateTime ts) {
        Tenant tenant = new Tenant(description, true, ts);
        auditService.saveMessage("Tenant inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveInstrument(tenant);
        addInstrumentToGraph(tenant.getInstrumentid(),tenant.getInstrumentid(),EdgeType.TENANTGRAPH);
        int budgetGroupId = newBudgetGroup("budgetGroup_"+description, ts);
        addInstrumentToGraph(budgetGroupId, tenant.getInstrumentid(), EdgeType.TENANTGRAPH);
        int incomeBudgetId = createBudget("incomeBudget_"+description, budgetGroupId, ts);
        addInstrumentToGraph(incomeBudgetId, tenant.getInstrumentid(), EdgeType.INCOMEBUDGET);
        int accPfId = newAccountPortfolio("accountPf_"+description, ts);
        addInstrumentToGraph(accPfId, tenant.getInstrumentid(), EdgeType.TENANTGRAPH);
        return "new Tenant saved sucessfully";
    }

    protected void addInstrumentToGraph(int instrumentId, int ancestorId, EdgeType edgeType){
        List<InstrumentGraphEntry> ancestorGraphEntries = instrumentDao.getAncestorGraphEntries(ancestorId, edgeType);
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
    public String newBudget(String description, int budgetGroupId, LocalDateTime ts) {
        Optional<Instrument> budgetGroup = instrumentDao.getInstrument(budgetGroupId);
        if(!budgetGroup.isPresent()){
            return "Budget not saved: unknown budgetGroupId:"+budgetGroupId;
        }
        if(budgetGroup.get().getInstrumentType() != InstrumentType.BudgetGroup){
            return "Budget not saved: Instrument with Id "+budgetGroupId + " is not a Budgetgroup";
        }
        createBudget(description, budgetGroupId, ts);
        return "new budget saved sucessfully";
    }

    protected int createBudget(String description, int budgetGroupId, LocalDateTime ts) {
        Budget budget = new Budget(description, true, ts);
        auditService.saveMessage("budget inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveInstrument(budget);
        int budgetId = budget.getInstrumentid();
        addInstrumentToGraph(budgetId, budgetGroupId, EdgeType.TENANTGRAPH);
        return budgetId;
    }

    protected int newBudgetGroup(String description, LocalDateTime ts) {
        BudgetGroup budgetGroup = new BudgetGroup(description, true, ts);
        instrumentDao.saveInstrument(budgetGroup);
        return budgetGroup.getInstrumentid();
    }

    protected int newAccountPortfolio(String description, LocalDateTime ts) {
        AccountPortfolio accountPortfolio = new AccountPortfolio(description, true, ts);
        instrumentDao.saveInstrument(accountPortfolio);
        return accountPortfolio.getInstrumentid();
    }

    @Override
    public String newGiroAccount(String description, int tenantId, LocalDateTime ts) {
        Optional<Instrument> accportfolio = instrumentDao.getAccountPortfolio(tenantId);
        if(!accportfolio.isPresent()) {
            return "Giro not saved: tenant for the id:"+tenantId+" not exists or has no accountPortfolio";
        }
        Giro giro = new Giro(description, true, ts);
        auditService.saveMessage("new giro inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveInstrument(giro);
        addInstrumentToGraph(giro.getInstrumentid(), accportfolio.get().getInstrumentid(), EdgeType.TENANTGRAPH);
        return "new giro saved sucessfully";
    }

    @Override
    public String updateInstrumentDesc(int instrumentId, String description) {
        return null;
    }

    @Override
    public String deactivateInstrument(int instrumentId) {
        return null;
    }

    @Override
    public String newIncomeExpense(String description, int accId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts){
        Optional<Instrument> account = instrumentDao.getInstrument(accId);
        if(!account.isPresent() || account.get().getInstrumentType()!=InstrumentType.Giro){
            return "IncomeExpense not saved: unknown account oder wrong account type:"+accId;
        }
        Optional<Instrument> budget = instrumentDao.getInstrument(budgetId);
        if(!budget.isPresent() || budget.get().getInstrumentType()!=InstrumentType.Budget){
            return "IncomeExpense not saved: unknown budget:"+budgetId;
        }
        Optional<Integer> tenantOfAcc = instrumentDao.getRootInstrument(accId, EdgeType.TENANTGRAPH);
        Optional<Integer> tenantOfBudget = instrumentDao.getRootInstrument(budgetId, EdgeType.TENANTGRAPH);

        if(!tenantOfAcc.isPresent()
            || !tenantOfBudget.isPresent()
            || tenantOfAcc.get()!=tenantOfBudget.get()){
            return "IncomeExpense not saved: budget and account have not the same tenant";
        }
        Transaction transaction = new Transaction(description, transactionDate, ts, TransactionType.INCOMEEXPENSES);

        Cashflow accountCashflow = new Cashflow(account.get(), value);
        accountCashflow.setTransaction(transaction);
        Cashflow budgetCashflow = new Cashflow(budget.get(), value);
        budgetCashflow.setTransaction(transaction);
        Set<Cashflow> cashflows = new HashSet<>();
        cashflows.add(accountCashflow);
        cashflows.add(budgetCashflow);

        transaction.setCashflows(cashflows);
        auditService.saveMessage("new transaction saved for Account "+accId+" and Budget "+budgetId+". Date:" + transactionDate + ", value:" + value + ", desc:" +description,
            Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveTransaction(transaction);
        return "transaction saved sucessfully";
    }

    @Override
    public String newTransfer(String description, int srcInstrumentId, int trgInstrumentId, double value, LocalDate transactionDate, LocalDateTime ts){
        Optional<Instrument> src = instrumentDao.getInstrument(srcInstrumentId);
        TransactionType transactionType =TransactionType.TRANSFER;
        if(!src.isPresent()){
            return "Transfer not saved: unknown instrument:"+srcInstrumentId;
        }
        Optional<Instrument> trg = instrumentDao.getInstrument(trgInstrumentId);
        if(!trg.isPresent()){
            return "Transfer not saved: unknown instrument:"+trgInstrumentId;
        }
        if(trg.get().getInstrumentType() == InstrumentType.Budget){
            transactionType =TransactionType.BUDGETTRANSFER;
            if(src.get().getInstrumentType() != InstrumentType.Budget){
                return "Only transfers from budget to Budget or from Account to Account are allowed:";
            }
        } else {
            if( !isAccountTransferAllowed(trg.get()) || !isAccountTransferAllowed(src.get()) ){
                return "No Transfer allowed for this accounts:";
            }
        }

        Optional<Integer> tenantSrc = instrumentDao.getRootInstrument(srcInstrumentId, EdgeType.TENANTGRAPH);
        Optional<Integer> tenantTrg = instrumentDao.getRootInstrument(trgInstrumentId, EdgeType.TENANTGRAPH);

        if(!tenantSrc.isPresent()
            || !tenantTrg.isPresent()
            || tenantSrc.get()!=tenantTrg.get()){
            return "IncomeExpense not saved: budget and account have not the same tenant";
        }
        Transaction transaction = new Transaction(description, transactionDate, ts, transactionType);

        Cashflow srcCashflow = new Cashflow(src.get(), value * -1);
        srcCashflow.setTransaction(transaction);
        Cashflow trgCashflow = new Cashflow(trg.get(), value);
        trgCashflow.setTransaction(transaction);
        Set<Cashflow> cashflows = new HashSet<>();
        cashflows.add(srcCashflow);
        cashflows.add(trgCashflow);

        transaction.setCashflows(cashflows);
        auditService.saveMessage("new transaction saved for Instrument "+srcInstrumentId+" and  "+trgInstrumentId+". Date:" + transactionDate + ", value:" + value + ", desc:" +description,
            Severity.INFO, AUDIT_MSG_TYPE);
        instrumentDao.saveTransaction(transaction);
        return "transaction saved sucessfully";
    }

    @Override
    public String deleteTransaction(int transactionId){
        Optional<Transaction> transaction = instrumentDao.getTransaction(transactionId);
        if(transaction.isPresent()){
            auditService.saveMessage(" transaction with id "+transactionId+" ,desc: '"+transaction.get().getDescription()+
                    "' and Transactiondate:" + transaction.get().getTransactiondate() + "deleted",
                Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.deleteTransaction(transaction.get());
        }
        return "transaction deleted:"+transactionId;
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
    public List<Transaction> listTransactions(){
        List<Transaction> transactions = instrumentDao.listTransactions();
        return transactions;
    }
}
