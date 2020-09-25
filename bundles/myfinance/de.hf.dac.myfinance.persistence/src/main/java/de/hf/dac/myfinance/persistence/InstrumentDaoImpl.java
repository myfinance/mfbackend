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

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import java.time.LocalDate;
import java.util.*;

public class InstrumentDaoImpl extends BaseDao<Instrument> implements InstrumentDao {


    @Inject
    public InstrumentDaoImpl(@Named(EnvTarget.MDB) EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
    }

    @Override
    public List<Instrument> listInstruments() {
        return listQueryResult("select a FROM Instrument a");
    }

    @Override
    public Optional<Instrument> getInstrument(int instrumentId) {
        Optional<Instrument> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Instrument a WHERE instrumentid = :instrumentid");
            query.setParameter("instrumentid", instrumentId);
            result = getFirstQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public  List<Cashflow> listInstrumentCashflows(int instrumentId){
        List<Cashflow> returnValue;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            returnValue = getCashflows(instrumentId);
        } finally {
            marketDataEm.close();
        }
        return returnValue;
    }

    private List<Cashflow> getCashflows(int instrumentId) {
        List<Cashflow> returnValue;
        Query query = marketDataEm.createQuery("select a FROM Cashflow a WHERE a.instrument.instrumentid = :instrumentid");
        query.setParameter("instrumentid", instrumentId);
        returnValue=(List<Cashflow>) query.getResultList();
        return returnValue;
    }

    @Override
    public  Map<LocalDate, List<Cashflow>> getInstrumentCashflowMap(int instrumentId){
        Map<LocalDate, List<Cashflow>> returnValue = new HashMap<>();
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            getCashflows(instrumentId).forEach(x->{
                List<Cashflow> cashflows = new ArrayList();
                cashflows.add(x);
                if(returnValue.containsKey(x.getTransaction().getTransactiondate())) {
                    cashflows.addAll(returnValue.get(x.getTransaction().getTransactiondate()));
                    returnValue.remove(x.getTransaction().getTransactiondate());
                }
                returnValue.put(x.getTransaction().getTransactiondate(), cashflows);
            });
        } finally {
            marketDataEm.close();
        }
        return returnValue;
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
            result = getFirstQueryResult(query);
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
            result = getFirstQueryResult(query);
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
        save(instrument);
    }

    @Override
    public void updateInstrument(int instrumentId, String description, boolean isActive){
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Instrument a WHERE instrumentid = :instrumentid");
            query.setParameter("instrumentid", instrumentId);
            Optional<Instrument> instrument = getFirstQueryResult(query);
            Instrument newInstrument = instrument.get();
            newInstrument.setDescription(description);
            newInstrument.setIsactive(isActive);
            marketDataEm.getTransaction().begin();
            marketDataEm.persist(newInstrument);
            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
    }

    @Override
    public void saveSymbol(SecuritySymbols symbol) {
        save(symbol);
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
            Query query = marketDataEm.createQuery("select i FROM Instrument i JOIN InstrumentGraphEntry a ON i.instrumentid=a.id.descendant WHERE a.id.ancestor= :instrumentid and a.id.edgetype= :edgetype and a.pathlength>0");
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
            result = getFirstQueryResult(query);
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
        save(instrumentGraphEntry);
    }

    private Optional<Equity> getFirstEquityQueryResult(Query query) {
        Optional<Equity> result = Optional.empty();
        Object object = getFirstQueryObjectResult(query);
        if(object!=null) result = Optional.of((Equity) object);
        return result;
    }

    private Optional<Source> getFirstSourceQueryResult(Query query) {
        Optional<Source> result = Optional.empty();
        Object object = getFirstQueryObjectResult(query);
        if(object!=null) result = Optional.of((Source) object);
        return result;
    }
}
