/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RootSecurityServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 13.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.securitycontext;

import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.myfinance.api.application.EnvTarget;
import de.hf.dac.myfinance.api.application.MarketDataSystemDescriptor;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.OpType;
import de.hf.dac.security.restauthorization.BaseSecurityProviderImpl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;

public class RootSecurityServiceImpl extends BaseSecurityProviderImpl<OpType,OpLevel>
    implements RootSecurityProvider<OpType, OpLevel> {

    @Inject
    public RootSecurityServiceImpl(@Named(EnvTarget.SDB) EntityManagerFactory sdbFactory) {
        this.sdbFactory = sdbFactory;
        updateAuth();
    }

    final private EntityManagerFactory sdbFactory;

    @Override
    public EntityManagerFactory getEMFactory() {
        return sdbFactory;
    }

    @Override
    protected String getSystem() {
        return MarketDataSystemDescriptor.VALUE;
    }

    @Override
    protected Class<OpType> getAccessType() {
        return OpType.class;
    }

    @Override
    protected Class<OpLevel> getResourceLevel() {
        return OpLevel.class;
    }
}

