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
import com.google.inject.Provides;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.efmb.tx.WrappedEntityManagerFactory;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.EnvironmentTargetInfo;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.persistence.ProductDaoImpl;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.service.ProductServiceImpl;

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

    public MarketDataEnvironmentBuilderModule(EnvironmentService envService, EntityManagerFactorySetup emfb,
        TransactionManager jtaManager, String env){
        this.envService=envService;
        this.emfb=emfb;
        this.jtaManager=jtaManager;
        this.env = env;
    }

    @Override
    protected void configure() {
        bind(EnvironmentService.class).toInstance(envService);

        EntityManagerFactory entityManagerFactory = provideEntityManagerFactory();
        bind(ProductDao.class).toInstance(new ProductDaoImpl(entityManagerFactory));
        bind(ProductService.class).to(ProductServiceImpl.class);
        bind(MarketDataEnvironment.class).to(MarketDataEnvironmentImpl.class);
    }


    @Provides
    EntityManagerFactory provideEntityManagerFactory() {
        EnvironmentTargetInfo marketDataTargetInfo = envService.getTarget(env, "marketdata");
        DatabaseInfo dbi = (DatabaseInfo) marketDataTargetInfo.getTargetDetails();
        WrappedEntityManagerFactory emf = null;
        try {
            emf = new WrappedEntityManagerFactory(jtaManager, emfb.buildEntityManagerFactory("marketdata", new ClassLoader[] {Product.class.getClassLoader()}, dbi));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return emf;
    }

}