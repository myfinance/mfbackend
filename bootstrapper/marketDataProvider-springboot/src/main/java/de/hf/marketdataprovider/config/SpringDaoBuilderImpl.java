/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SpringDaoBuilderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.config;

import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.persistence.ProductDaoImpl;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

public class SpringDaoBuilderImpl implements DaoBuilder {

    /*private DataSourceFactory dataSourceFactory;
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory){
        this.dataSourceFactory=dataSourceFactory;
    }*/
    @Inject
    EntityManagerFactory emf;

    @Override
    public ProductDao buildProductDao(String env) throws SQLException {
        /*EnvironmentConfiguration config = new ResfileConfigurationImpl();
        EntityManagerFactorySetupImpl emfs = new EntityManagerFactorySetupImpl();
        emfs.setConfiguration(config);
        emfs.setPostgresDataSourceFactory(dataSourceFactory);
        EnvironmentTargetInfo marketDataTargetInfo = envService.getTarget(env, "marketdata");
        DatabaseInfo dbi = (DatabaseInfo) marketDataTargetInfo.getTargetDetails();
        WrappedEntityManagerFactory emf = null;
        try {
            emf = new WrappedEntityManagerFactory(jtaManager, emfb.buildEntityManagerFactory("marketdata", new ClassLoader[] {Product.class.getClassLoader()}, dbi));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }*/
        return new ProductDaoImpl(emf);
    }
}
