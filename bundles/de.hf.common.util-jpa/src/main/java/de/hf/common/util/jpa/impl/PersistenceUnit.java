/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : PersistenceUnit.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 18.07.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.common.util.jpa.impl;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created by xne0133 on 30.05.2016.
 */
public class PersistenceUnit implements PersistenceUnitInfo {
    protected ClassLoader classLoader;
    protected Set<String> classNames;
    protected String persistenceUnitName;
    protected Properties props;
    protected PersistenceUnitTransactionType transactionType;
    private boolean excludeUnlisted;
    private DataSource jtaDataSource;
    private String jtaDataSourceName;
    private DataSource nonJtaDataSource;
    private String nonJtaDataSourceName;
    private String persistenceProviderClassName;
    private String persistenceXMLSchemaVersion;
    private SharedCacheMode sharedCacheMode = SharedCacheMode.UNSPECIFIED;
    private ValidationMode validationMode = ValidationMode.NONE;

    public PersistenceUnit(ClassLoader classLoader, String persistenceUnitName, PersistenceUnitTransactionType transactionType) {
        this.classNames = new HashSet<String>();
        this.props = new Properties();
        this.persistenceUnitName = persistenceUnitName;
        this.transactionType = transactionType;
        this.classLoader = classLoader;
    }

    public void addClassName(String className) {
        this.classNames.add(className);
    }

    public void addProperty(String name, String value) {
        props.put(name, value);
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        /*TransformerRegistry reg = TransformerRegistrySingleton.get();
        reg.addTransformer(bundle, transformer);*/
        throw new IllegalArgumentException("No Transformers!");
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return this.excludeUnlisted;
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    @Override
    public DataSource getJtaDataSource() {
        return this.jtaDataSource;
    }

    public String getJtaDataSourceName() {
        return jtaDataSourceName;
    }

    @Override
    public List<String> getManagedClassNames() {
        return new ArrayList<String>(classNames);
    }

    @Override
    public List<String> getMappingFileNames() {
        return Collections.emptyList();
    }

    public String getName() {
        return persistenceUnitName;
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return this.nonJtaDataSource;
    }

    public String getNonJtaDataSourceName() {
        return nonJtaDataSourceName;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return this.persistenceProviderClassName;
    }

    @Override
    public String getPersistenceUnitName() {
        return this.persistenceUnitName;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        try {
            return this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toURL();
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return this.persistenceXMLSchemaVersion;
    }

    @Override
    public Properties getProperties() {
        return this.props;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return this.sharedCacheMode;
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public ValidationMode getValidationMode() {
        return this.validationMode;
    }

    public boolean isExcludeUnlisted() {
        return excludeUnlisted;
    }

    public void setExcludeUnlisted(boolean excludeUnlisted) {
        this.excludeUnlisted = excludeUnlisted;
    }

    public void setJtaDataSource(DataSource jtaDataSource) {
        this.jtaDataSource = jtaDataSource;
    }

    public void setJtaDataSourceName(String jtaDataSourceName) {
        this.jtaDataSourceName = jtaDataSourceName;
    }

    public void setNonJtaDataSource(DataSource nonJtaDataSource) {
        this.nonJtaDataSource = nonJtaDataSource;
    }

    public void setNonJtaDataSourceName(String nonJtaDataSourceName) {
        this.nonJtaDataSourceName = nonJtaDataSourceName;
    }

    public void setProviderClassName(String providerClassName) {
        this.persistenceProviderClassName = providerClassName;
    }

    public void setSharedCacheMode(SharedCacheMode sharedCacheMode) {
        this.sharedCacheMode = sharedCacheMode;
    }

    public void setValidationMode(ValidationMode validationMode) {
        this.validationMode = validationMode;
    }

    public void setTransactionType(PersistenceUnitTransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
