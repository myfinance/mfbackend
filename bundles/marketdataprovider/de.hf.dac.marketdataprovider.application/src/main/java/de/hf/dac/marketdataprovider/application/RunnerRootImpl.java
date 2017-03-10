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
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.application.RunnerRoot;
import org.apache.commons.lang3.NotImplementedException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Designate(ocd = RunnerRootImpl.RunnerRootSecurity.class)
@Component(service = RunnerRoot.class, immediate = true, scope = ServiceScope.SINGLETON)
public class RunnerRootImpl implements RunnerRoot {
//public class RunnerRootImpl extends CCRServiceResourceTypeBase implements RunnerRoot {

   /* public RunnerRootImpl() {
        super(null);
    }*/

    @ObjectClassDefinition(name = "Runner Security Source Configuration")
    public @interface RunnerRootSecurity {
        String sourceEnvironmentForSecurityDB() default "dev";
    }

    /*@Reference
    SecurityServiceBuilder<OpType, CCRResourceLevel> securityServiceBuilder;

    private RootSecurityProvider<OpType, CCRResourceLevel> rootSecurityProvider;
*/
    @Reference
    private JobDispatcher<JobParameter> dispatcher;

    /*@Activate
    private void activate(RunnerRootSecurity cacheRootSecurity) {
        try {
            rootSecurityProvider = securityServiceBuilder.build(cacheRootSecurity.sourceEnvironmentForSecurityDB());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    final private String id = "RunnerRoot";

    /*@Override
    public Retriever getRetriever(String operationID, Optional<Qualifier> suffix) {
        throw new NotImplementedException("getRetriever");
    }

    @Override
    public CCRServiceResourceType getParent() {
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
    public CCRResourceLevel getOpLevel() {
        return CCRResourceLevel.runner;
    }

    @Override
    public RootSecurityProvider getRootSecurityProvider() {
        return rootSecurityProvider;
    }
*/
    public JobDispatcher<JobParameter> getDispatcher() {
        return dispatcher;
    }

   /* public CCRRunnerJobTypeCCR getAuthType(String beanClass, String aggMode) {
        return new CCRRunnerJobTypeImplCCR(beanClass, aggMode, this);
    }*/
}
