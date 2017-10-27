/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RestAssuredExecutionContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.rest;

import de.hf.dac.testrunner.execution.plugin.DefaultExecutionContext;
import de.hf.dac.testrunner.execution.plugin.Plugin;

/**
 * Anything needed for the RestAssured Plugin to run agains a given
 * Environment
 */
public class RestAssuredExecutionContext extends DefaultExecutionContext {

    public RestAssuredExecutionContext(String env, Plugin plugin) {
        super(env, plugin);
    }

}

