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
import de.hf.dac.io.efmb.impl.EntityManagerFactoryBuilderImpl;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

import static javax.persistence.spi.PersistenceUnitTransactionType.JTA;

@OsgiServiceProvider(classes = {EntityManagerFactorySetup.class})
@Singleton
public class EntityManagerFactorySetupImpl implements EntityManagerFactorySetup {

    @Inject
    EntityManagerFactoryBuilder efb;

    //@Inject @OsgiService(filter = "(&(osgi.jdbc.driver.class=com.sybase.jdbc4.jdbc.SybDriver))" )
    DataSourceFactory sybaseDataSourceFactory;

    //@Inject @OsgiService(filter = "(&(osgi.jdbc.driver.class=oracle.jdbc.OracleDriver))" )
    DataSourceFactory oracleDataSourceFactory;

    @Inject @OsgiService(filter = "(&(osgi.jdbc.driver.class=org.h2.Driver))" )
    DataSourceFactory h2Embedded;

    @Inject @OsgiService(filter = "(&(osgi.jdbc.driver.class=org.postgresql.Driver))" )
    DataSourceFactory postgresDataSourceFactory;



    private Map<String,Object> buildDBProperties(String persistenceUnit, Class<?>[] entities, ClassLoader[] classLoaders, String jdbcUrl, String user,
        String password, String jtaPlatform, String driverClass, String databaseDialectClass, Properties extraHibernateProperties)
            throws SQLException {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(EntityManagerFactoryBuilderImpl.persistenceUnitName,persistenceUnit);
        List<String> managedClasses = new ArrayList<>();
        if(entities!=null) {
            for(Class<?> c : entities) {
                managedClasses.add(c.getCanonicalName());
            }
        }
        props.put(EntityManagerFactoryBuilderImpl.managedClassNames,managedClasses);
        Properties persistenceUnitProperties = new Properties();
        persistenceUnitProperties.put("hibernate.dialect", databaseDialectClass);
        persistenceUnitProperties.put("hibernate.transaction.jta.platform",jtaPlatform);

        List<ClassLoader> all = new ArrayList<>();
        all.addAll(Arrays.asList(classLoaders));
        all.add(this.getClass().getClassLoader());

        if(classLoaders != null) {
            persistenceUnitProperties.put("hibernate.classloaders", new HashSet<>(all));
            props.put("joinedClassloader", classLoaders[0]);

            // look for compile time generated list of javax.persistence.Entity annotatedClasses
            Bundle bundle = ((BundleReference) classLoaders[0]).getBundle();
            String[] annotatedDomainClasses = new String[] {bundle.getHeaders().get("AnnotatedDomainClasses"), bundle.getHeaders().get("AnnotatedDomainSuperClasses")};

            for (String annotatedDomainClass : annotatedDomainClasses) {
                if (annotatedDomainClass != null && annotatedDomainClass.length() != 0 ) {
                    String[] classNames = annotatedDomainClass.split(",");
                    for (String className : classNames) {
                        managedClasses.add(className);
                    }
                }
            }
        }

        persistenceUnitProperties.put("hibernate.connection.url",jdbcUrl);
        persistenceUnitProperties.put("hibernate.connection.user",user);
        persistenceUnitProperties.put("hibernate.connection.password",password);
        persistenceUnitProperties.put("hibernate.connection.driver_class", driverClass);

        if (extraHibernateProperties != null) {
            persistenceUnitProperties.putAll(extraHibernateProperties);
        }

        props.put(EntityManagerFactoryBuilderImpl.persistenceUnitProperties, persistenceUnitProperties);
        props.put("javax.persistence.jdbc.driver", driverClass);
        props.put("javax.persistence.transactionType", JTA.name());

        return props;
    }

    private static Properties getJdbcProps() {
        Properties ret = new Properties();
        return ret;
    }

