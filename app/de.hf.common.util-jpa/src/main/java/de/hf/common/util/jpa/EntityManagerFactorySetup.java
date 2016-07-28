/** ----------------------------------------------------------------------------
 *
 * ---                                 ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EntityManagerFactorySetup.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 18.07.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.common.util.jpa;

import de.hf.common.util.jpa.impl.EntityManagerFactoryBuilderImpl;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

import static javax.persistence.spi.PersistenceUnitTransactionType.JTA;


@Component(service = EntityManagerFactorySetup.class )
public class EntityManagerFactorySetup {

    EntityManagerFactoryBuilder efb;
    @Reference(cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.STATIC)
    protected void setEntityManagerFactoryBuilder(EntityManagerFactoryBuilder efb) {
        this.efb = efb;
    }

    /*DataSourceFactory postgresDataSourceFactory;
    @Reference(cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.STATIC,
        target = "(osgi.jdbc.driver.name=PostgreSQL JDBC Driver)")
    protected void setPostgresDataSourceFactory(DataSourceFactory dsf) {
        this.postgresDataSourceFactory = dsf;
    }*/

    /*@H2Qualifier
    DataSourceFactory h2DataSourceFactory;
    @Reference(cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.STATIC,
        target = "(osgi.jdbc.driver.name=h2)")
    protected void setH2DataSourceFactory(DataSourceFactory dsf) {
        this.h2DataSourceFactory = dsf;
    }*/

    /*DataSource dataSource;
    @Reference(target = "(dataSourceName=marketdatapostgres)")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }*/



    private Map<String,Object> getDBProperties(String persistenceUnit, Class<?>[] entities, String jdbcUrl, String user, String password, String jtaPlatform, String driverClass, String databaseDialectClass)
        throws SQLException {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(EntityManagerFactoryBuilderImpl.CCR_persistenceUnitName,persistenceUnit);
        List<String> managedClasses = new ArrayList<>();
        for(Class<?> c : entities) {
            managedClasses.add(c.getCanonicalName());
        }
        props.put(EntityManagerFactoryBuilderImpl.CCR_managedClassNames,managedClasses);
        Properties persistenceUnitProperties = new Properties();
        persistenceUnitProperties.put("hibernate.dialect", databaseDialectClass);
        persistenceUnitProperties.put("hibernate.transaction.jta.platform",jtaPlatform);
        persistenceUnitProperties.put("hibernate.connection.url",jdbcUrl);
        persistenceUnitProperties.put("hibernate.connection.user",user);
        persistenceUnitProperties.put("hibernate.connection.password",password);
        persistenceUnitProperties.put("hibernate.connection.driver_class", driverClass);
        props.put(EntityManagerFactoryBuilderImpl.CCR_persistenceUnitProperties, persistenceUnitProperties);
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

    public EntityManagerFactory buildEntityManagerFactory(String persistenceUnit, Class<?>[] entities, DatabaseInfo dbi, String dataSourceJndiUrl)
            throws SQLException {
        Object dataSource = null;
        String user = null;
        String password = null;
        String url = null;
        String jtaPlatform = null;
        String driver = null;
        String dialect = null;
        DatabaseInfo db = null;
        if(dataSourceJndiUrl == null) {
            db = dbi;
        } else {
            Object wrapperDataSource = getDataSourceFromJndi(dataSourceJndiUrl);
            db = getDBInfo(wrapperDataSource);
        }

        url = db.getUrl();
        user = db.getUser();
        password = db.getPassword();

        jtaPlatform = "org.hibernate.osgi.OsgiJtaPlatform";


        driver = db.getDriver();

        Map<String,Object> props = null;
        if(driver.matches("com.sybase.jdbc4.jdbc.SybDriver")) {
            dialect = "org.hibernate.dialect.SybaseASE157Dialect";
        } else if(db.getDriver().matches("oracle.jdbc.OracleDriver")) {
            dialect = "org.hibernate.dialect.Oracle10gDialect";
        } else if(db.getDriver().matches("org.postgresql.Driver")) {
            //dataSource = postgresDataSourceFactory.createDataSource(getJdbcProps());
            dialect = "org.hibernate.dialect.PostgreSQLDialect";
        } else if(db.getDriver().matches("org.h2.Driver")) {
            dialect = "org.hibernate.dialect.H2Dialect";
        } else {
            throw new IllegalArgumentException("Unhandled Driver " + driver);
        }
        props = getDBProperties(persistenceUnit,entities,url,user,password, jtaPlatform, driver, dialect);
        //props.put("javax.persistence.JtaDataSource",dataSource); //String vergelich funzt nicht. deshalb:
        /* mit diesen Zeilen wird die Datasource gesetzt aber mit dieser kann keine Verbindung aufgebaut werden da die Properties nicht gesetzt werden
        dazu muss getJdbcProps mit logik versehen werden. dann funbzt es aber immer noch nicht da mein aktueller windows user statt der übergebene user verwendet wird
        */
        //props.put(EntityManagerFactoryBuilderImpl.JAVAX_PERSISTENCE_JTA_DATASOURCE,dataSource);
        //props.put(EntityManagerFactoryBuilderImpl.JAVAX_PERSISTENCE_NON_JTA_DATASOURCE,dataSource);

        return efb.createEntityManagerFactory(props);

    }

    public static Iterable<Field> getFieldsUpTo(Class<?> startClass,
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
            //cf = (LocalManagedConnectionFactory)f.get(wrapperDataSource);
            connectionFactory = connectionFactoryField.get(wrapperDataSource);
            Iterable<Field> fields = getFieldsUpTo(connectionFactory.getClass(),Object.class);
            for(Field field : fields) {
                field.setAccessible(true);
                if(field.getName() == "connectionURL") {
                    url = (String)field.get(connectionFactory);
                }
                if(field.getName() == "password") {
                    password = (String)field.get(connectionFactory);
                }
                if(field.getName() == "userName") {
                    user = (String)field.get(connectionFactory);
                }
                if(field.getName() == "driverClass") {
                    driver = (String)field.get(connectionFactory);
                }
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        DatabaseInfo ret = new DatabaseInfo(url,user,password,driver,"sybase","");
        //DatabaseInfo ret = new DatabaseInfo(cf.getConnectionURL(),cf.getUserName(),cf.getPassword(),cf.getDriverClass(),"sybase","");
        return ret;
    }


}

