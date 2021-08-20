package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hf.dac.myfinance.api.domain.Budget;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Depot;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.Giro;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.RecurrentFrequency;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.domain.Trade;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.myfinance.test.testcasegenerator.TradeTestCaseGenerator;

public class TransactionServiceMock implements TransactionService {

    TradeTestCaseGenerator tradeTestCase;
    
    public void setTradeTestCase(TradeTestCaseGenerator tradeTestCase){
        this.tradeTestCase = tradeTestCase;
    }

    @Override
    public void updateTransaction(int transactionId, String description, double value, LocalDate transactionDate,
            LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newIncomeExpense(String description, int accId, int budgetId, double value, LocalDate transactionDate,
            LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newTransfer(String description, int srcInstrumentId, int trgInstrumentId, double value,
            LocalDate transactionDate, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Cashflow> listInstrumentCashflows(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<LocalDate, List<Cashflow>> getInstrumentCashflowMap(int instrumentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteTransaction(int transactionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<RecurrentTransaction> listRecurrentTransactions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void newRecurrentTransaction(String description, int srcInstrumentId, int trgInstrumentId,
            RecurrentFrequency recurrentFrequency, double value, LocalDate nextTransactionDate, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteRecurrentTransaction(int recurrentTransactionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateRecurrentTransaction(int id, String description, double value, LocalDate nexttransaction,
            LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void bookRecurrentTransactions(LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newLinkedIncomeExpense(String description, int accId, int linkedAccId, int budgetId, double value,
            LocalDate transactionDate, LocalDateTime ts) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void newTrade(String description, int depotId, String isin, double amount, int accId, int budgetId,
            double value, LocalDate transactionDate, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTrade(int tradsactionid, String description, double amount, double value,
            LocalDate transactionDate, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Transaction> getTrades(int depotId) {
        return tradeTestCase.getTrades(depotId);
    }
    
}