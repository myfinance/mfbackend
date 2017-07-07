/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : WrappedEntityManagerFactory.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.efmb.tx;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleReference;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.Map;

public class WrappedEntityManagerFactory implements EntityManagerFactory
{

    private static final Logger LOG = LoggerFactory.getLogger(WrappedEntityManagerFactory.class);

    private static ThreadLocal<OsgiTxProvider> txProvider;
    private EntityManagerFactory emfDelegate;

    public WrappedEntityManagerFactory(TransactionManager jtaManager, EntityManagerFactory emfDelegate) {
        this.emfDelegate = emfDelegate;

        txProvider = ThreadLocal.withInitial(() -> {
            BundleContext context = ((BundleReference) WrappedEntityManagerFactory.class.getClassLoader()).getBundle().getBundleContext();
            ServiceReference txRef = context.getServiceReference(UserTransaction.class.getName());
            UserTransaction utx = (UserTransaction) context.getService(txRef);
            // never timeout
            try {
                utx.setTransactionTimeout(6000);
            } catch (SystemException e) {
                LOG.error("Unspecified error",e);
            }
            return new OsgiTxProvider(jtaManager, utx);
        });

    }

    @Override
    public EntityManager createEntityManager() {
        // need to intercept this and wrap our own Entitymanager
        return new WrappedEntityManager(txProvider.get(), emfDelegate.createEntityManager(), this);
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        return emfDelegate.createEntityManager(map);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        return emfDelegate.createEntityManager(synchronizationType);
    }


    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        return emfDelegate.createEntityManager(synchronizationType, map);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return emfDelegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return emfDelegate.getMetamodel();
    }

    @Override
    public boolean isOpen() {
        return emfDelegate.isOpen();
    }

    @Override
    public void close() {
        emfDelegate.close();
    }

    @Override
    public Map<String, Object> getProperties() {
        return emfDelegate.getProperties();
    }

    @Override
    public Cache getCache() {
        return emfDelegate.getCache();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return emfDelegate.getPersistenceUnitUtil();
    }

    @Override
    public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {
        emfDelegate.addNamedEntityGraph(s, entityGraph);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return emfDelegate.unwrap(aClass);
    }

    @Override
    public void addNamedQuery(String s, Query query) {
        emfDelegate.addNamedQuery(s, query);
    }
}
