/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : TransactionDaoImpl.java
 * Author(s)   : hf
 * Created     : 05.04.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.application.EnvTarget;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class TransactionDaoImpl  extends BaseDao<Transaction> implements TransactionDao {

    @Inject
    public TransactionDaoImpl(
        @Named(EnvTarget.MDB)
            EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
    }

    @Override
    public List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate) {
        List<Transaction> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Transaction a WHERE transactiondate >= :startDate and transactiondate <= :endDate");
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            result=(List<Transaction>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Transaction> getTransaction(int transactionid) {
        Optional<Transaction> result = Optional.empty();
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Transaction a WHERE transactionid= :transactionid");
            query.setParameter("transactionid", transactionid);
            result = getFirstQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public  List<Cashflow> listInstrumentCashflows(int instrumentId){
        List<Cashflow> returnValue;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            returnValue = getCashflows(instrumentId);
        } finally {
            marketDataEm.close();
        }
        return returnValue;
    }

    private List<Cashflow> getCashflows(int instrumentId) {
        List<Cashflow> returnValue;
        Query query = marketDataEm.createQuery("select a FROM Cashflow a WHERE a.instrument.instrumentid = :instrumentid");
        query.setParameter("instrumentid", instrumentId);
        returnValue=(List<Cashflow>) query.getResultList();
        return returnValue;
    }

    @Override
    public  Map<LocalDate, List<Cashflow>> getInstrumentCashflowMap(int instrumentId){
        Map<LocalDate, List<Cashflow>> returnValue = new HashMap<>();
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            getCashflows(instrumentId).forEach(x->{
                List<Cashflow> cashflows = new ArrayList<>();
                x.setDescription(x.getTransaction().getDescription());
                cashflows.add(x);
                if(returnValue.containsKey(x.getTransaction().getTransactiondate())) {
                    cashflows.addAll(returnValue.get(x.getTransaction().getTransactiondate()));
                    returnValue.remove(x.getTransaction().getTransactiondate());
                }
                returnValue.put(x.getTransaction().getTransactiondate(), cashflows);
            });
        } finally {
            marketDataEm.close();
        }
        return returnValue;
    }
    
    public String deleteTransaction(int transactionId) {
        String result = " transaction with id "+transactionId;
        try {

            marketDataEm = this.marketDataEmf.createEntityManager();
            marketDataEm.getTransaction().begin();
            Transaction transaction = marketDataEm.find(Transaction.class, transactionId);
            result+=" ,desc: '"+transaction.getDescription()+
                        "' ,Transactiondate:" + transaction.getTransactiondate() + " deleted";
            marketDataEm.remove(transaction);

            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        save(transaction);
    }

    @Override
    public void updateTransaction(int transactionid, String description, LocalDate transactionDate, LocalDateTime ts){
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Transaction a WHERE transactionid= :transactionid");
            query.setParameter("transactionid", transactionid);
            Optional<Transaction> transaction = getFirstQueryResult(query);
            if(transaction.isPresent()) {
                Transaction newTransaction = transaction.get();
                newTransaction.setDescription(description);
                newTransaction.setTransactiondate(transactionDate);
                newTransaction.setLastchanged(ts);
                marketDataEm.getTransaction().begin();
                marketDataEm.persist(newTransaction);
                marketDataEm.getTransaction().commit();
            }
        } finally {
            marketDataEm.close();
        }
    }
}
