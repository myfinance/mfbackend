/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RunnerHandler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application.routes;

import de.hf.dac.api.io.routes.RouteExchangeHandler;
import de.hf.dac.api.io.routes.RunnerLaunchParameter;
import de.hf.dac.api.io.routes.RunnerLauncher;
import de.hf.dac.api.io.routes.job.JobResult;
import de.hf.dac.api.io.routes.job.RunnerParameter;
import de.hf.dac.api.io.routes.job.StringJobResult;
import de.hf.dac.api.io.routes.job.WrappedJobParameter;
import org.apache.camel.Exchange;

/**
 * Retrieve WrappedRunnerParameter. Unfold them and forward to actual implementation
 */
public class RunnerHandler implements RouteExchangeHandler {

    private final RunnerLauncher launcher;

    public RunnerHandler(RunnerLauncher launcher) {
        this.launcher = launcher;
    }

    @Override
    public Object handleExchange(Exchange exchange) {
        // unwrap and hand over to actual implementing class
        Object object = exchange.getIn().getBody();
        if (object instanceof WrappedJobParameter) {
            return handleJobParams((WrappedJobParameter) object);
        } else if (object instanceof JobResult) {
            return handleJobResult((JobResult) object);
        }

        return new StringJobResult(
            "Unable to Handle RunnerParameters. Expected " + RunnerParameter.class.getName() + " but found " + object.getClass().getName(), "ERROR");
    }

    private Object handleJobResult(JobResult object) {
        return new StringJobResult("Finally Post Processing Runner Results " + object + object.getClass().getName(), "ERROR");
    }

    private Object handleJobParams(WrappedJobParameter object) {
        WrappedJobParameter wrappedParams = object;
        RunnerParameter p = (RunnerParameter) wrappedParams.getWrappedSerializable();
        // pass on to actual spring launch logic
        return launcher.launch(new RunnerLaunchParameter(wrappedParams.getEnv(),wrappedParams.getJobType(), wrappedParams.getUid(), wrappedParams.getStart(), p));
    }

}
