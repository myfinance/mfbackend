/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseSecurityContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 18.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.rootcontext;

import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.common.BaseDS;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSecurityContext extends BaseDS implements ServiceResourceType {

    final protected String id;
    private final ServiceResourceType parent;

    public BaseSecurityContext(String id) {
        this.id = id;
        this.parent = this;
    }

    public BaseSecurityContext(String id, ServiceResourceType parent) {
        this.id = id;
        this.parent = parent;
    }

    @Override
    public ServiceResourceType getParent() {
        return parent;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RootSecurityProvider getRootSecurityProvider() {
        return getParent().getRootSecurityProvider();
    }

    @Override
    public List<String> getParentIdTrail() {
        List<String> ret = new ArrayList<>(getParent().getParentIdTrail());
        ret.add(id);
        return ret;
    }
}
