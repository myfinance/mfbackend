package de.hf.dac.myfinance.service.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
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
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class TradeHandler extends IncomeExpensesHandler {
    Instrument security;
    Instrument depot;
    double amount;
    TradeDao tradeDao;
    
    public TradeHandler(InstrumentService instrumentService, 
            TransactionDao transactionDao, 
            AuditService auditService,
            ValueCurveService valueCurveService,
            CashflowDao cashflowDao,
            TradeDao tradeDao) {
        super(instrumentService, transactionDao, auditService, valueCurveService, cashflowDao);
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
        super.init(accId, budgetId, ts, description, value, transactionDate);                
        this.security = validateSecurity(isin);
        this.depot = validateInstrument(depotId, InstrumentType.DEPOT);
        this.amount = amount;
        saveMsg="new Trade saved with properties: isin "+ isin + ", amount " + amount + ", depot: " + depotId + ", Account "+account.getInstrumentid()+", Budget "+budget.getInstrumentid();
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
        valueCurveService.updateCache(depot.getInstrumentid());
    }

    public void updateTrade(String description, double amount, double value, LocalDate transactionDate, LocalDateTime ts) {
        updateTransaction(description, value, transactionDate, ts);
        transaction.getTrades().forEach(i-> {
            if(i.getAmount()!=amount) {
                tradeDao.updateTrade(i.getTradeid(), amount);
                valueCurveService.updateCache(i.getDepot().getInstrumentid());
            }});
    }

    @Override
    public void deleteTransaction(){
        super.deleteTransaction();
        transaction.getTrades().forEach(i->valueCurveService.updateCache(i.getDepot().getInstrumentid()));
    }
}