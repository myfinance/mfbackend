/** ----------------------------------------------------------------------------
 *
 * ---          Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EntityManagerFactoryBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.efmb.impl;


import org.ops4j.pax.cdi.api.OsgiService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Singleton
public class EntityManagerFactoryBuilderImpl implements EntityManagerFactoryBuilder {
    private static final String JAVAX_PERSISTENCE_JDBC_DRIVER = "javax.persistence.jdbc.driver";
    private static final String JAVAX_PERSISTENCE_JTA_DATASOURCE = "javax.persistence.jtaDataSource";
    private static final String JAVAX_PERSISTENCE_NON_JTA_DATASOURCE = "javax.persistence.nonJtaDataSource";
    private static final String JAVAX_PERSISTENCE_TX_TYPE = "javax.persistence.transactionType";

    public static final String persistenceUnitName = "persistenceUnitName";
    public static final String managedClassNames = "managedClassNames";
    public static final String persistenceUnitProperties = "persistenceUnitProperties";

    @OsgiService
    @Inject
    private PersistenceProvider provider;

    public EntityManagerFactory createEntityManagerFactory(Map<String, Object> props) {
        props = new HashMap<String, Object>(props);

        String persistenceUnitName;
        List<String> managedClassNames;
        Properties persistenceUnitProperties;

        Object o = props.get(EntityManagerFactoryBuilderImpl.persistenceUnitName);
        if(o instanceof String) {
            persistenceUnitName = (String) o;
            props.remove(EntityManagerFactoryBuilderImpl.persistenceUnitName);
        } else {
            throw new IllegalArgumentException("No " + EntityManagerFactoryBuilderImpl.persistenceUnitName + " found");
        }

        o = props.get(EntityManagerFactoryBuilderImpl.managedClassNames);
        if(o instanceof List<?>) {
            managedClassNames = (List<String>) o;
            props.remove(EntityManagerFactoryBuilderImpl.managedClassNames);
        } else {
            managedClassNames = new ArrayList<String>();
        }

        o = props.get(EntityManagerFactoryBuilderImpl.persistenceUnitProperties);
        if(o instanceof Properties) {
            persistenceUnitProperties = (Properties) o;
            props.remove(EntityManagerFactoryBuilderImpl.persistenceUnitProperties);
        } else {
            persistenceUnitProperties = new Properties();
        }

        PersistenceUnitInfoImpl persistenceUnitInfoImpl = null;
        if(org.osgi.framework.FrameworkUtil.getBundle(EntityManagerFactoryBuilderImpl.class) == null) {
            persistenceUnitInfoImpl = new PersistenceUnitInfoImpl(Thread.currentThread().getContextClassLoader(),persistenceUnitName,PersistenceUnitTransactionType.JTA);
        } else {
            // we are in OSGi conmtainer
            Bundle bundle =
                BundleReference.class.cast(EntityManagerFactoryBuilderImpl.class.getClassLoader())
                    .getBundle();

            persistenceUnitInfoImpl = new OSGiPersistenceUnit(bundle,persistenceUnitName,PersistenceUnitTransactionType.JTA);
        }

        persistenceUnitInfoImpl.setExcludeUnlisted(true);
        for(String m : managedClassNames) {
            persistenceUnitInfoImpl.addClassName(m);
        }

        for(Object e : persistenceUnitProperties.keySet()) {
            persistenceUnitInfoImpl.addProperty((String)e,persistenceUnitProperties.get(e));
        }

        String driver = (String)props.get(JAVAX_PERSISTENCE_JDBC_DRIVER);
        if (driver == null) {
            throw new IllegalArgumentException("No driver found");
        }

        // Handle overridden datasources in a provider agnostic way
        // This isn't necessary for EclipseLink, but Hibernate and
        // OpenJPA both need some extra help.
        o = props.get(JAVAX_PERSISTENCE_JTA_DATASOURCE);
        if(o instanceof DataSource) {
            persistenceUnitInfoImpl.setJtaDataSource((DataSource) o);
            props.remove(JAVAX_PERSISTENCE_JTA_DATASOURCE);
        }

        o = props.get(JAVAX_PERSISTENCE_NON_JTA_DATASOURCE);
        if(o instanceof DataSource) {
            persistenceUnitInfoImpl.setNonJtaDataSource((DataSource) o);
            props.remove(JAVAX_PERSISTENCE_NON_JTA_DATASOURCE);
        }

        o = props.get(JAVAX_PERSISTENCE_TX_TYPE);
        if(o instanceof PersistenceUnitTransactionType) {
            persistenceUnitInfoImpl.setTransactionType((PersistenceUnitTransactionType) o);
        } else if (o instanceof String) {
            persistenceUnitInfoImpl.setTransactionType(
                PersistenceUnitTransactionType.valueOf((String) o));
        }

        return provider.createContainerEntityManagerFactory(persistenceUnitInfoImpl, props);
    }

}
