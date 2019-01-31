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

package de.hf.dac.myfinance.persistence;

import de.hf.dac.myfinance.api.application.EnvTarget;
import de.hf.dac.myfinance.api.domain.*;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.repositories.InstrumentRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
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
    public List<Transaction> listTransactions(){
        Query query = marketDataEm.createQuery("select a FROM Transaction a");
        return (List<Transaction>) query.getResultList();
    }

    @Override
    public Optional<Transaction> getTransaction(int transactionid){
        Optional<Transaction> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM Transaction a WHERE transactionid= :transactionid");
        query.setParameter("transactionid", transactionid);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Transaction)object);
        }
        return result;
    }

    @Override
    public void deleteTransaction(Transaction transaction){
        marketDataEm.getTransaction().begin();
        marketDataEm.remove(transaction);
        marketDataEm.getTransaction().commit();
    }

    @Override
    public Optional<Equity> getEquity(String isin) {

        Optional<Equity> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM Instrument a WHERE instrumentTypeId= :instrumentTypeId and businesskey = :isin");
        query.setParameter("instrumentTypeId", InstrumentType.Equity.getValue());
        query.setParameter("isin", isin);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Equity)object);
        }
        return result;
    }

    @Override
    public Optional<Instrument> getSecurity(String businesskey) {
        List instrumentTypeIds = new ArrayList<Integer>();
        EnumSet.allOf(InstrumentType.class).stream().filter(i->i.getTypeGroup()==InstrumentTypeGroup.SECURITY).forEach(i-> instrumentTypeIds.add(i.getValue()));

        Optional<Instrument> result = Optional.empty();
        Query query = marketDataEm.createQuery(
            "select a FROM Instrument a WHERE a.instrumentTypeId IN :instrumentTypeIds and businesskey = :businesskey"
        );
        query.setParameter("instrumentTypeIds", instrumentTypeIds);
        query.setParameter("businesskey", businesskey);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Instrument)object);
        }
        return result;
    }

    @Override
    public List<Instrument> getSecurities() {
        List instrumentTypeIds = new ArrayList<Integer>();
        EnumSet.allOf(InstrumentType.class).stream().filter(i->i.getTypeGroup()==InstrumentTypeGroup.SECURITY).forEach(i-> instrumentTypeIds.add(i.getValue()));

        Optional<Instrument> result = Optional.empty();
        Query query = marketDataEm.createQuery(
            "select a FROM Instrument a WHERE a.instrumentTypeId IN :instrumentTypeIds"
        );
        query.setParameter("instrumentTypeIds", instrumentTypeIds);
        return (List<Instrument>) query.getResultList();
    }

    @Override
    public Optional<Instrument> getCurrency(String currencyCode) {

        Optional<Instrument> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a FROM Currency a WHERE businesskey = :currencycode");
        query.setParameter("currencycode", currencyCode);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Instrument)object);
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
    public void saveInstrument(Instrument instrument) {
        marketDataEm.getTransaction().begin();
        marketDataEm.persist(instrument);
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

    @Override
    public void saveTransaction(Transaction transaction) {
        marketDataEm.getTransaction().begin();
        marketDataEm.persist(transaction);
        marketDataEm.getTransaction().commit();
    }

    @Override
    public List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType) {

        Query query = marketDataEm.createQuery("select a FROM InstrumentGraphEntry a WHERE a.id.descendant= :instrumentid and a.id.edgetype= :edgetype");
        query.setParameter("instrumentid", instrumentId);
        query.setParameter("edgetype", edgeType);
        List<InstrumentGraphEntry> queryResult = (List<InstrumentGraphEntry>) query.getResultList();
        return queryResult;
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType) {

        Query query = marketDataEm.createQuery("select i FROM InstrumentGraphEntry a JOIN Instrument i ON i.instrumentid=a.instrumentid WHERE a.id.ancestor= :instrumentid and a.id.edgetype= :edgetype and a.pathlength=1");
        query.setParameter("instrumentid", instrumentId);
        query.setParameter("edgetype", edgeType);
        List<Instrument> queryResult = (List<Instrument>) query.getResultList();
        return queryResult;
    }

    @Override
    public Optional<Instrument> getAccountPortfolio(int tenantId){
        Optional<Instrument> result = Optional.empty();
        Query query = marketDataEm.createQuery("select i FROM InstrumentGraphEntry a JOIN Instrument i ON i.instrumentid=a.id.descendant "
            + "WHERE a.id.ancestor= :instrumentid and a.id.edgetype= :edgetype and a.pathlength=1 and i.instrumentTypeId= :instrumenttype");
        query.setParameter("instrumentid", tenantId);
        query.setParameter("edgetype", EdgeType.TENANTGRAPH);
        query.setParameter("instrumenttype", InstrumentType.AccountPortfolio.getValue());
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Instrument)object);
        }
        return result;
    }

    @Override
    public Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType) {
        Optional<Integer> result = Optional.empty();
        Query query = marketDataEm.createQuery("select a.id.ancestor FROM InstrumentGraphEntry a "
            + "WHERE a.id.descendant= :instrumentid and a.id.edgetype= :edgetype "
            + "ORDER BY a.pathlength desc ");
        query.setParameter("instrumentid", instrumentId);
        query.setParameter("edgetype", edgeType);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult!=null && !queryResult.isEmpty()){
            Object object = queryResult.get(0);
            result = Optional.of((Integer)object);
        }
        return result;
    }

    @Override
    public void saveGraphEntry(InstrumentGraphEntry instrumentGraphEntry) {
        marketDataEm.getTransaction().begin();
        marketDataEm.persist(instrumentGraphEntry);
        marketDataEm.getTransaction().commit();
    }
}
