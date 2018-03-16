/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RestAssuredPlugin.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.rest;

import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.plugin.TestPlugin;
import de.hf.dac.testrunner.plugins.base.ClassBasedPlugin;

/**
 * Provide Plugin Actions for
 */
@TestPlugin(description = "Test Plugin providing Actions based on the RestAssured Library")
public class RestAssuredPlugin extends ClassBasedPlugin {

    public RestAssuredPlugin() throws NoSuchMethodException {
    }

    @Override
    public String getName() {
        return "RestAssuredPlugin";
    }

    @Override
    public ExecutionContext provideContext(String env) {
        return new RestAssuredExecutionContext(env, this);
    }
}
