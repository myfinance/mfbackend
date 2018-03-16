/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseMFRunnerParameter.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.runner;

import de.hf.dac.api.io.routes.job.RunnerParameter;
import io.swagger.annotations.ApiModel;

@ApiModel()
public class BaseMFRunnerParameter  extends RunnerParameter {

    public static final String ENV = "env";


    public BaseMFRunnerParameter() {

    }

    public BaseMFRunnerParameter(String env) {
        setEnvironment(env);
    }

    public BaseMFRunnerParameter(BaseMFRunnerParameter toBeCopied) {
        this(toBeCopied.getEnvironment());
        setParams(toBeCopied.getParams());
    }

    public String getEnvironment() {
        return (String) get(ENV);
    }

    public void setEnvironment(String env) {
        put(ENV, env);
    }

}
