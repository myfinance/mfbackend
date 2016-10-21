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
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.persistence.DaoBuilder;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.ServiceBuilder;

import java.sql.SQLException;

public class MarketDataEnvironmentBuilderModule  extends AbstractModule {

    private EnvironmentService envService;
    private ServiceBuilder serviceBuilder;
    private DaoBuilder daoBuilder;
    private String env;

    public MarketDataEnvironmentBuilderModule(EnvironmentService envService, ServiceBuilder serviceBuilder, DaoBuilder daoBuilder, String env){
        this.envService=envService;
        this.serviceBuilder=serviceBuilder;
        this.daoBuilder=daoBuilder;
        this.env=env;
    }

    @Override
    protected void configure() {
        bind(EnvironmentService.class).toInstance(envService);
        ProductDao productDao = null;
        try {
            productDao=daoBuilder.buildProductDao(env);
            bind(ProductDao.class).toInstance(productDao);
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        bind(ProductService.class).toInstance(serviceBuilder.buildProductService(productDao));
        bind(MarketDataEnvironment.class).to(MarketDataEnvironmentImpl.class);
    }


}