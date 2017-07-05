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


import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleReference;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static de.hf.dac.io.efmb.EntityManagerFactorySetupImpl.JAVAX_PERSISTENCE_JDBC_DRIVER;
import static de.hf.dac.io.efmb.EntityManagerFactorySetupImpl.JAVAX_PERSISTENCE_JTA_DATASOURCE;
import static de.hf.dac.io.efmb.EntityManagerFactorySetupImpl.JAVAX_PERSISTENCE_NON_JTA_DATASOURCE;
import static de.hf.dac.io.efmb.EntityManagerFactorySetupImpl.JAVAX_PERSISTENCE_TX_TYPE;

@Component(name = "DAC.EntityManagerFactoryBuilderImpl", service = EntityManagerFactoryBuilder.class)
public class EntityManagerFactoryBuilderImpl implements EntityManagerFactoryBuilder {
    public static final String CCR_persistenceUnitName = "persistenceUnitName";
    public static final String CCR_managedClassNames = "managedClassNames";
    public static final String CCR_persistenceUnitProperties = "persistenceUnitProperties";
    public static final String JOINED_CLASSLOADER = "joinedClassloader";

    @Activate
    protected void activate(ComponentContext componentContext,
        BundleContext bundleContext,
        Map<String, ?> properties) {
        final ServiceReference<PersistenceProvider> serviceReference = bundleContext.getServiceReference(PersistenceProvider.class);
        provider = bundleContext.getService(serviceReference);
    }

    private PersistenceProvider provider;

    public EntityManagerFactory createEntityManagerFactory(Map<String, Object> props) {
        props = new HashMap<>(props);

        String persistenceUnitName;
        List<String> managedClassNames;
        Properties persistenceUnitProperties;

        Object o = props.get(CCR_persistenceUnitName);
        if(o instanceof String) {
            persistenceUnitName = (String) o;
            props.remove(CCR_persistenceUnitName);
        } else {
            throw new IllegalArgumentException("No " + CCR_persistenceUnitName + " found");
        }

        o = props.get(CCR_managedClassNames);
        if(o instanceof List<?>) {
            managedClassNames = (List<String>) o;
            props.remove(CCR_managedClassNames);
        } else {
            managedClassNames = new ArrayList<>();
        }

        o = props.get(CCR_persistenceUnitProperties);
        if(o instanceof Properties) {
            persistenceUnitProperties = (Properties) o;
            props.remove(CCR_persistenceUnitProperties);
        } else {
            persistenceUnitProperties = new Properties();
        }
        // we are in OSGi conmtainer
        Bundle bundle =
            BundleReference.class.cast(EntityManagerFactoryBuilderImpl.class.getClassLoader())
                .getBundle();

        PersistenceUnitInfoImpl persistenceUnitInfoImpl = new OSGiPersistenceUnit(bundle,persistenceUnitName,PersistenceUnitTransactionType.JTA);

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

        o = props.get(JOINED_CLASSLOADER);
        if(o instanceof ClassLoader) {
            persistenceUnitInfoImpl.classLoader = (ClassLoader)o;
            props.remove(JOINED_CLASSLOADER);
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

