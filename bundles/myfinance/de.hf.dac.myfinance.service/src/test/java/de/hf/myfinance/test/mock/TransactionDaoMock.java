package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;

public class TransactionDaoMock implements TransactionDao {
    Transaction transaction;

    @Override
    public List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Transaction> getTransaction(int transactionid) {
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
    public String deleteTransaction(int transactionid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    @Override
    public void updateTransaction(int transactionid, String description, LocalDate transactionDate, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Transaction> getTrades(int depotId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}