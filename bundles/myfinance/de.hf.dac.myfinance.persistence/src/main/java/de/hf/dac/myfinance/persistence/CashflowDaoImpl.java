package de.hf.dac.myfinance.persistence;

import de.hf.dac.myfinance.api.application.EnvTarget;
import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Optional;

public class CashflowDaoImpl extends BaseDao<Cashflow> implements CashflowDao {

    @Inject
    public CashflowDaoImpl(
            @Named(EnvTarget.MDB)
                    EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
    }
    @Override
    public void updateCashflow(int cashflowid, double value){
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Cashflow a WHERE cashflowid= :cashflowid");
            query.setParameter("cashflowid", cashflowid);
            Optional<Cashflow> cashflow = getFirstQueryResult(query);
            if(cashflow.isPresent()) {
                Cashflow newCashflow = cashflow.get();
                newCashflow.setValue(value);
                marketDataEm.getTransaction().begin();
                marketDataEm.persist(newCashflow);
                marketDataEm.getTransaction().commit();
            }
        } finally {
            marketDataEm.close();
        }
    }
}
