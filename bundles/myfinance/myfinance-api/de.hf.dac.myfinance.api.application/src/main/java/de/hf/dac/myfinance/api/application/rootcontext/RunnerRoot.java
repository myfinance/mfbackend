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

package de.hf.dac.myfinance.api.application.rootcontext;

import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.myfinance.api.application.ServiceResourceType;
import de.hf.dac.myfinance.api.application.servicecontext.MDRunnerJobTypeContext;

public interface RunnerRoot extends ServiceResourceType {
    JobDispatcher<JobParameter> getDispatcher();
    MDRunnerJobTypeContext getMDRunnerJobTypeContext(String jobType);
}