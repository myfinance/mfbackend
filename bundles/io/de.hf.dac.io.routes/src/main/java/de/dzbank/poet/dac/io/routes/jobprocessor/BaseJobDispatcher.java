/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseJobDispatcher.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.dzbank.poet.dac.io.routes.jobprocessor;

import de.hf.dac.api.io.routes.ApplicationRouteContext;
import de.hf.dac.api.io.routes.RouteContextBuilder;
import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobHandler;
import de.hf.dac.api.io.routes.job.JobInformation;
import de.hf.dac.api.io.routes.job.JobParameter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Generic Job Dispatcher based on JobParameters
 * This class will be exposed as a (declarative) Service to be used for programmatically sending
 * Jobs into the Dispatcher
 */
@Component(service = JobDispatcher.class, immediate = true, scope = ServiceScope.SINGLETON, name = "DAC.BaseJobDispatcher")
public class BaseJobDispatcher implements JobDispatcher<JobParameter> {

    public static final Logger LOG = LoggerFactory.getLogger(BaseJobDispatcher.class);

    private Map<String, JobHandler> routingidToHandlerMap = new HashMap<>();
    private ApplicationRouteContext routingContext = null;

    @Reference(service = RouteContextBuilder.class)
    protected RouteContextBuilder contextBuilder;

    @Reference(bind = "bind", unbind = "unbind", cardinality = ReferenceCardinality.MULTIPLE, service = JobHandler.class, policy = ReferencePolicy.DYNAMIC, target =
        "(" + JobHandler.JOB_HANDLER_TARGET_PROPERTY + ")")
    volatile List<JobHandler<JobParameter>> jobHandlerList = new ArrayList<>();

    /**
     * Simple map to keep in memory state of currently known Jobs (queued, runnning, finsihed
     */
    volatile Map<String, JobInformation> knownJobs = new HashMap<>();

    @Activate
    public void init() throws Exception {
        // create CAMEL Infrastructure for Job Handling
        this.routingContext = contextBuilder.build(DAC_JOB_DISPATCHER, this.getClass().getClassLoader(), new BaseJobDispatcherRouteProvider(this));
    }

    @Override
    public JobInformation sendJob(JobParameter job) {
        if (routingContext != null) {
            LOG.info("Dispatching JobParameter {} ", (job != null ? job.getClass().getName() : "null"));
            // Wrap and remmeber job information. Assign unique id as well
            JobInformation jobInfo = registerJob(job);
            routingContext.sendMessage(DIRECT_VM_DAC_JOB_IN, jobInfo.getPayload());
            return jobInfo;
        }
        return new JobInformation(UUID.randomUUID().toString());
    }

    private JobInformation registerJob(JobParameter job) {
        String jobUUID = job != null ? UUID.randomUUID().toString() : null;
        if (jobUUID != null) {
            JobInformation info = new JobInformation(jobUUID);
            info.setPayload(job);
            // join them together by one unique identifier
            job.setUid(jobUUID);
            // save job in internal reference map
            synchronized (this.knownJobs) {
                knownJobs.put(jobUUID, info);
            }

            return info;
        }
        return null;
    }

    public void cleanJobList() {
        synchronized (this.knownJobs) {
            Iterator<Map.Entry<String, JobInformation>> it = knownJobs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, JobInformation> entry = it.next();
                if (entry.getValue().getStatus().in(JobInformation.JobStatus.FINISHED, JobInformation.JobStatus.FAILED)) {
                    it.remove();
                }
            }
        }
    }

    @Override
    public JobInformation updateJobStatus(String jobUUID, JobInformation.JobStatus newStatus, String message) {
        // find info
        if (jobUUID != null) {
            JobInformation info = this.knownJobs.get(jobUUID);
            if (info != null) {
                info.setStatus(newStatus, message);
            }
            return info;
        }
        return null;
    }

    @Override
    public JobInformation getStatus(String jobUUID) {
        if (this.knownJobs.containsKey(jobUUID)) {
            return this.knownJobs.get(jobUUID);
        } else {
            JobInformation failed = new JobInformation("JOB NOT FOUND");
            failed.setStatus(JobInformation.JobStatus.FAILED, "JOB NOT FOUND");
            return failed;
        }
    }

    @Override
    public List<JobInformation> list() {
        List<JobInformation> result = new ArrayList<>();
        synchronized (this.knownJobs) {
            result.addAll(this.knownJobs.values());
        }
        return result;
    }

    protected void unbind(JobHandler<JobParameter> handler) {

        String[] ids = handler.getRoutingIDs();

        for (String id : ids) {
            JobHandler jobHandler = this.routingidToHandlerMap.get(id);
            if (jobHandler != null) {
                jobHandler.postUnBind();
            }
        }
    }

    protected void bind(JobHandler<JobParameter> handler) {
        if (handler != null) {
            String name = handler.getClass().getName();
            LOG.info("Binding JobaHandler {}", name);
            String[] ids = handler.getRoutingIDs();
            for (String id : ids) {
                LOG.info("Binding {} to {}", id, name);
                this.routingidToHandlerMap.put(id, handler);
                handler.postBind();
            }
        }

    }

    public Map<String, JobHandler> getRoutingidToHandlerMap() {
        return routingidToHandlerMap;
    }

    public List<JobHandler<JobParameter>> getJobHandlerList() {
        return jobHandlerList;
    }

    public void setJobHandlerList(List<JobHandler<JobParameter>> jobHandlerList) {
        this.jobHandlerList = jobHandlerList;
    }

    public JobDispatchHandler getHandler() {
        return new JobDispatchHandler(this);
    }

    public ApplicationRouteContext getRoutingContext() {
        return routingContext;
    }

    public Map<String, JobInformation> getKnownJobs() {
        return knownJobs;
    }

    public void setKnownJobs(Map<String, JobInformation> knownJobs) {
        this.knownJobs = knownJobs;
    }
}

