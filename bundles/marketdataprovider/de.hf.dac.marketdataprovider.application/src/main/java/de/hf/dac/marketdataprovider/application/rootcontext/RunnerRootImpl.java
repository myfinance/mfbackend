/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RunnerRootImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.rootcontext;

import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.rootcontext.RunnerRoot;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import de.hf.dac.marketdataprovider.application.servicecontext.MDRunnerJobTypeContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = RunnerRoot.class, scope = ServiceScope.SINGLETON)
public class RunnerRootImpl extends BaseRootSecurityContext implements RunnerRoot {

    @Reference
    private JobDispatcher<JobParameter> dispatcher;

    public RunnerRootImpl(){
        super("RunnerRoot" );
    }

    @Override
    public OpLevel getOpLevel() {
        return OpLevel.runner;
    }

    @Override
    public JobDispatcher<JobParameter> getDispatcher() {
        return dispatcher;
    }

    @Override
    public ServiceResourceType getChildServiceContext(String jobType) {
        return new MDRunnerJobTypeContext(jobType, this);
    }
}
