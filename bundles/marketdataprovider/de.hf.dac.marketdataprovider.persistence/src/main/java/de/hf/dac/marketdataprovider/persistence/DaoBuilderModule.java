/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DaoBuilderModule.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 13.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.efmb.tx.WrappedEntityManagerFactory;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.EnvironmentTargetInfo;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.persistence.DaoBuilder;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

public class DaoBuilderModule extends AbstractModule {

    private EntityManagerFactorySetup emfb;
    private EnvironmentService envService;
    private TransactionManager jtaManager;
    private String env;

    public DaoBuilderModule(EntityManagerFactorySetup emfb, EnvironmentService envService, TransactionManager jtaManager, String env){
        this.emfb=emfb;
        this.envService=envService;
        this.jtaManager=jtaManager;
        this.env=env;
    }

    @Override
    protected void configure() {
        //bind(ProductDao.class).to(ProductDaoImpl.class);
        //Singleton
        EntityManagerFactory entityManagerFactory = provideEntityManagerFactory();
        bind(ProductDao.class).toInstance(new ProductDaoImpl(entityManagerFactory));
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