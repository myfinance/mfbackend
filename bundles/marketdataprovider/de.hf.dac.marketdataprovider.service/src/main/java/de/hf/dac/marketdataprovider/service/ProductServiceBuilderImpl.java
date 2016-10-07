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

import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.ProductServiceBuilder;
import de.hf.dac.marketdataprovider.api.service.ProductServiceContextBuilder;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;

@OsgiServiceProvider(classes = {ProductServiceBuilder.class})
@Singleton
public class ProductServiceBuilderImpl implements ProductServiceBuilder {

    @Inject
    ProductServiceContextBuilder contextBuilder;

    @Override
    public ProductService build(String env) throws SQLException {
        // create autowire applicationContext
        ApplicationContext applicationContext = contextBuilder.build(env);
        return applicationContext.autowire(ProductService.class);
    }
}
