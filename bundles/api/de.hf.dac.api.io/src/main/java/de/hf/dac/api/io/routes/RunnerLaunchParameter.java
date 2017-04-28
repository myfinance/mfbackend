/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RunnerLaunchParameter.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes;

import de.hf.dac.api.io.routes.job.JobParameter;
import de.hf.dac.api.io.routes.job.RunnerParameter;

import java.util.Date;

public class RunnerLaunchParameter implements JobParameter {

    public static final String RUNNER_REQUEST = "RUNNER_REQUEST";
    public static final String RUNNER_RESULT = "RUNNER_RESULT";

    private final String env;
    private final String bean;
    private String uid;
    private final RunnerParameter beanParameter;
    private final Date started;

    public RunnerLaunchParameter(String env, String bean, String jobUid, Date started, RunnerParameter beanParameter) {
        this.env = env;
        this.beanParameter = beanParameter;
        this.started = started;
        this.uid = jobUid;
        this.bean = bean;
    }

    public String getEnv() {
        return env;
    }

    public String getBean() {
        return bean;
    }

    public RunnerParameter getBeanParameter() {
        return beanParameter;
    }

    @Override
    public String getRoutingID() {
        return RUNNER_REQUEST;
    }

    @Override
    public String getResultRoutingID() {
        return RUNNER_RESULT;
    }

    @Override
    public Date getStart() {
        return started;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }
}

