/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RunnerLaunchResult.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes;

import de.hf.dac.api.io.routes.job.BaseJobResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class RunnerLaunchResult extends BaseJobResult {

    public static final Logger LOG = LoggerFactory.getLogger(RunnerLaunchResult.class);

    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;
    private final RunnerLaunchParameter params;

    private int status = SUCCESS;
    private String message = null;

    public RunnerLaunchResult(RunnerLaunchParameter p) {
        super(p.getUid(), p.getResultRoutingID());
        setStart(p.getStart());
        setFinish(new Date());
        this.params = p;
    }

    public RunnerLaunchResult(int status, RunnerLaunchParameter p) {
        this(p);
        this.status = status;
    }

    public RunnerLaunchResult(int status, String message, RunnerLaunchParameter p) {
        this(p);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RunnerLaunchParameter getParams() {
        return params;
    }

    public long getRuntime() {
        if (params != null) {
            return getFinish().getTime() - params.getStart().getTime();
        } else
            return 0L;
    }
}

