/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : InstrumentDaoImpl.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 28.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import de.hf.dac.marketdataprovider.api.application.EnvTarget;
import de.hf.dac.marketdataprovider.api.domain.Currency;
import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.SecuritySymbols;
import de.hf.dac.marketdataprovider.api.domain.Source;
import de.hf.dac.marketdataprovider.api.persistence.dao.InstrumentDao;
import de.hf.dac.marketdataprovider.api.persistence.repositories.InstrumentRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class InstrumentDaoImpl  extends BaseDao implements InstrumentDao {

    private InstrumentRepository instrumentRepository;

    @Inject
    public InstrumentDaoImpl(@Named(EnvTarget.MDB) EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
        instrumentRepository = repositoryService.buildRepository(InstrumentRepository.class, marketDataEm);
    }

    @Override
    public List<Instrument> listInstruments() {
        return instrumentRepository.findAll();
    }

    @Override
    public Optional<Instrument> getInstrument(int instrumentId) {

        Optional<Instrument> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM Instrument a WHERE instrumentid = :instrumentid");
        query.setParameter("instrumentid", instrumentId);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Instrument)object);
        }
        return result;
    }

    @Override
    public Optional<Security>getSecurity(String isin) {

        Optional<Security> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM Security a WHERE isin = :isin");
        query.setParameter("isin", isin);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Security)object);
        }
        return result;
    }

    @Override
    public List<Security> getSecurities() {

        Optional<Security> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM Security a");
        return (List<Security>) query.getResultList();
    }

    @Override
    public Optional<Currency> getCurrency(String currencyCode) {

        Optional<Currency> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM Currency a WHERE currencycode = :currencycode");
        query.setParameter("currencycode", currencyCode);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Currency)object);
        }
        return result;
    }

    @Override
    public List<EndOfDayPrice> listEndOfDayPrices(int instrumentid) {

        Optional<EndOfDayPrice> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM EndOfDayPrice a WHERE instrumentid = :instrumentid");
        query.setParameter("instrumentid", instrumentid);
        List<EndOfDayPrice> queryResult = (List<EndOfDayPrice>) query.getResultList();
        return queryResult;
    }

    @Override
    public Optional<EndOfDayPrice>getEndOfDayPrice(int instrumentid, LocalDate dayofprice) {

        Optional<EndOfDayPrice> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM EndOfDayPrice a WHERE instrumentid = :instrumentid and dayofprice = :dayofprice");
        query.setParameter("instrumentid", instrumentid);
        query.setParameter("dayofprice", dayofprice);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((EndOfDayPrice)object);
        }
        return result;
    }

    @Override
    public LocalDate getLastPricedDay(int instrumentid) {

        Query query = marketDataEm.createQuery("select max(a.dayofprice) FROM EndOfDayPrice a WHERE instrumentid = :instrumentid");
        query.setParameter("instrumentid", instrumentid);
        return (LocalDate) query.getSingleResult();
    }

    @Override
    public Optional<Source> getSource(int sourceId) {

        Optional<Source> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM Source a WHERE id = :id");
        query.setParameter("id", sourceId);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Source)object);
        }
        return result;
    }

    @Override
    public List<Source> getActiveSources() {

        Query query = marketDataEm.createQuery("select a FROM Source a WHERE isactive = true");
        return (List<Source>) query.getResultList();
    }

    @Override
    public void saveSecurity(Security security) {
        marketDataEm.getTransaction().begin();
        marketDataEm.persist(security);
        marketDataEm.getTransaction().commit();
    }

    @Override
    public void saveCurrency(Currency currency) {
        marketDataEm.getTransaction().begin();
        marketDataEm.persist(currency);
        marketDataEm.getTransaction().commit();
    }

    @Override
    public void saveSymbol(SecuritySymbols symbol) {
        marketDataEm.getTransaction().begin();
        marketDataEm.persist(symbol);
        marketDataEm.getTransaction().commit();
    }

    @Override
    public void saveEndOfDayPrice(EndOfDayPrice price) {
        marketDataEm.getTransaction().begin();
        marketDataEm.persist(price);
        marketDataEm.getTransaction().commit();
    }
}
