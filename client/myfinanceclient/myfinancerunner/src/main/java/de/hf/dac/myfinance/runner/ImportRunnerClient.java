/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ImportRunnerClient.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner;

import de.hf.dac.api.io.routes.job.RunnerParameter;
import de.hf.dac.myfinance.api.runner.BaseMFRunnerParameter;

public class ImportRunnerClient extends BaseMFRunnerClient {

    /**
     * @param args
     *        The command line arguments
     */
    public static void main(String[] args) {

        try {
            new ImportRunnerClient().run(args);
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
