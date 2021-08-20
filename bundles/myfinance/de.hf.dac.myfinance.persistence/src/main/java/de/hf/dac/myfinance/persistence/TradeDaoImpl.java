package de.hf.dac.myfinance.persistence;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import de.hf.dac.myfinance.api.domain.Trade;
import de.hf.dac.myfinance.api.persistence.dao.TradeDao;
import de.hf.dac.myfinance.api.application.EnvTarget;

public class TradeDaoImpl extends BaseDao<Trade> implements TradeDao {

    @Inject
    public TradeDaoImpl(
            @Named(EnvTarget.MDB)
                    EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
    }
    @Override
    public void updateTrade(int tradeid, double amount){
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Trade a WHERE tradeid= :tradeid");
            query.setParameter("tradeid", tradeid);
            Optional<Trade> trade = getFirstQueryResult(query);
            if(trade.isPresent()) {
                Trade newTrade = trade.get();
                newTrade.setAmount(amount);
                marketDataEm.getTransaction().begin();
                marketDataEm.persist(newTrade);
                marketDataEm.getTransaction().commit();
            }
        } finally {
            marketDataEm.close();
        }
    }
}
