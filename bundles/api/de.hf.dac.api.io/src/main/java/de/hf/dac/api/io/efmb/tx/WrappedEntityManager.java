/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : WrappedEntityManager.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.efmb.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.List;
import java.util.Map;

public class WrappedEntityManager implements EntityManager {

    private static final Logger LOG = LoggerFactory.getLogger(WrappedEntityManager.class);

    private final EntityManager entityManager;
    private final OsgiTxProvider txProvider;
    private final WrappedEntityManagerTransaction transaction;
    private final WrappedEntityManagerFactory wrappedEntityManagerFactory;

    public WrappedEntityManager(OsgiTxProvider txProvider, EntityManager entityManager, WrappedEntityManagerFactory wrappedEntityManagerFactory) {
        this.txProvider = txProvider;
        this.entityManager = entityManager;
        transaction = new WrappedEntityManagerTransaction(this, txProvider);
        this.wrappedEntityManagerFactory = wrappedEntityManagerFactory;
    }

    @Override
    public EntityTransaction getTransaction() {
        return transaction;
    }

    @Override
    public void persist(Object o) {
        entityManager.persist(o);
    }

    @Override
    public <T> T merge(T t) {
        return entityManager.merge(t);
    }

    @Override
    public void remove(Object o) {
        entityManager.remove(o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return entityManager.find(aClass, o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return entityManager.find(aClass, o, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return entityManager.find(aClass, o, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return entityManager.find(aClass, o, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return entityManager.getReference(aClass, o);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        entityManager.setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return entityManager.getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        entityManager.lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        entityManager.lock(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o) {
        entityManager.refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        entityManager.refresh(o, map);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        entityManager.refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        entityManager.refresh(o, lockModeType, map);
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    @Override
    public void detach(Object o) {
        entityManager.detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return entityManager.contains(o);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return entityManager.getLockMode(o);
    }

    @Override
    public void setProperty(String s, Object o) {
        entityManager.setProperty(s, o);
    }

    @Override
    public Map<String, Object> getProperties() {
        return entityManager.getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return entityManager.createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return entityManager.createQuery(criteriaQuery);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return entityManager.createQuery(s, aClass);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return entityManager.createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return entityManager.createQuery(criteriaDelete);
    }

    @Override
    public Query createNamedQuery(String s) {
        return entityManager.createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return entityManager.createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return entityManager.createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return entityManager.createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return entityManager.createNativeQuery(s, s1);
    }

    @Override
    public void joinTransaction() {
        entityManager.joinTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return entityManager.unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return entityManager.getDelegate();
    }

    @Override
    public void close() {
        try {
            Transaction tx = txProvider.removeTxForEntityManager(this);
            entityManager.close();
            if (tx != null) {
                txProvider.resumeAndJoin(this, tx);
            }
        } catch (SystemException e) {
            LOG.error("Unspecified error",e);
        }
    }

    @Override
    public boolean isOpen() {
        return entityManager.isOpen();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return wrappedEntityManagerFactory;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return entityManager.getMetamodel();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return entityManager.createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return entityManager.createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return entityManager.createStoredProcedureQuery(s, strings);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return entityManager.getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return entityManager.getEntityGraphs(aClass);
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return entityManager.createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return entityManager.createEntityGraph(s);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return entityManager.createNamedStoredProcedureQuery(s);
    }

    @Override
    public boolean isJoinedToTransaction() {
        return entityManager.isJoinedToTransaction();
    }
}
