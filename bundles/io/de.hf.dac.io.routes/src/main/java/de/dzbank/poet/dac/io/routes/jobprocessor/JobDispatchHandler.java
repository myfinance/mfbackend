/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : JobDispatchHandler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.dzbank.poet.dac.io.routes.jobprocessor;

import de.hf.dac.api.io.routes.job.JobHandler;
import de.hf.dac.api.io.routes.job.JobInformation;
import de.hf.dac.api.io.routes.job.JobResult;
import de.hf.dac.api.io.routes.job.Routable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The actual dispatching logic. Tries to find a dynamically registered JobHandler
 * from any other bundle in the Container.
 */
public class JobDispatchHandler {

    public static final Logger LOG = LoggerFactory.getLogger(JobDispatchHandler.class);

    private final BaseJobDispatcher baseJobDispatcher;

    public JobDispatchHandler(BaseJobDispatcher baseJobDispatcher) {
        this.baseJobDispatcher = baseJobDispatcher;
    }

    public String handleResult(JobResult result) {
        if (result == null) {
            LOG.error("Unable to handle Null Result");
        } else {
            LOG.info("Job Result Received {}", result.getMessage());
            String message = String.format("Job started %s and Finished %s", result.getStart(), result.getFinish());
            LOG.info(message);
            baseJobDispatcher
                .updateJobStatus(result.getUid(), result.getMessage() != null ? JobInformation.JobStatus.FAILED : JobInformation.JobStatus.FINISHED,
                    message);
            dispatch(result);
        }
        return "Success";
    }

    public String handleJob(Routable job) {
        if (job == null) {
            LOG.error("Unable to handle Null Job");
            return "Failure";
        } else {
            baseJobDispatcher.updateJobStatus(job.getUid(), JobInformation.JobStatus.QUEUED, "Dispatched to handler " + job.getClass().getName());
            dispatch(job);
        }
        return "Success";
    }

    public void dispatch(Routable job) {
        String routingID = job.getRoutingID();
        if (baseJobDispatcher.getRoutingidToHandlerMap().containsKey(routingID)) {
            JobHandler handler = baseJobDispatcher.getRoutingidToHandlerMap().get(routingID);
            String name = handler.getClass().getName();
            LOG.info("Found {} to handle {} of {}", name, routingID, job.getClass().getName());
            if (handler != null) {
                String inboxUrl = handler.getInbox(routingID);
                if (baseJobDispatcher.getRoutingContext() != null) {
                    // simply putting into next Camel Route
                    baseJobDispatcher.getRoutingContext().sendMessage(inboxUrl, job);
                }
            } else {
                baseJobDispatcher.updateJobStatus(job.getUid(), JobInformation.JobStatus.FAILED, "Handler Reference was null. Unable to Dispach Job");
            }
        } else {
            // unable to find a matching routing handler
            baseJobDispatcher.updateJobStatus(job.getUid(), JobInformation.JobStatus.FAILED, "Unable to find OSGI Service handling this job");
        }
    }
}
