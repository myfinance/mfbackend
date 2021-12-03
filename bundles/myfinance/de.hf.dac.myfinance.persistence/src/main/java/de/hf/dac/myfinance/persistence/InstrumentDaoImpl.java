/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
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
import java.util.*;

public class InstrumentDaoImpl extends BaseDao<Instrument> implements InstrumentDao {
    private static final String INSTRUMENTID = "instrumentid";

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
            query.setParameter(INSTRUMENTID, instrumentId);
            result = getFirstQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Instrument> getEquity(String isin) {
        Optional<Instrument> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM Instrument a WHERE instrumentTypeId= :instrumentTypeId and businesskey = :isin");
            query.setParameter("instrumentTypeId", InstrumentType.EQUITY.getValue());
            query.setParameter("isin", isin);
            result = getFirstQueryResult(query);
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Instrument> getSecurity(String businesskey) {
        var instrumentTypeIds=getInstrumentTypeIds();
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
        var instrumentTypeIds=getInstrumentTypeIds();
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
            Query query = marketDataEm.createQuery("select a FROM Currency a WHERE businesskey = :businesskey");
            query.setParameter("businesskey", currencyCode);
            result = getFirstQueryResult(query);
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
            query.setParameter(INSTRUMENTID, instrumentId);
            Optional<Instrument> instrument = getFirstQueryResult(query);
            if(instrument.isPresent()) {
                Instrument newInstrument = instrument.get();
                newInstrument.setDescription(description);
                newInstrument.setIsactive(isActive);
                marketDataEm.getTransaction().begin();
                marketDataEm.persist(newInstrument);
                marketDataEm.getTransaction().commit();
            }
        } finally {
            marketDataEm.close();
        }
    }

    @Override
    public void saveSymbol(SecuritySymbols symbol) {
        save(symbol);
    }

    @Override
    public String deleteSymbols(int symbolId) {
        String result = " symbol with id "+symbolId;
        try {

            marketDataEm = this.marketDataEmf.createEntityManager();
            marketDataEm.getTransaction().begin();
            SecuritySymbols securitySymbols = marketDataEm.find(SecuritySymbols.class, symbolId);
            result+=" ,symbol: '"+securitySymbols.getSymbol()+ " deleted";
            marketDataEm.remove(securitySymbols);

            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType) {
        List<InstrumentGraphEntry> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select a FROM InstrumentGraphEntry a WHERE a.id.descendant= :instrumentid and a.id.edgetype= :edgetype");
            query.setParameter(INSTRUMENTID, instrumentId);
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
            query.setParameter(INSTRUMENTID, instrumentId);
            query.setParameter("edgetype", edgeType);
            result = (List<Instrument>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength) {
        List<Instrument> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select i FROM Instrument i JOIN InstrumentGraphEntry a ON i.instrumentid=a.id.descendant WHERE a.id.ancestor= :instrumentid and a.id.edgetype= :edgetype and a.pathlength= :pathlength");
            query.setParameter(INSTRUMENTID, instrumentId);
            query.setParameter("edgetype", edgeType);
            query.setParameter("pathlength", pathlength);
            result = (List<Instrument>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public Optional<Instrument> getAccountPortfolio(int tenantId){
        return getFirstInstrumentByType(tenantId, EdgeType.TENANTGRAPH, InstrumentType.ACCOUNTPORTFOLIO);
    }

    @Override
    public Optional<Instrument> getBudgetPortfolio(int tenantId){
        return getFirstInstrumentByType(tenantId, EdgeType.TENANTGRAPH, InstrumentType.BUDGETPORTFOLIO);
    }

    protected Optional<Instrument> getFirstInstrumentByType(int tenantId, EdgeType edgetype, InstrumentType instrumentType){
        Optional<Instrument> result;
        try {
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery("select i FROM InstrumentGraphEntry a JOIN Instrument i ON i.instrumentid=a.id.descendant "
                + "WHERE a.id.ancestor= :instrumentid and a.id.edgetype= :edgetype and a.pathlength=1 and i.instrumentTypeId= :instrumenttype");
            query.setParameter(INSTRUMENTID, tenantId);
            query.setParameter("edgetype", edgetype);
            query.setParameter("instrumenttype", instrumentType.getValue());
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
            query.setParameter(INSTRUMENTID, instrumentId);
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

    @Override
    public void saveInstrumentProperty(InstrumentProperties instrumentProperty) {
        save(instrumentProperty);
    }

    @Override
    public List<InstrumentProperties> getInstrumentProperties(int instrumentId) {
        List<InstrumentProperties> result;
        try{
            marketDataEm = this.marketDataEmf.createEntityManager();
            Query query = marketDataEm.createQuery(
                "select a FROM InstrumentProperties a WHERE a.instrumentid= :instrumentId"
            );
            query.setParameter("instrumentId", instrumentId);
            result = (List<InstrumentProperties>) query.getResultList();
        } finally {
            marketDataEm.close();
        }
        return result;
    }

    @Override
    public String deleteInstrumentProperty(int instrumentPropertyId) {
        String result = " instrumentProperty with id "+instrumentPropertyId;
        try {

            marketDataEm = this.marketDataEmf.createEntityManager();
            marketDataEm.getTransaction().begin();
            InstrumentProperties instrumentProperty = marketDataEm.find(InstrumentProperties.class, instrumentPropertyId);
            result+=" ,type: '"+instrumentProperty.getPropertyname()+
                    "' ,value:" + instrumentProperty.getValue() + 
                    "' ,validfrom:" + instrumentProperty.getValidfrom() + " deleted";
            marketDataEm.remove(instrumentProperty);

            marketDataEm.getTransaction().commit();
        } finally {
            marketDataEm.close();
        }
        return result;
    }
}
