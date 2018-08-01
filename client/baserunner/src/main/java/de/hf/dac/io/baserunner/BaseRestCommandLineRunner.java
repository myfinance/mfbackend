/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseRestCommandLineRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.baserunner;

import de.hf.dac.api.io.routes.job.RunnerParameter;

/**
 * Base class for all runners which starts jobs in an container via rest interface
 */
public abstract class BaseRestCommandLineRunner extends BaseRunner {

    public BaseRestCommandLineRunner() {
        this.optionsParser = new OptionsParser();
    }

    /**
     * Run.
     *
     * @param args the args
     * @throws Exception the exception
     */
    @Override
    public void run(String[] args) throws Exception {
        int rc = 0;

        try {
            super.run(args);

            this.passParamsToExternal(this.extractParameters());

        } catch (Exception e) {
            rc = -1;
            log.error(e.getLocalizedMessage(), e);
        } finally {
            this.shutdown(rc);
        }
    }



    protected abstract RunnerParameter extractParameters();

    protected abstract void passParamsToExternal( RunnerParameter var);

}

