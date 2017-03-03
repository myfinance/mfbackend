/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ImportRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.runner;

import de.hf.dac.api.io.routes.job.RunnerParameter;
import de.hf.dac.marketdataprovider.api.runner.ImportRunnerParameter;

public class ImportRunner extends BaseMDRunner {

    /**
     * @param args
     *        The command line arguments
     */
    public static void main(String[] args) {

        try {
            new ImportRunner().run(args);
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
        return new ImportRunnerParameter(env, ImportRunnerParameter.IMPORTTYPES.YAHOO);
    }


}
