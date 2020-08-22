/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseDao.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.persistence;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.InstrumentTypeGroup;

public abstract class BaseDao<T> {

    //default EntityManager
    protected EntityManager marketDataEm;

    //to create additional EntityManagers with separate connections for big transactions
    protected EntityManagerFactory marketDataEmf;

    protected final RepositoryService repositoryService = new RepositoryService();

    public BaseDao(EntityManagerFactory marketDataEmf) {
        this.marketDataEmf = marketDataEmf;
        marketDataEm = this.marketDataEmf.createEntityManager();
    }

    protected void save(Object object) {
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(object);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }

    protected List<T> listQueryResult(String queryString) {
        List<T> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery(queryString);
            result=(List<T>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }
    protected Optional<T> getFirstQueryResult(Query query) {
        Optional<T> result = Optional.empty();
        Object object = getFirstQueryObjectResult(query);
        if(object!=null) result = Optional.of((T) object);
        return result;
    }

    protected Optional<Integer> getFirstIntegerQueryResult(Query query) {
        Optional<Integer> result = Optional.empty();
        Object object = getFirstQueryObjectResult(query);
        if(object!=null) result = Optional.of((Integer) object);
        return result;
    }

    protected Object getFirstQueryObjectResult(Query query) {
        List<Object> queryResult = (List<Object>) query.getResultList();
        if (queryResult != null && !queryResult.isEmpty()) {
            return queryResult.get(0);
        }
        return null;
    }

    protected List<Integer> getInstrumentTypeIds() {
        List instrumentTypeIds = new ArrayList<Integer>();
        EnumSet.allOf(InstrumentType.class).stream().filter(i->i.getTypeGroup()== InstrumentTypeGroup.SECURITY)
               .forEach(i-> instrumentTypeIds.add(i.getValue()));
        return instrumentTypeIds;
    }
}
