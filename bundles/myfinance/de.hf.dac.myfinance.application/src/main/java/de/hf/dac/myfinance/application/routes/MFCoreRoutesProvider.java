/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFCoreRoutesProvider.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.routes;

import de.hf.dac.api.io.routes.RunnerLaunchParameter;
import de.hf.dac.api.io.routes.job.JobDispatcher;
import org.apache.camel.builder.RouteBuilder;

public class MFCoreRoutesProvider extends RouteBuilder {
    public static final int KEEP_ALIVE_TIME = 1000 * 60 * 60 * 2;
    private final MFCoreRoutesImpl mdCoreRoutes;

    public static final String DIRECT_VM_MD_CORE_RUNNER_IN = "direct-vm:md-core-Runner-in";
    public static final String DIRECT_VM_MD_CORE_RUNNER_OUT = "direct-vm:md-core-Runner-out";


    public MFCoreRoutesProvider(MFCoreRoutesImpl mdCoreRoutes) {
        this.mdCoreRoutes = mdCoreRoutes;
    }

    @Override
    public void configure() throws Exception {


        /**
         * Route to handle incoming WrappedRunnerParamater. All Jobs need to contain a RunnerParameter wrapped into WrappedJobParameter.
         */
        from(DIRECT_VM_MD_CORE_RUNNER_IN).id("Runner-in").threads(2).keepAliveTime(KEEP_ALIVE_TIME). //
            bean(mdCoreRoutes.getRunnerHandler(), "handleExchange").id("Runner-handler") //
            .threads(2).keepAliveTime(KEEP_ALIVE_TIME) //
            // return back any results into Dispatching queue.
            // make sure base infrastructure knows about it
            .to(JobDispatcher.DIRECT_VM_DAC_JOB_RESULT_IN);

        /**
         * Route to handle incoming JobResults
         */
        from(DIRECT_VM_MD_CORE_RUNNER_OUT).id("Runner-result-postprocessing").threads(2).keepAliveTime(KEEP_ALIVE_TIME). //
            bean(mdCoreRoutes.getRunnerHandler(), "handleExchange").id("Runner-result-handler");
    }

    public static String getInbox(String s) {
        if (RunnerLaunchParameter.RUNNER_REQUEST.equals(s)) {
            return DIRECT_VM_MD_CORE_RUNNER_IN;
        } else if (RunnerLaunchParameter.RUNNER_RESULT.equals(s)) {
            return DIRECT_VM_MD_CORE_RUNNER_OUT;
        }
        return null;
    }
}

