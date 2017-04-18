/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDRunnerJobTypeContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.servicecontext;

import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import de.hf.dac.marketdataprovider.application.rootcontext.BaseSecurityContext;

public class MDRunnerJobTypeContext extends BaseSecurityContext {


    public MDRunnerJobTypeContext(String beanClass, ServiceResourceType parent) {
        super(beanClass,parent);
    }

    @Override
    public ServiceResourceType getChildServiceContext(String id) {
        return null;
    }

    @Override
    public OpLevel getOpLevel() {
        return OpLevel.runner;
    }

}

