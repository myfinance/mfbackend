/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : EndOfDayPriceDaoImpl.java
 * Author(s)   : hf
 * Created     : 05.04.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import de.hf.dac.myfinance.api.application.EnvTarget;
import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Source;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;

public class EndOfDayPriceDaoImpl extends BaseDao<EndOfDayPrice> implements EndOfDayPriceDao {


    @Inject
    public EndOfDayPriceDaoImpl(@Named(EnvTarget.MDB)
        EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
    }

    @Override
    public List<EndOfDayPrice> listEndOfDayPrices(int instrumentid) {
        List<EndOfDayPrice> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM EndOfDayPrice a WHERE instrumentid = :instrumentid");
            query.setParameter("instrumentid", instrumentid);
            result = (List<EndOfDayPrice>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentid, LocalDate dayofprice) {
        Optional<EndOfDayPrice> result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM EndOfDayPrice a WHERE instrumentid = :instrumentid and dayofprice = :dayofprice");
            query.setParameter("instrumentid", instrumentid);
            query.setParameter("dayofprice", dayofprice);
            result = getFirstQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public LocalDate getLastPricedDay(int instrumentid) {
        LocalDate result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select max(a.dayofprice) FROM EndOfDayPrice a WHERE instrumentid = :instrumentid");
            query.setParameter("instrumentid", instrumentid);
            result = (LocalDate) query.getSingleResult();
        } finally {
            marketDataEm.close();
        }
        return result;
    }



    @Override
    public void saveEndOfDayPrice(EndOfDayPrice price) {
        save(price);
    }

    @Override
    public Optional<Source> getSource(int sourceId) {
        Optional<Source> result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Source a WHERE id = :id");
            query.setParameter("id", sourceId);
            result = getFirstSourceQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    private Optional<Source> getFirstSourceQueryResult(Query query) {
        Optional<Source> result = Optional.empty();
        Object object = getFirstQueryObjectResult(query);
        if(object!=null) result = Optional.of((Source) object);
        return result;
    }

    @Override
    public List<Source> getActiveSources() {
        List<Source> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Source a WHERE isactive = true");
            result = (List<Source>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

}
