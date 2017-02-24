/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : JobDispatcher.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import java.util.List;

/**
 *
 * @param <JOB_TYPE>
 */
public interface JobDispatcher<JOB_TYPE extends JobParameter> {

    public static final String DAC_JOB_DISPATCHER = "DAC_JOB_DISPATCHER";
    public static final String DIRECT_VM_PDAC_JOB_IN = "direct-vm:dac-core-job-in";
    public static final String DIRECT_VM_PDAC_JOB_RESULT_IN = "direct-vm:dac-core-job-results-in";
    public static final String DIRECT_DEAD_LETTER = "direct-vm:dac-core-job-dead-letter";


    // Put new Job into Dispatcher.

    /**
     * Submit generic Job into Camel Job Routing / Dispatching Engine
     * @param job Instance describing the job.
     * @return Unique ID of this Job for later result retrieval
     */
    JobInformation sendJob(JOB_TYPE job);

    /**
     * Setting a new Job Status.
     * @param jobUUID Unique ID of running Job
     * @param newStatus The actual new Status
     * @return update JobInformation
     */
    JobInformation updateJobStatus(String jobUUID, JobInformation.JobStatus newStatus, String message);

    JobInformation getStatus(String jobUUID);

    /**
     * List all currently Queued and Running Jobs
     * @return List of JobInformationInstances
     */
    List<JobInformation> list();

}
