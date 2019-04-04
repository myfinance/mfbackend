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
    }

    @Override
    public List<Instrument> listInstruments() {
        List<Instrument> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            instrumentRepository = repositoryService.buildRepository(InstrumentRepository.class, marketDataEm);
            result=instrumentRepository.findAll();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Instrument> getInstrument(int instrumentId) {
        Optional<Instrument> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Instrument a WHERE instrumentid = :instrumentid");
            query.setParameter("instrumentid", instrumentId);
            result = getFirstInstrumentQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public List<Transaction> listTransactions(){
        List<Transaction> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Transaction a");
            result=(List<Transaction>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public List<Cashflow> listInstrumentCashflows(int instrumentId){
        List<Cashflow> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Cashflow a WHERE instrumentid = :instrumentid");
            query.setParameter("instrumentid", instrumentId);
            result=(List<Cashflow>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Transaction> getTransaction(int transactionid){
        Optional<Transaction> result = Optional.empty();
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query =marketDataEm.createQuery("select a FROM Transaction a WHERE transactionid= :transactionid");
            query.setParameter("transactionid", transactionid);
            result = getFirstTransactionQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public void deleteTransaction(Transaction transaction){
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            marketDataEm.getTransaction().begin();
            marketDataEm.remove(transaction);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }

    @Override
    public Optional<Equity> getEquity(String isin) {
        Optional<Equity> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Instrument a WHERE instrumentTypeId= :instrumentTypeId and businesskey = :isin");
            query.setParameter("instrumentTypeId", InstrumentType.Equity.getValue());
            query.setParameter("isin", isin);
            result = getFirstEquityQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Instrument> getSecurity(String businesskey) {
        List instrumentTypeIds=getInstrumentTypeIds();
        Optional<Instrument> result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Instrument a WHERE a.instrumentTypeId IN :instrumentTypeIds and businesskey = :businesskey");
            query.setParameter("instrumentTypeIds", instrumentTypeIds);
            query.setParameter("businesskey", businesskey);
            result = getFirstInstrumentQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public List<Instrument> getSecurities() {
        List instrumentTypeIds=getInstrumentTypeIds();
        List<Instrument> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery(
                "select a FROM Instrument a WHERE a.instrumentTypeId IN :instrumentTypeIds"
            );
            query.setParameter("instrumentTypeIds", instrumentTypeIds);
            List<Object> queryResult = (List<Object>) query.getResultList();
            result = (List<Instrument>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Instrument> getCurrency(String currencyCode) {
        Optional<Instrument> result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Currency a WHERE businesskey = :currencycode");
            query.setParameter("currencycode", currencyCode);
            result = getFirstInstrumentQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
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
    public Optional<EndOfDayPrice>getEndOfDayPrice(int instrumentid, LocalDate dayofprice) {
        Optional<EndOfDayPrice> result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM EndOfDayPrice a WHERE instrumentid = :instrumentid and dayofprice = :dayofprice");
            query.setParameter("instrumentid", instrumentid);
            query.setParameter("dayofprice", dayofprice);
            result = getFirstEndOfDayPriceQueryResult(query);
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

    @Override
    public void saveInstrument(Instrument instrument) {
        try{
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(instrument);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }

    @Override
    public void saveSymbol(SecuritySymbols symbol) {
        try{
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(symbol);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }

    @Override
    public void saveEndOfDayPrice(EndOfDayPrice price) {
        try{
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(price);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        try{
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(transaction);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }

    @Override
    public List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType) {
        List<InstrumentGraphEntry> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM InstrumentGraphEntry a WHERE a.id.descendant= :instrumentid and a.id.edgetype= :edgetype");
            query.setParameter("instrumentid", instrumentId);
            query.setParameter("edgetype", edgeType);
            result = (List<InstrumentGraphEntry>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType) {
        List<Instrument> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select i FROM InstrumentGraphEntry a JOIN Instrument i ON i.instrumentid=a.instrumentid WHERE a.id.ancestor= :instrumentid and a.id.edgetype= :edgetype and a.pathlength=1");
            query.setParameter("instrumentid", instrumentId);
            query.setParameter("edgetype", edgeType);
            result = (List<Instrument>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Instrument> getAccountPortfolio(int tenantId){
        Optional<Instrument> result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select i FROM InstrumentGraphEntry a JOIN Instrument i ON i.instrumentid=a.id.descendant "
                + "WHERE a.id.ancestor= :instrumentid and a.id.edgetype= :edgetype and a.pathlength=1 and i.instrumentTypeId= :instrumenttype");
            query.setParameter("instrumentid", tenantId);
            query.setParameter("edgetype", EdgeType.TENANTGRAPH);
            query.setParameter("instrumenttype", InstrumentType.AccountPortfolio.getValue());
            result = getFirstInstrumentQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType) {
        Optional<Integer> result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a.id.ancestor FROM InstrumentGraphEntry a "
                + "WHERE a.id.descendant= :instrumentid and a.id.edgetype= :edgetype "
                + "ORDER BY a.pathlength desc ");
            query.setParameter("instrumentid", instrumentId);
            query.setParameter("edgetype", edgeType);
            result = getFirstIntegerQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public void saveGraphEntry(InstrumentGraphEntry instrumentGraphEntry) {
        try{
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(instrumentGraphEntry);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }

    private Optional<Equity> getFirstEquityQueryResult(Query query) {
        Optional<Equity> result = Optional.empty();
        Object object = getFirstQueryResult(query);
        if(object!=null) result = Optional.of((Equity) object);
        return result;
    }

    private Optional<EndOfDayPrice> getFirstEndOfDayPriceQueryResult(Query query) {
        Optional<EndOfDayPrice> result = Optional.empty();
        Object object = getFirstQueryResult(query);
        if(object!=null) result = Optional.of((EndOfDayPrice) object);
        return result;
    }

    private Optional<Transaction> getFirstTransactionQueryResult(Query query) {
        Optional<Transaction> result = Optional.empty();
        Object object = getFirstQueryResult(query);
        if(object!=null) result = Optional.of((Transaction) object);
        return result;
    }

    private Optional<Instrument> getFirstInstrumentQueryResult(Query query) {
        Optional<Instrument> result = Optional.empty();
        Object object = getFirstQueryResult(query);
        if(object!=null) result = Optional.of((Instrument) object);
        return result;
    }

    private Optional<Source> getFirstSourceQueryResult(Query query) {
        Optional<Source> result = Optional.empty();
        Object object = getFirstQueryResult(query);
        if(object!=null) result = Optional.of((Source) object);
        return result;
    }

    private Optional<Integer> getFirstIntegerQueryResult(Query query) {
        Optional<Integer> result = Optional.empty();
        Object object = getFirstQueryResult(query);
        if(object!=null) result = Optional.of((Integer) object);
        return result;
    }

    private Object getFirstQueryResult(Query query) {
        List<Object> queryResult = (List<Object>) query.getResultList();
        if (queryResult != null && !queryResult.isEmpty()) {
            return queryResult.get(0);
        }
        return null;
    }

    private List<Integer> getInstrumentTypeIds() {
        List instrumentTypeIds = new ArrayList<Integer>();
        EnumSet.allOf(InstrumentType.class).stream().filter(i->i.getTypeGroup()== InstrumentTypeGroup.SECURITY)
               .forEach(i-> instrumentTypeIds.add(i.getValue()));
        return instrumentTypeIds;
    }

}
