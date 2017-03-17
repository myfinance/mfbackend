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

package de.hf.dac.marketdataprovider.application;

import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.api.security.SecurityServiceBuilder;
import de.hf.dac.marketdataprovider.api.application.MDRunnerJobType;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.application.RunnerRoot;
import de.hf.dac.marketdataprovider.api.application.ServiceResourceType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Designate(ocd = RunnerRootImpl.RunnerRootSecurity.class)
@Component(service = RunnerRoot.class, immediate = true, scope = ServiceScope.SINGLETON)
public class RunnerRootImpl implements RunnerRoot, ServiceResourceType {

    /**
     * Not all Requests have an Environment e.G. listRunner (there are jobs for multiple environments listed).
     * so I have to configure a environment to secure these operations as well
     */
    @ObjectClassDefinition(name = "Runner Security Source Configuration")
    public @interface RunnerRootSecurity {
        String sourceEnvironmentForSecurityDB() default "dev";
    }

    @Reference
    SecurityServiceBuilder<OpType, OpLevel> securityServiceBuilder;

    private RootSecurityProvider<OpType, OpLevel> rootSecurityProvider;

    @Reference
    private JobDispatcher<JobParameter> dispatcher;

    @Activate
    private void activate(RunnerRootSecurity cacheRootSecurity) {
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
    public RootSecurityProvider getRootSecurityProvider() {
        return rootSecurityProvider;
    }

    public JobDispatcher<JobParameter> getDispatcher() {
        return dispatcher;
    }

    public MDRunnerJobType getAuthType(String jobType) {
        return new MDRunnerJobTypeImpl(jobType, this);
    }
}
