/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFCoreRoutesImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.routes;

import de.hf.dac.api.io.routes.RouteContextBuilder;
import de.hf.dac.api.io.routes.RunnerLaunchParameter;
import de.hf.dac.api.io.routes.RunnerLauncher;
import de.hf.dac.api.io.routes.job.JobHandler;
import de.hf.dac.api.io.routes.job.JobParameter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobHandler.class, property = { JobHandler.JOB_HANDLER_TARGET_PROPERTY }, immediate = true)
public class MFCoreRoutesImpl implements JobHandler<JobParameter> {

        public static final Logger LOG = LoggerFactory.getLogger(MFCoreRoutesImpl.class);

        public static final String MD_CORE_SERVICE = "MD-CORE-SERVICE";

        @Reference
        RouteContextBuilder contextBuilder;

        @Reference
        RunnerLauncher runnerLauncher;

        public MFCoreRoutesImpl() {
            LOG.info("Started");
        }


        /**
         * Setting up local Camel routing
         */
        @Activate
        public void init() throws Exception {
            if(contextBuilder==null) {
                LOG.error("no RouteContextBuilder available ");
            }
            contextBuilder.build(MD_CORE_SERVICE, this.getClass().getClassLoader(), new MFCoreRoutesProvider(this));
        }

        public RouteContextBuilder getContextBuilder() {
            return contextBuilder;
        }

        public void setContextBuilder(RouteContextBuilder contextBuilder) {
            this.contextBuilder = contextBuilder;
        }

        public RunnerHandler getRunnerHandler() {
            return new RunnerHandler(runnerLauncher);
        }

        @Override
        public String[] getRoutingIDs() {
            return new String[] { RunnerLaunchParameter.RUNNER_REQUEST, RunnerLaunchParameter.RUNNER_RESULT };
        }

        @Override
        public String getInbox(String s) {
            return MFCoreRoutesProvider.getInbox(s);
        }

        @Override
        public void postBind() {
            LOG.info("Registered {}" + getRoutingIDs());
        }

        @Override
        public void postUnBind() {
            LOG.info("Unregistered {}" + getRoutingIDs());
        }

    }

