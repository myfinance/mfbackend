/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ServiceBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service;

import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.ServiceBuilder;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Singleton;

@OsgiServiceProvider(classes = { ServiceBuilder.class })
@Singleton
public class ServiceBuilderImpl implements ServiceBuilder {

    @Override
    public ProductService buildProductService(ProductDao productDao) {
        return new ProductServiceImpl(productDao);
    }
}
