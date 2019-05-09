/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : TransactionDao.java
 * Author(s)   : xn01598
 * Created     : 05.04.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.persistence.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import de.hf.dac.myfinance.api.domain.Transaction;

public interface TransactionDao  {
    List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate);
    Optional<Transaction> getTransaction(int transactionid);
    void deleteTransaction(Transaction transaction);
    void saveTransaction(Transaction transaction);
}
