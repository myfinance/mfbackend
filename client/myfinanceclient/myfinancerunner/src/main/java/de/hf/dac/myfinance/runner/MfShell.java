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

    protected Action action = Action.IMPORT;

    /**
     * @param args
     *        The command line arguments
     */
    public static void main(String[] args) {

        new MfShell().run(args);
    }

    @Override
    protected void addCustomCommandLineOptions() {
        for (CmdLineOptions option:CmdLineOptions.values()) {
            option.addOption(optionsParser);
        }
    }

    @Override
    protected RunnerParameter extractParameters() {
        String env = "dev";
        if (optionsParser.hasOption(CmdLineOptions.ENVIRONMENT.getShortName())) {
            env = optionsParser.getOptionArg(CmdLineOptions.ENVIRONMENT.getShortName());
        }

        if (optionsParser.hasOption(CmdLineOptions.ACTIONTYPE.getShortName())) {
            action = action.getActionByAlias(optionsParser.getOptionArg(CmdLineOptions.ACTIONTYPE.getShortName()));
        }
        return new BaseMFRunnerParameter(env);
    }



    @Override
    public String getJobType() {
        return action.getJobType();
    }

    @Override
    protected String getAppName() {
        return "MfShell";
    }
}

