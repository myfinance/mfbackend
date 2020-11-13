package de.hf.dac.myfinance.persistence;

import de.hf.dac.myfinance.api.application.EnvTarget;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RecurrentTransactionDaoImpl  extends BaseDao<RecurrentTransaction> implements RecurrentTransactionDao {

    @Inject
    public RecurrentTransactionDaoImpl(
            @Named(EnvTarget.MDB)
                    EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
    }

    @Override
    public List<RecurrentTransaction> listRecurrentTransactions() {
        return listQueryResult("select a FROM RecurrentTransaction a");
    }

    @Override
    public Optional<RecurrentTransaction> getRecurrentTransaction(int recurrenttransactionid) {
        Optional<RecurrentTransaction> result = Optional.empty();
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM RecurrentTransaction a WHERE recurrenttransactionid= :recurrenttransactionid");
            query.setParameter("recurrenttransactionid", recurrenttransactionid);
            result = getFirstQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public String deleteRecurrentTransaction(int recurrenttransactionid) {
        String result = " recurrenttransaction with id "+recurrenttransactionid;
        try {

            marketDataEm = this.marketDataEmf.createEntityManager();
            marketDataEm.getTransaction().begin();
            RecurrentTransaction recurrenttransaction = marketDataEm.find(RecurrentTransaction.class, recurrenttransactionid);
            result+=" ,desc: '"+recurrenttransaction.getDescription()+
                    "' ,NextTransactiondate:" + recurrenttransaction.getNexttransaction() + " deleted";
            marketDataEm.remove(recurrenttransaction);

            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public void saveRecurrentTransaction(RecurrentTransaction recurrenttransaction) {
        save(recurrenttransaction);
    }

    @Override
    public void updateRecurrentTransaction(int recurrenttransactionid, String description, double value, LocalDate nexttransactionDate){
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM RecurrentTransaction a WHERE recurrenttransactionid= :recurrenttransactionid");
            query.setParameter("recurrenttransactionid", recurrenttransactionid);
            Optional<RecurrentTransaction> transaction = getFirstQueryResult(query);
            RecurrentTransaction newTransaction = transaction.get();
            newTransaction.setDescription(description);
            newTransaction.setNexttransaction(nexttransactionDate);
            newTransaction.setValue(value);
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(newTransaction);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }
}
