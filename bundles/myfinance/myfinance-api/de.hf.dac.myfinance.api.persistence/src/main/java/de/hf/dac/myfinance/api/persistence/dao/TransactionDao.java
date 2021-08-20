/**
 * ----------------------------------------------------------------------------
 * ---          Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : TransactionDao.java
 * Author(s)   : hf
 * Created     : 05.04.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.persistence.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Transaction;

public interface TransactionDao  {
    List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate);
    Optional<Transaction> getTransaction(int transactionid);
    List<Cashflow> listInstrumentCashflows(int instrumentId);
    Map<LocalDate, List<Cashflow>>getInstrumentCashflowMap(int instrumentId);
    String deleteTransaction(int transactionid);
    void saveTransaction(Transaction transaction);
    void updateTransaction(int transactionid, String description, LocalDate transactionDate, LocalDateTime ts);
    List<Transaction> getTrades(int depotId);
}
