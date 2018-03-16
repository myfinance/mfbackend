/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDRunnerJobTypeContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.application.servicecontext;

import de.hf.dac.api.io.routes.job.JobDispatcher;
import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.myfinance.api.application.ServiceResourceType;

public interface MDRunnerJobTypeContext extends ServiceResourceType {
    JobDispatcher<JobParameter> getDispatcher();
}
