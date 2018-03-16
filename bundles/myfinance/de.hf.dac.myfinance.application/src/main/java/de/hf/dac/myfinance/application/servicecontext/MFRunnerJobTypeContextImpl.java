/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFRunnerJobTypeContextImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.servicecontext;

import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.myfinance.api.application.OpLevel;
import de.hf.dac.myfinance.api.application.ServiceResourceType;
import de.hf.dac.myfinance.api.application.servicecontext.MDRunnerJobTypeContext;
import de.hf.dac.myfinance.application.rootcontext.BaseSecurityContext;

public class MFRunnerJobTypeContextImpl extends BaseSecurityContext implements MDRunnerJobTypeContext {

    private JobDispatcher<JobParameter> dispatcher;

    public MFRunnerJobTypeContextImpl(String beanClass, ServiceResourceType parent, JobDispatcher<JobParameter> dispatcher) {
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

