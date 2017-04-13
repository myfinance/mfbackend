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
import de.hf.dac.marketdataprovider.application.MDRunnerJobTypeImpl;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.Designate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Designate(ocd = BaseRootContext.RootSecurity.class)
@Component(service = RunnerRoot.class, immediate = true, scope = ServiceScope.SINGLETON)
public class RunnerRootImpl extends BaseRootContext implements RunnerRoot {

    @Reference
    private JobDispatcher<JobParameter> dispatcher;

    @Activate
    private void activate(RootSecurity cacheRootSecurity) {
        try {
            rootSecurityProvider = securityServiceBuilder.build(cacheRootSecurity.sourceEnvironmentForSecurityDB());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    final private String id = "RunnerRoot";


    @Override
    public ServiceResourceType getParent() {
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<String> getParentIdTrail() {
        List<String> ret = new ArrayList<>();
        ret.add(id);
        return ret;
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
    public ServiceResourceType getAuthType(String jobType) {
        return new MDRunnerJobTypeImpl(jobType, this);
    }
}
