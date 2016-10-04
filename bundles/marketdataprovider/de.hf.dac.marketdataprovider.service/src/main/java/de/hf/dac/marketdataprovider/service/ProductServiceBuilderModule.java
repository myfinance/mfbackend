/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ProductServiceBuilderModule.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.EnvironmentTargetInfo;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.persistence.RepositoryService;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import org.osgi.framework.BundleContext;

import javax.persistence.EntityManagerFactory;

public class ProductServiceBuilderModule extends AbstractModule {

    private EntityManagerFactorySetup emfb;
    private EnvironmentService envService;
    private BundleContext bundleContext;
    private RepositoryService repositoryService;
    private String env;

    public ProductServiceBuilderModule(EntityManagerFactorySetup emfb, EnvironmentService envService, BundleContext bundleContext, RepositoryService repositoryService, String env){
        this.emfb=emfb;
        this.envService=envService;
        this.bundleContext=bundleContext;
        this.repositoryService = repositoryService;
        this.env=env;
    }

    @Override
    protected void configure() {
        bind(ProductService.class).to(ProductServiceImpl.class);
        bind(BundleContext.class).toInstance(bundleContext);
        bind(RepositoryService.class).toInstance(repositoryService);
        //bind(EntityManagerFactory.class).toProvider(this);
    }

    @Provides
    EntityManagerFactory provideEntityManagerFactory() {
        EnvironmentTargetInfo marketDataTargetInfo = envService.getTarget(env, "marketdata");
        DatabaseInfo dbi = (DatabaseInfo) marketDataTargetInfo.getTargetDetails();
        EntityManagerFactory marketdata = null;
        try {
            marketdata = emfb.buildEntityManagerFactory("marketdata", new ClassLoader[] {Product.class.getClassLoader()}, dbi);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return marketdata;
    }
}
