/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDRunnerJobTypeImpl.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 17.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application;

import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class MDRunnerJobTypeImpl implements ServiceResourceType {
    private final ServiceResourceType parent;

    @Override
    public ServiceResourceType getParent() {
        return parent;
    }

    final private String id;

    public MDRunnerJobTypeImpl(String beanClass, ServiceResourceType parent) {
        this.id = beanClass;
        this.parent = parent;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public OpLevel getOpLevel() {
        return OpLevel.runner;
    }

    @Override
    public RootSecurityProvider getRootSecurityProvider() {
        return getParent().getRootSecurityProvider();
    }


    @Override
    public List<String> getParentIdTrail() {
        throw new NotImplementedException("getParentIdTrail");
    }

}