    private Object getDataSourceFromJndi(String url) {
        Object ds = null;
        Context context = null;
        try {
            context = new InitialContext();
            ds = context.lookup(url);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    @Override
    public EntityManagerFactory buildEntityManagerFactory(String persistenceUnit,
        ClassLoader[] classLoaders,
        DatabaseInfo dbi) throws SQLException {
        return buildEntityManagerFactory(persistenceUnit, null, classLoaders, dbi);
    }

    @Override
    public EntityManagerFactory buildEntityManagerFactory(String persistenceUnit,
        Class<?>[] entities,
        ClassLoader[] classLoaders,
        DatabaseInfo dbi) throws SQLException {
        Object dataSource = null;
        String user = null;
        String password = null;
        String url = null;
        String jtaPlatform = null;
        String driver = null;
        String dialect = null;
        DatabaseInfo db = null;
        if(dbi.getJndiUrl() == null) {
            db = dbi;
        } else {
            Object wrapperDataSource = getDataSourceFromJndi(dbi.getJndiUrl());
            db = getDBInfo(wrapperDataSource);
        }

        url = db.getUrl();
        user = db.getUser();
        password = db.getPassword();

        if(org.osgi.framework.FrameworkUtil.getBundle(EntityManagerFactorySetupImpl.class) == null ) {
            jtaPlatform = "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform";
        } else {
            jtaPlatform = "org.hibernate.osgi.OsgiJtaPlatform";
        }

        driver = db.getDriver();

        if(driver.matches("com.sybase.jdbc4.jdbc.SybDriver")) {
            dataSource = sybaseDataSourceFactory.createDataSource(getJdbcProps());
            dialect = "org.hibernate.dialect.SybaseASE157Dialect";
        } else if(db.getDriver().matches("oracle.jdbc.OracleDriver")) {
            dialect = "org.hibernate.dialect.Oracle10gDialect";
            dataSource = oracleDataSourceFactory.createDataSource(getJdbcProps());
        } else if (db.getDriver().matches("org.h2.Driver")) {
            dialect = "org.hibernate.dialect.H2Dialect";
            dataSource = h2Embedded.createDataSource(getJdbcProps());
        } else if (db.getDriver().matches("org.postgresql.Driver")) {
            dialect = "org.hibernate.dialect.PostgreSQL82Dialect";
            dataSource = postgresDataSourceFactory.createDataSource(getJdbcProps());
        } else {
            throw new IllegalArgumentException("Unhandled Driver " + driver);
        }

        if(db.getDialect()!=null){
            dialect = db.getDialect();
        }

        Map<String,Object> props = buildDBProperties(persistenceUnit,entities,classLoaders,url,user,password, jtaPlatform, driver, dialect, dbi.getExtraHibernateProperties());

        props.put("javax.persistence.JtaDataSource",dataSource);
        return efb.createEntityManagerFactory(props);

    }

    private static Iterable<Field> getFieldsUpTo(Class<?> startClass,
        Class<?> exclusiveParent) {

        List<Field> currentClassFields = new ArrayList<>(Arrays.asList(startClass.getDeclaredFields()));
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null &&
            (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            List<Field> parentClassFields =
                (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }

    private DatabaseInfo getDBInfo(Object wrapperDataSource) {
        Field connectionFactoryField = null;
        Object connectionFactory = null;
        String url = null;
        String user = null;
        String password = null;
        String driver = null;
        try {
            Class<?> clazz = wrapperDataSource.getClass();
            connectionFactoryField = clazz.getDeclaredField("mcf");
            connectionFactoryField.setAccessible(true);
            connectionFactory = connectionFactoryField.get(wrapperDataSource);
            Iterable<Field> fields = getFieldsUpTo(connectionFactory.getClass(),Object.class);
            for(Field field : fields) {
                field.setAccessible(true);
                if(field.getName().equals("connectionURL")) {
                    url = (String)field.get(connectionFactory);
                }
                if(field.getName().equals("password")) {
                    password = (String)field.get(connectionFactory);
                }
                if(field.getName().equals("userName")) {
                    user = (String)field.get(connectionFactory);
                }
                if(field.getName().equals("driverClass")) {
                    driver = (String)field.get(connectionFactory);
                }
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        DatabaseInfo ret = null;

        new DatabaseInfo(url,user,password,driver,"","");
        return ret;
    }


}
