/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ProductServiceBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 29.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.marketdataprovider.api.persistence.RepositoryService;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.ProductServiceBuilder;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;

@OsgiServiceProvider(classes = {ProductServiceBuilder.class})
@Singleton
public class ProductServiceBuilderImpl implements ProductServiceBuilder {

    @Inject
    @OsgiService
    EntityManagerFactorySetup emfb;

    @Inject
    @OsgiService
    EnvironmentService envService;

    @Inject
    @OsgiService
    RepositoryService repositoryService;

    @Inject
    protected BundleContext bundleContext;


    @Override
    public ProductService build(String env) throws SQLException {
        Injector injector = Guice.createInjector(new ProductServiceBuilderModule(emfb, envService, bundleContext, repositoryService, env));
        return injector.getInstance(ProductService.class);
    }
}
