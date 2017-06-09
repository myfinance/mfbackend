/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : WrappedJobParameter.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 28.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import java.io.Serializable;

public class WrappedJobParameter extends BaseWrapper implements JobParameter {

    public static final String RUNNER_REQUEST = "RUNNER_REQUEST";
    public static final String RUNNER_RESULT = "RUNNER_RESULT";

    private String env;
    private String jobType;

    public WrappedJobParameter(Serializable wrappedSerializable, String env, String jobType, String uid, String routingID, String resultRoutingID) {
        super(wrappedSerializable, uid, routingID, resultRoutingID);
        this.env = env;
        this.jobType = jobType;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getJobType() {
        return jobType;
    }

}
