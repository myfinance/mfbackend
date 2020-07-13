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
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.application.EnvTarget;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
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
    public void deleteTransaction(Transaction transaction) {
        deleteObject(transaction);
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        save(transaction);
    }

    @Override
    public void updateTransaction(int transactionid, String description, LocalDate transactionDate){
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Transaction a WHERE transactionid= :transactionid");
            query.setParameter("transactionid", transactionid);
            Optional<Transaction> transaction = getFirstQueryResult(query);
            Transaction newTransaction = transaction.get();
            newTransaction.setDescription(description);
            newTransaction.setTransactiondate(transactionDate);
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(newTransaction);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }
}
