/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ProductServiceContextBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service;

import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.api.io.env.context.ContextBuilder;
import de.hf.dac.marketdataprovider.api.persistence.RepositoryService;
import de.hf.dac.marketdataprovider.api.service.ProductServiceContextBuilder;
import org.ops4j.pax.cdi.api.OsgiService;
import com.google.inject.Module;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.TransactionManager;

@OsgiServiceProvider(classes = { ProductServiceContextBuilder.class })
@Singleton
public class ProductServiceContextBuilderImpl implements ProductServiceContextBuilder {

    @Inject
    @OsgiService
    ContextBuilder contextBuilder;

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
    @OsgiService
    TransactionManager jtaManager;

    @Override
    public ApplicationContext build(String env) {
        ProductServiceBuilderModule productServiceModule //
            = new ProductServiceBuilderModule(emfb, //
            envService, //
            jtaManager, //
            repositoryService, //
            env);
        return contextBuilder.build("productservice/" + env, new Module[] { productServiceModule });
    }
}
