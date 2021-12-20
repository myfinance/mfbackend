package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;

public class TransactionDaoMock implements TransactionDao {
    Transaction transaction;
    Map<Integer, Transaction> transactions = new HashMap<Integer, Transaction>();
    int maxId = 0;

    @Override
    public List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate) {
        return transactions.values().stream().filter(i->!(i.getTransactiondate().isBefore(startDate) || i.getTransactiondate().isAfter(endDate))).collect(Collectors.toList());
    }

    @Override
    public Optional<Transaction> getTransaction(int transactionid) {
        return Optional.ofNullable(transactions.get(transactionid));
    }

    @Override
    public List<Cashflow> listInstrumentCashflows(int instrumentId) {
        List<Cashflow> cashflows = new ArrayList<Cashflow>();
        transactions.values().forEach(i->
            i.getCashflows().forEach(j -> {
                if(j.getInstrument().getInstrumentid()==instrumentId){
                    cashflows.add(j);
                }}
            )
        );
        return cashflows;
    }

    @Override
    public Map<LocalDate, List<Cashflow>> getInstrumentCashflowMap(int instrumentId) {
        Map<LocalDate, List<Cashflow>> cashflows = new HashMap<LocalDate, List<Cashflow>>();
        transactions.values().forEach(i->
            i.getCashflows().forEach(j -> {
                if(j.getInstrument().getInstrumentid()==instrumentId){
                    List<Cashflow> cashflowPerDay = new ArrayList<Cashflow>();
                    if(cashflows.containsKey(i.getTransactiondate())) {
                        cashflowPerDay = cashflows.get(i.getTransactiondate());
                    }
                    cashflowPerDay.add(j);
                    cashflows.put(i.getTransactiondate(), cashflowPerDay);
                }}
            )
        );
        return cashflows;
    }

    @Override
    public String deleteTransaction(int transactionid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        maxId++;
        transaction.setTransactionid(maxId);
        transactions.put(transaction.getTransactionid(), transaction);
    }

    @Override
    public void updateTransaction(int transactionid, String description, LocalDate transactionDate, LocalDateTime ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Transaction> getTrades(int depotId) {
        return transactions.values().stream().filter(i->i.getTransactionType().equals(TransactionType.TRADE)).collect(Collectors.toList());
    }
    
}