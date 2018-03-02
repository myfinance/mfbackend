/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RunnerLauncherImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.runner;

import de.hf.dac.api.io.routes.RunnerLaunchParameter;
import de.hf.dac.api.io.routes.RunnerLaunchResult;
import de.hf.dac.api.io.routes.RunnerLauncher;
import de.hf.dac.api.io.routes.job.RunnerParameter;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentContextBuilder;
import de.hf.dac.marketdataprovider.api.runner.BaseMDRunnerParameter;
import de.hf.dac.marketdataprovider.api.runner.Runner;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = RunnerLauncher.class)
public class RunnerLauncherImpl implements RunnerLauncher {

    public static final Logger LOG = LoggerFactory.getLogger(RunnerLauncherImpl.class);

    @Reference
    MarketDataEnvironmentContextBuilder contextBuilder;

    @Override
    public RunnerLaunchResult launch(RunnerLaunchParameter params) {
        RunnerParameter runnerParameter = params.getBeanParameter();
        LOG.debug("Reveived params {} started at {}" + runnerParameter.getClass().getName(), params.getStart());

        // find actual class to be used
        String beanName = params.getBean();

        if (beanName == null) {
            LOG.error("Unable to GUICE correct Bean. No name given in Request");
            // need to define proper result instances returning meaningful values for PDB.
            return new RunnerLaunchResult(RunnerLaunchResult.FAILURE, "Unable to find correct Bean. No name given in Request", params);
        }

        Class clazz = null;
        try {
            clazz = Class.forName(beanName);
            // get request generator autowired for the current environment
            Object runnerBean = contextBuilder.build(params.getEnv()).autowire(clazz);

            try {
                if (runnerBean instanceof Runner) {
                    Runner st = (Runner) runnerBean;
                    st.run(new BaseMDRunnerParameter((BaseMDRunnerParameter) runnerParameter));
                }
                // put actual results into the result instances here
                // Actually all Runner only return 0 to indicate success. Here we add the call params as well
                return new RunnerLaunchResult(params);

            } catch (Throwable ex) {
                // catch everything to avoid camel dead letter delivery but send out Reply instead to mark this job as failed.
                return new RunnerLaunchResult(RunnerLaunchResult.FAILURE, ex.getMessage(), params);
            }
        } catch (ClassNotFoundException cnfe) {
            LOG.error("Unable to GUICE correct Bean. {}", cnfe.getLocalizedMessage());
            return new RunnerLaunchResult(RunnerLaunchResult.FAILURE, cnfe.getMessage(), params);
        } catch (Exception e) {
            LOG.error("Unable to call Runner instance {}", e.getLocalizedMessage());
            return new RunnerLaunchResult(RunnerLaunchResult.FAILURE, e.getMessage(), params);
        }
    }
}

