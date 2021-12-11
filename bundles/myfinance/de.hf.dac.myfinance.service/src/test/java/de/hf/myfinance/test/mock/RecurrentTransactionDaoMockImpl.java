package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;

public class RecurrentTransactionDaoMockImpl implements RecurrentTransactionDao {

    @Override
    public List<RecurrentTransaction> listRecurrentTransactions() {
        return new ArrayList<RecurrentTransaction>();
    }

    @Override
    public Optional<RecurrentTransaction> getRecurrentTransaction(int recurrenttransactionid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String deleteRecurrentTransaction(int recurrenttransactionid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveRecurrentTransaction(RecurrentTransaction recurrenttransaction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateRecurrentTransaction(int recurrenttransactionid, String description, double value,
            LocalDate nexttransactionDate) {
        // TODO Auto-generated method stub

    }

}