/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataEnvironmentBuilderModule.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.efmb.tx.WrappedEntityManagerFactory;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.EnvironmentTargetInfo;
import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.marketdataprovider.api.application.EnvTarget;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.persistence.dao.InstrumentDao;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.persistence.InstrumentDaoImpl;
import de.hf.dac.marketdataprovider.persistence.ProductDaoImpl;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.InstrumentService;
import de.hf.dac.marketdataprovider.service.InstrumentServiceImpl;
import de.hf.dac.marketdataprovider.service.ProductServiceImpl;
import de.hf.dac.security.restauthorization.domain.RestAuthorization;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

/**
 * Provides the application context of marketdataprovider
 */
public class MarketDataEnvironmentBuilderModule  extends AbstractModule {

    private EnvironmentService envService;
    private EntityManagerFactorySetup emfb;
    TransactionManager jtaManager;
    private String env;
    private JobDispatcher<JobParameter> dispatcher;

    public MarketDataEnvironmentBuilderModule(EnvironmentService envService, EntityManagerFactorySetup emfb,
        TransactionManager jtaManager, String env, JobDispatcher<JobParameter> dispatcher){
        this.envService=envService;
        this.emfb=emfb;
        this.jtaManager=jtaManager;
        this.env = env;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void configure() {
        bind(EnvironmentService.class).toInstance(envService);

        bind(EntityManagerFactory.class).annotatedWith(Names.named(EnvTarget.MDB)).toInstance(provideMdbEntityManagerFactory());

        bind(String.class).annotatedWith(Names.named("envID")).toInstance(new String(env));
        bind(ProductDao.class).to(ProductDaoImpl.class);
        bind(InstrumentDao.class).to(InstrumentDaoImpl.class);
        bind(ProductService.class).to(ProductServiceImpl.class);
        bind(InstrumentService.class).to(InstrumentServiceImpl.class);

        bind(MarketDataEnvironment.class).to(MarketDataEnvironmentImpl.class);
    }

    EntityManagerFactory provideMdbEntityManagerFactory() {
        EnvironmentTargetInfo marketDataTargetInfo = envService.getTarget(env, EnvTarget.MDB);
        DatabaseInfo dbi = (DatabaseInfo) marketDataTargetInfo.getTargetDetails();
        //// TODO: 09.01.2017 die extra hibernate properties sollten aus dertabelle dacenvironmentconfiguration gelesen werden
        /*Properties extraHibernateProperties = new Properties();
        extraHibernateProperties.put("hibernate.hbm2ddl.auto", "create-drop");
        dbi.setExtraHibernateProperties(extraHibernateProperties);*/
        WrappedEntityManagerFactory emf = null;
        try {
            emf = new WrappedEntityManagerFactory(jtaManager, emfb.buildEntityManagerFactory(EnvTarget.MDB, new ClassLoader[] {Product.class.getClassLoader()}, dbi));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return emf;
    }
}