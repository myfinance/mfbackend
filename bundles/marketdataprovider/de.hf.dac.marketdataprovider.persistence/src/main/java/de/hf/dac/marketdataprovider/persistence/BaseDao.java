/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseDao.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class BaseDao {

    //default EntityManager
    protected EntityManager marketDataEm;

    //to create additional EntityManagers with separate connections for big transactions
    protected EntityManagerFactory marketDataEmf;
    @Inject
    public void setMarketDataEmf(EntityManagerFactory marketDataEmf){
        this.marketDataEmf = marketDataEmf;
    }

    protected final RepositoryService repositoryService = new RepositoryService();

    public BaseDao(EntityManagerFactory marketDataEmf) {
        this.marketDataEmf = marketDataEmf;
        marketDataEm = this.marketDataEmf.createEntityManager();
    }
}
