package de.hf.myfinance.test.testcasegenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hf.dac.myfinance.api.domain.Budget;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Depot;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.Trade;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;

public class TradeTestCaseGenerator {

    List<Transaction> transactions = new ArrayList<Transaction>();
    Depot depot = new Depot("depot", true, LocalDateTime.now());
    Giro giro = new Giro("giro", true, LocalDateTime.now());
    Budget budget = new Budget("budget", true, LocalDateTime.now());

    public TradeTestCaseGenerator() {
        depot = new Depot("depot", true, LocalDateTime.now());
        giro = new Giro("giro", true, LocalDateTime.now());
        budget = new Budget("budget", true, LocalDateTime.now());
    }

    public void addTrade(LocalDate transactionDate, int securityId, double amount) {
        var equity = new Equity("testisin"+securityId, "testequity"+securityId, true, LocalDateTime.now());
        equity.setInstrumentid(securityId);
        var aTransaction = buildTradeTransaction(depot, giro, budget, transactionDate, equity, "atrade", amount, -100);
        transactions.add(aTransaction);
    }

    public List<Transaction> getTrades(int depotId) {
        return transactions;
    }

    private Transaction buildTradeTransaction(Instrument depot, Instrument giro, Instrument budget, LocalDate transactionDate, Instrument equity, String description, double amount, double value) {
        var aTransaction = new Transaction(description, transactionDate, LocalDateTime.now(), TransactionType.TRADE);
        aTransaction.setTrades(buildTrade(depot, equity, amount, aTransaction));
        aTransaction.setCashflows(buildIncomeExpense(giro, budget, value));
        return aTransaction;
    }

    private Set<Trade> buildTrade(Instrument depot, Instrument equity, double amount, Transaction aTransaction) {
        var aTrade = new Trade(depot, equity, aTransaction, amount);
        var firstTradeSet = new HashSet<Trade>();
        firstTradeSet.add(aTrade);
        return firstTradeSet;
    }

    private Set<Cashflow> buildIncomeExpense(Instrument giro, Instrument budget, double value) {
        var firstCashflow = new Cashflow(giro, value);
        var secondCashflow = new Cashflow(budget, value);
        var firstCashflowSet = new HashSet<Cashflow>();
        firstCashflowSet.add(firstCashflow);
        firstCashflowSet.add(secondCashflow);
        return firstCashflowSet;
    }
    
}