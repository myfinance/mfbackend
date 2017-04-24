/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDRunnerJobTypeContextImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.servicecontext;

import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import de.hf.dac.marketdataprovider.api.application.servicecontext.MDRunnerJobTypeContext;
import de.hf.dac.marketdataprovider.application.rootcontext.BaseSecurityContext;

public class MDRunnerJobTypeContextImpl extends BaseSecurityContext implements MDRunnerJobTypeContext {

    private JobDispatcher<JobParameter> dispatcher;

    public MDRunnerJobTypeContextImpl(String beanClass, ServiceResourceType parent, JobDispatcher<JobParameter> dispatcher) {
        super(beanClass,parent);
        this.dispatcher = dispatcher;
    }

    @Override
    public ServiceResourceType getChildServiceContext(String id) {
        return null;
    }

    @Override
    public OpLevel getOpLevel() {
        return OpLevel.runner;
    }

    @Override
    public JobDispatcher<JobParameter> getDispatcher() {
        return dispatcher;
    }
}

