package de.hf.dac.myfinance.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.RecurrentFrequency;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.domain.Transaction;

public interface TransactionService {
    void updateTransaction(int transactionId, String description, double value, LocalDate transactionDate, LocalDateTime ts);
    void newIncomeExpense(String description, int accId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts);
    void newLinkedIncomeExpense(String description, int accId, int linkedAccId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts);
    void newTransfer(String description, int srcInstrumentId, int trgInstrumentId, double value, LocalDate transactionDate, LocalDateTime ts);
    List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate);
    List<Cashflow> listInstrumentCashflows(int instrumentId);
    Map<LocalDate, List<Cashflow>> getInstrumentCashflowMap(int instrumentId);
    void deleteTransaction(int transactionId);
    List<RecurrentTransaction> listRecurrentTransactions();
    void newRecurrentTransaction(String description, int srcInstrumentId, int trgInstrumentId, RecurrentFrequency recurrentFrequency, double value, LocalDate nextTransactionDate, LocalDateTime ts);
    void deleteRecurrentTransaction(int recurrentTransactionId);
    void updateRecurrentTransaction(int id, String description, double value, LocalDate nexttransaction, LocalDateTime ts);
    void bookRecurrentTransactions(LocalDateTime ts);
    void newTrade(String description, int depotId, String isin, double amount, int accId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts);
    void updateTrade(int tradsactionid, String description, double amount, double value, LocalDate transactionDate, LocalDateTime ts);
    List<Transaction> getTrades(int depotId);
}