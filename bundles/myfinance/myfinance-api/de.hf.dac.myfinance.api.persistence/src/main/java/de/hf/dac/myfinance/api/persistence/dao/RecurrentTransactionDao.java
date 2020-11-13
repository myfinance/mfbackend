package de.hf.dac.myfinance.api.persistence.dao;

import de.hf.dac.myfinance.api.domain.RecurrentTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecurrentTransactionDao {
    List<RecurrentTransaction> listRecurrentTransactions();
    Optional<RecurrentTransaction> getRecurrentTransaction(int recurrenttransactionid);
    String deleteRecurrentTransaction(int recurrenttransactionid);
    void saveRecurrentTransaction(RecurrentTransaction recurrenttransaction);
    void updateRecurrentTransaction(int recurrenttransactionid, String description, double value, LocalDate nexttransactionDate);
}
