/** ----------------------------------------------------------------------------
 *
 * ---           Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EntityManagerFactorySetupImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.efmb;

import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentConfiguration;
import de.hf.dac.io.efmb.impl.EntityManagerFactoryBuilderImpl;
import org.ops4j.pax.jdbc.pool.common.PooledDataSourceFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static javax.persistence.spi.PersistenceUnitTransactionType.JTA;

@Component(service = {EntityManagerFactorySetup.class}, name = "DAC.EntityManagerFactorySetup", scope = ServiceScope.SINGLETON )
public class EntityManagerFactorySetupImpl implements EntityManagerFactorySetup {

    public static final String JAVAX_PERSISTENCE_JDBC_DRIVER = "javax.persistence.jdbc.driver";
    public static final String JAVAX_PERSISTENCE_JTA_DATASOURCE = "javax.persistence.jtaDataSource";
    public static final String JAVAX_PERSISTENCE_NON_JTA_DATASOURCE = "javax.persistence.nonJtaDataSource";
    public static final String JAVAX_PERSISTENCE_TX_TYPE = "javax.persistence.transactionType";

    @Reference
    EntityManagerFactoryBuilder efb;

    @Reference(target = "(&(osgi.jdbc.driver.class=org.h2.Driver))" )
    DataSourceFactory h2Embedded;

    @Reference(target = "(&(osgi.jdbc.driver.class=org.postgresql.Driver))" )
    DataSourceFactory postgresDataSourceFactory;

    @Reference(target = "(&(xa=false))")
    PooledDataSourceFactory pooledDataSourceFactory;

    @Reference
    EnvironmentConfiguration configuration;

    private Map<String,WeakReference<EntityManagerFactory>> entityManagerFactories = new ConcurrentHashMap<>();

    private static Properties getPooledProps(String dataSourceName, String url, String user, String password, PoolSize poolSize) {
        Properties props = new Properties();
        props.setProperty(DataSourceFactory.JDBC_URL, url);
        props.setProperty(DataSourceFactory.JDBC_USER,user);
        props.setProperty(DataSourceFactory.JDBC_PASSWORD,password);
        //props.setProperty(DataSourceFactory.JDBC_DATASOURCE_NAME, dataSourceName);
        setC3P0Props(props, poolSize, dataSourceName);
        return props;
    }


    private static void setC3P0Props(Properties ret, PoolSize poolSize, String dataSourceName) {
        // http://www.mchange.com/projects/c3p0/#configuration_properties

        if (dataSourceName != null && !dataSourceName.isEmpty()) {
            ret.setProperty("c3p0.dataSourceName", dataSourceName);
        }


        Integer initialPoolSize;
        Integer maxPoolSize;
        Integer minPoolSize;
        Integer acquireIncrement;

        // Pool Size
        switch (poolSize) {
            case SMALL:
                initialPoolSize = 3;
                maxPoolSize = 8;
                minPoolSize = 3;
                acquireIncrement = 1;
                break;
            case MEDIUM:
                initialPoolSize = 6;
                maxPoolSize = 20;
                minPoolSize = 6;
                acquireIncrement = 2;
                break;
            case LARGE:
                initialPoolSize = 20;
                maxPoolSize = 100;
                minPoolSize = 20;
                acquireIncrement = 3;
                break;
            default:
                throw new IllegalArgumentException("Unknown PoolSize " + poolSize);
        }

        ret.setProperty("c3p0.initialPoolSize", initialPoolSize.toString());
        ret.setProperty("c3p0.maxPoolSize", maxPoolSize.toString());
        ret.setProperty("c3p0.minPoolSize", minPoolSize.toString());
        ret.setProperty("c3p0.acquireIncrement", acquireIncrement.toString());

        // Basic Pool Configuration

        ret.setProperty("c3p0.checkoutTimeout", "30000"); // milliseconds

        // Managing Pool Size and Connection Age
        ret.setProperty("c3p0.maxIdleTime", "7200"); // seconds
        ret.setProperty("c3p0.maxConnectionAge", "7200"); // seconds
        ret.setProperty("c3p0.maxIdleTimeExcessConnections", "1800"); // seconds

        // Recovery
        ret.setProperty("c3p0.acquireRetryAttempts", "10");
        ret.setProperty("c3p0.acquireRetryDelay", "4000"); // milliseconds
        ret.setProperty("c3p0.breakAfterAcquireFailure", "false");

        // Configuring Connection Testing

        ret.setProperty("c3p0.idleConnectionTestPeriod", "30"); // seconds
        ret.setProperty("c3p0.testConnectionOnCheckin", "true");
        ret.setProperty("c3p0.testConnectionOnCheckout", "false");
        // testConnectionOnCheckout is costly
        // better use combination
        // of (async!) idleConnectionTestPeriod testConnectionOnCheckin

        // Internal Configuration

        ret.setProperty("c3p0.numHelperThreads", "6"); // async cleanup

        // Experimental

        // Statement Caching?
        //ret.setProperty("c3p0.maxStatementsPerConnection ","20");

        ret.setProperty("c3p0.unreturnedConnectionTimeout  ", "14400"); // seconds


    }

    private Map<String, Object> buildDBProperties(String persistenceUnitId, Class<?>[] entities, ClassLoader[] classLoaders, String user,
        String password, String jtaPlatform, String driverClass, String databaseDialectClass, Properties extraHibernateProperties)
        throws SQLException {



        List<ClassLoader> allClassLoaders = new ArrayList<>();
        allClassLoaders.addAll(Arrays.asList(classLoaders));
        allClassLoaders.add(this.getClass().getClassLoader());

        for (Class<?> entity : entities) {
            allClassLoaders.add(entity.getClassLoader());
        }

        List<String> managedClasses = getManagedClasses(entities, classLoaders);

        // https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html
        Properties persitenceUnitProperties = new Properties();
        persitenceUnitProperties.put("hibernate.dialect", databaseDialectClass);
        persitenceUnitProperties.put("hibernate.transaction.jta.platform", jtaPlatform);
        persitenceUnitProperties.put("hibernate.connection.username", user);
        persitenceUnitProperties.put("hibernate.connection.password", password);
        persitenceUnitProperties.put("hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION");
        persitenceUnitProperties.put("hibernate.transaction.auto_close_session", false);
        persitenceUnitProperties.put("hibernate.jdbc.batch_size", Integer.parseInt(configuration.getString("CCR", "HIBERNATE_BATCH_SIZE", "50")));
        persitenceUnitProperties.put("hibernate.classloaders", new HashSet<>(allClassLoaders));
        if (extraHibernateProperties != null) {
            persitenceUnitProperties.putAll(extraHibernateProperties);
        }

        Map<String, Object> emfbProperties = new HashMap<>();
        emfbProperties.put(EntityManagerFactoryBuilderImpl.CCR_persistenceUnitName, persistenceUnitId);


        emfbProperties.put(EntityManagerFactoryBuilderImpl.JOINED_CLASSLOADER, allClassLoaders.get(0));
        emfbProperties.put(EntityManagerFactoryBuilderImpl.CCR_managedClassNames, managedClasses);
        emfbProperties.put(EntityManagerFactoryBuilderImpl.CCR_persistenceUnitProperties, persitenceUnitProperties);
        emfbProperties.put(JAVAX_PERSISTENCE_JDBC_DRIVER, driverClass);
        emfbProperties.put(JAVAX_PERSISTENCE_TX_TYPE, JTA.name());

        return emfbProperties;
    }

    private static List<String> getManagedClasses(Class<?>[] entities, ClassLoader[] classLoaders) {
        List<String> managedClasses = new ArrayList<>();

        for (Class<?> c : entities) {
            managedClasses.add(c.getCanonicalName());
        }
        for (ClassLoader classLoader : classLoaders) {

            // look for compile time generated list of javax.persistence.Entity annotatedClasses
            Bundle bundle = ((BundleReference) classLoader).getBundle();
            String[] annotatedDomainClasses = new String[]{bundle.getHeaders().get("AnnotatedDomainClasses"), bundle.getHeaders().get("AnnotatedDomainSuperClasses")};

            for (String annotatedDomainClass : annotatedDomainClasses) {
                if (annotatedDomainClass != null && !annotatedDomainClass.isEmpty()) {
                    String[] classNames = annotatedDomainClass.split(",");
                    for (String className : classNames) {
                        if (!managedClasses.contains(className)) {
                            managedClasses.add(className);
                        }
                    }
                }
            }
        }
        return managedClasses;
    }


    @Override
    public EntityManagerFactory getOrCreateEntityManagerFactory(String persistenceUnitId,
        PoolSize poolSize,
        Class<?>[] entities,
        ClassLoader[] classLoaders,
        DatabaseInfo db) {
        if(entityManagerFactories.containsKey(persistenceUnitId)) {
            EntityManagerFactory emf = entityManagerFactories.get(persistenceUnitId).get();
            if(emf != null ) {
                return emf;
            }
            entityManagerFactories.remove(persistenceUnitId);
        }

        String user = null;
        String pwd = null;
        String url = null;
        String jtaPlatform = "org.hibernate.osgi.OsgiJtaPlatform";
        String driver = null;
        String dialect = null;

        url = db.getUrl();
        user = db.getUser();
        pwd = db.getPassword();

        driver = db.getDriver();

        try {
            DataSourceFactory dsf = null;

            if (db.getDriver().matches("org.h2.Driver")) {
                dialect = db.getDialect();
                dsf = h2Embedded;
            } else if (db.getDriver().matches("org.postgresql.Driver")) {
                dialect = "org.hibernate.dialect.PostgreSQL82Dialect";
                dsf = postgresDataSourceFactory;
            } else {
                throw new IllegalArgumentException("Unhandled Driver " + driver);
            }

            DataSource dataSource = poolSize == PoolSize.NO_POOL
                ? dsf.createDataSource(getUnpooledProps(persistenceUnitId, url, user,pwd ))
                : pooledDataSourceFactory.create(dsf, getPooledProps(persistenceUnitId, url,user , pwd, poolSize));

            Map<String, Object> emfbProps = buildDBProperties(persistenceUnitId, entities, classLoaders, user, pwd, jtaPlatform, driver, dialect, db.getExtraHibernateProperties());

            emfbProps.put(JAVAX_PERSISTENCE_JTA_DATASOURCE, dataSource);
            final EntityManagerFactory entityManagerFactory = efb.createEntityManagerFactory(emfbProps);
            entityManagerFactories.put(persistenceUnitId, new WeakReference<>(entityManagerFactory));
            return entityManagerFactory;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    private Properties getUnpooledProps(String dataSourceName, String url, String user, String password) {
        Properties props = new Properties();
        props.setProperty(DataSourceFactory.JDBC_URL,url);
        props.setProperty(DataSourceFactory.JDBC_USER,user);
        props.setProperty(DataSourceFactory.JDBC_PASSWORD,password);
        props.setProperty(DataSourceFactory.JDBC_DATASOURCE_NAME, dataSourceName);

        return props;
    }




}