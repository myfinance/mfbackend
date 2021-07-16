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
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class TradeHandler extends IncomeExpensesHandler {
    Instrument security;
    Instrument depot;
    double amount;
    
    public TradeHandler(InstrumentService instrumentService, 
            TransactionDao transactionDao, 
            AuditService auditService,
            ValueCurveService valueCurveService,
            CashflowDao cashflowDao) {
        super(instrumentService, transactionDao, auditService, valueCurveService, cashflowDao);
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
}