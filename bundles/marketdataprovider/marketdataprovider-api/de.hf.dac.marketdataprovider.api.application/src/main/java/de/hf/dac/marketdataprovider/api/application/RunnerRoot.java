/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RunnerRoot.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.application;

import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;

public interface RunnerRoot {
    JobDispatcher<JobParameter> getDispatcher();
    MDRunnerJobType getJobType(String type);
}