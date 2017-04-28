/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseJobDispatcherRouteProvider.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.dzbank.poet.dac.io.routes.jobprocessor;

import org.apache.camel.ErrorHandlerFactory;
import org.apache.camel.builder.RouteBuilder;

public class BaseJobDispatcherRouteProvider extends RouteBuilder {
    public static final int KEEP_ALIVE_TIME = 1000 * 60 * 60 * 2;
    public static final String DAC_JOB_RESULTS_IN = "DAC-JOB-RESULTS-IN";
    public static final String BASE_JOBRESULT_DISPATCHER = "BASE-JOBRESULT-DISPATCHER";
    private final BaseJobDispatcher baseJobDispatcher;

    public BaseJobDispatcherRouteProvider(BaseJobDispatcher baseJobDispatcher) {
        this.baseJobDispatcher = baseJobDispatcher;
    }

    @Override
    public void configure() throws Exception {

        /**
         * Default Inbox receiving all new Jobs . Jobs will be routed according their RoutingID using
         * JobHandler Services registered from other Bundles
         */
        from(BaseJobDispatcher.DIRECT_VM_DAC_JOB_IN) //
            .errorHandler(getDeadLetterConfig()) //
            .id("DAC-JOBS-IN").threads(2).keepAliveTime(KEEP_ALIVE_TIME). //
            bean(baseJobDispatcher.getHandler(), "handleJob").id("BASE-JOB-DISPATCHER");

        /**
         * Default Inbox receiving all new JopbResults as well as Results. Jobs will be routed according their RoutingID using
         * JobHandler Services registered from other Bundles
         */
        from(BaseJobDispatcher.DIRECT_VM_DAC_JOB_RESULT_IN) //
            .errorHandler(getDeadLetterConfig()) //
            .id(DAC_JOB_RESULTS_IN).threads(2).keepAliveTime(KEEP_ALIVE_TIME). //
            bean(baseJobDispatcher.getHandler(), "handleResult").id(BASE_JOBRESULT_DISPATCHER);

    }

    private ErrorHandlerFactory getDeadLetterConfig() {
        return deadLetterChannel(BaseJobDispatcher.DIRECT_DEAD_LETTER) //
            .redeliveryDelay(5000).maximumRedeliveries(3) //
            .logHandled(true).logRetryStackTrace(true);
    }

}
