/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : MfShell.java
 * Author(s)   : xn01598
 * Created     : 27.07.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner;

import de.hf.dac.api.io.routes.job.RunnerParameter;
import de.hf.dac.myfinance.api.runner.BaseMFRunnerParameter;

/**
 * command line runner for all mf commands
 */
public class MfShell extends BaseMFRunnerClient {

    /**
     * @param args
     *        The command line arguments
     */
    public static void main(String[] args) {

        try {
            new MfShell().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected RunnerParameter extractParameters() {
        String env = "dev";
        if (optionsParser.hasOption(ENV_OPTION)) {
            env = optionsParser.getOptionArg(ENV_OPTION);
        }
        return new BaseMFRunnerParameter(env);
    }

    @Override
    public String getJobType() {
        return ImportRunner.class.getName();
    }
}

