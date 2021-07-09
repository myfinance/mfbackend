package de.hf.dac.myfinance.service.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Trade;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.service.InstrumentService;

public class TradeHandler extends AbsTransactionHandler {
    Instrument security;
    Instrument depot;
    double amount;
    
    public TradeHandler(InstrumentService instrumentService, 
            int accId, 
            int budgetId,
            String isin,
            int depotId,
            double amount) {
        super(instrumentService, accId, budgetId);
        this.amount = amount;
        transactionType = TransactionType.TRADE;
        this.security = validateSecurity(isin);
        this.depot = validateInstrument(depotId, InstrumentType.DEPOT);
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
    protected Transaction prepareTransaction(LocalDateTime ts, String description, double value, LocalDate transactionDate) {
        var transaction = super.prepareTransaction(ts, description, value, transactionDate);
        return prepareTrade(transaction);
    }
}