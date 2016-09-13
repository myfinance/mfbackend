/** ----------------------------------------------------------------------------
 *
 * ---          Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : GenericEntityManager.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 27.07.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.common.util.jpa;

import de.hf.marketdataprovider.domain.Instrument;
import de.hf.marketdataprovider.domain.Product;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Component
public class GenericEntityManager implements EntityManager {

    private EntityManager em = null;

    EntityManagerFactorySetup efs;
    @Reference(cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.STATIC)
    protected void setEntityManagerFactorySetup(EntityManagerFactorySetup efs) {
        this.efs = efs;
    }

    @Activate
    protected void activate(  ) {
        DatabaseInfo db = new DatabaseInfo("jdbc:postgresql://localhost:5432/marketdata", "postgres", "vulkan", "org.postgresql.Driver");
        Class<?>[] entities = new Class<?>[]{Product.class, Instrument.class};
        EntityManagerFactory sdbFactory=null;
        try {
            sdbFactory = efs.buildEntityManagerFactory("ServiceConfiguration", entities, db,null );
            em = sdbFactory.createEntityManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void persist(Object o) {
        em.persist(o);
    }

    @Override
    public <T> T merge(T t) {
        return em.merge(t);
    }

    @Override
    public void remove(Object o) {
        em.remove(o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return em.find(aClass, o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return em.find(aClass, o, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return em.find(aClass, o, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return em.find(aClass, o, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return em.getReference(aClass, o);
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        em.setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return em.getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        em.lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        em.lock(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o) {
        em.refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        em.refresh(o, map);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        em.refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        em.refresh(o, lockModeType, map);
    }

    @Override
    public void clear() {
        em.clear();
    }

    @Override
    public void detach(Object o) {
        em.detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return em.contains(o);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return em.getLockMode(o);
    }

    @Override
    public void setProperty(String s, Object o) {
        em.setProperty(s,o);
    }

    @Override
    public Map<String, Object> getProperties() {
        return em.getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return em.createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return em.createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return em.createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return em.createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return em.createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return em.createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return em.createNamedQuery(s,aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return em.createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return em.createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return em.createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return em.createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return em.createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return em.createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return em.createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        em.joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return em.isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return em.unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return em.getDelegate();
    }

    @Override
    public void close() {
        em.close();
    }

    @Override
    public boolean isOpen() {
        return em.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return em.getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return em.getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return em.getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return em.createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return em.createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return em.getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return em.getEntityGraphs(aClass);
    }
}
