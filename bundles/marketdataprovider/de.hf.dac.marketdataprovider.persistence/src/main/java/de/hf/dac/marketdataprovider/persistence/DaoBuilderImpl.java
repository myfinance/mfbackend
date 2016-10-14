/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DaoBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.marketdataprovider.api.persistence.DaoBuilder;
import de.hf.dac.marketdataprovider.api.persistence.DaoContextBuilder;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

@OsgiServiceProvider(classes = { DaoBuilder.class })
@Singleton
public class DaoBuilderImpl implements DaoBuilder {

    @Inject
    DaoContextBuilder contextBuilder;

    @Override
    public ProductDao buildProductDao(String env) throws SQLException {
        // create autowire applicationContext
        ApplicationContext applicationContext = contextBuilder.build(env);
        return applicationContext.autowire(ProductDao.class);
    }
}
