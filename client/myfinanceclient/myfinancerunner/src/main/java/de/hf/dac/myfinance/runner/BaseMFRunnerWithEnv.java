/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : BaseMFRunnerWithEnv.java
 * Author(s)   : xn01598
 * Created     : 03.08.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner;

import de.hf.dac.api.io.routes.job.RunnerParameter;
import de.hf.dac.myfinance.api.runner.BaseMFRunnerParameter;
import picocli.CommandLine;

public abstract class BaseMFRunnerWithEnv extends BaseMFRunnerClient {

    @CommandLine.Option(names = { "-env", "--environment" }, description = "the data environment of the backend. default is dev")
    private String env = "dev";

    @Override
    public void run() {
        log.info("start " + getClass() + " with environment: " + env);
        super.run();
    }

    @Override
    protected RunnerParameter extractParameters() {
        return new BaseMFRunnerParameter(env);
    }
}
