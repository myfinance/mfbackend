package de.hf.dac.myfinance.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Trade;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.TradeDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;

public class TradeHandler extends IncomeExpensesHandler {
    Instrument security;
    Instrument depot;
    double amount;
    TradeDao tradeDao;
    
    public TradeHandler(InstrumentService instrumentService, 
            TransactionDao transactionDao, 
            AuditService auditService,
            CashflowDao cashflowDao,
            TradeDao tradeDao) {
        super(instrumentService, transactionDao, auditService, cashflowDao);
        this.tradeDao = tradeDao;
        transactionType = TransactionType.TRADE;
    }

    public void init(int accId, 
            int budgetId,
            String isin,
            int depotId,
            double amount,
            LocalDateTime ts, 
            String description, 
            double value,
            LocalDate transactionDate) {
        super.init(checkForDefaultGiro(accId, depotId), checkForDefaultBudget(budgetId, depotId), ts, description, value, transactionDate);                
        this.security = validateSecurity(isin);
        this.depot = validateInstrument(depotId, InstrumentType.DEPOT);
        this.amount = amount;
        saveMsg="new Trade saved with properties: isin "+ isin + ", amount " + amount + ", depot: " + depotId + ", Account "+account.getInstrumentid()+", Budget "+budget.getInstrumentid();
    }

    private int checkForDefaultGiro(int accId, int depotId) {
        int giroId = accId;
        if(accId == -1) {
            var instrumentProperties = instrumentService.getInstrumentProperties(depotId);
            if(instrumentProperties!=null && !instrumentProperties.isEmpty()) {
                for(var instrumentProperty : instrumentProperties) {
                    if(instrumentProperty.getPropertyname().equals(InstrumentPropertyType.DEFAULTGIROID.getStringValue())) {
                        giroId = Integer.parseInt(instrumentProperty.getValue());
                    } 
                }
            }
        }
        return giroId;
    }

    private int checkForDefaultBudget(int budgetId, int depotId) {
        int id = budgetId;
        if(budgetId == -1) {

        }
        return id;
    }


    private Instrument validateSecurity(String isin) {
        var thesecurity = instrumentService.getSecurity(isin, ERROR_MSG + ": Unknown security:");
        if(thesecurity.getInstrumentType()!=InstrumentType.EQUITY && thesecurity.getInstrumentType()!=InstrumentType.BOND){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, ERROR_MSG + ": wrong instrument type:" + thesecurity.getInstrumentid());
        }
        return thesecurity;
    }

    private Transaction prepareTrade(Transaction transaction) {
        var trade = new Trade(depot, security, transaction, amount);
        var trades = new HashSet<Trade>(); 
        trades.add(trade);
        transaction.setTrades(trades);
        return transaction;
    }

    @Override
    protected void prepareTransaction(LocalDateTime ts, String description, double value, LocalDate transactionDate) {
        super.prepareTransaction(ts, description, value, transactionDate);
        prepareTrade(transaction);
    }

    @Override
    protected void updateCache() {
        super.updateCache();
        //valueCurveHandler.invalidateCache(depot.getInstrumentid());
    }

    public void updateTrade(String description, double amount, double value, LocalDate transactionDate, LocalDateTime ts) {
        updateTransaction(description, value, transactionDate, ts);
        transaction.getTrades().forEach(i-> {
            if(i.getAmount()!=amount) {
                tradeDao.updateTrade(i.getTradeid(), amount);
                //valueCurveHandler.invalidateCache(i.getDepot().getInstrumentid());
            }});
    }

    @Override
    public void deleteTransaction(){
        super.deleteTransaction();
        //transaction.getTrades().forEach(i->valueCurveHandler.invalidateCache(i.getDepot().getInstrumentid()));
    }
}