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

package de.hf.dac.myfinance.test.plugin.api;

import de.hf.dac.myfinance.test.plugin.MdResfileExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.plugin.TestPlugin;
import de.hf.dac.testrunner.plugins.rest.RestAssuredExecutionContext;
import de.hf.dac.testrunner.plugins.rest.RestAssuredPlugin;

import java.util.Map;

@TestPlugin(description = "MD Version of RestAssured Plugin. Using ResFiles for configuration")
public class MdRestAssuredPlugin extends RestAssuredPlugin {
    public MdRestAssuredPlugin() throws Exception {
        findPluginClasses("de.hf.dac.testrunner.plugins.rest");
    }

    public String getName() {
        return "MdRestAssuredPlugin";
    }

    public ExecutionContext provideContext(String env) {
        final MdResfileExecutionContext mdResfileExecutionContext = new MdResfileExecutionContext(this, env);

        return new RestAssuredExecutionContext(env, this){
            @Override
            public Map<String, Object> getProperties() {
                return mdResfileExecutionContext.getProperties();
            }
        };
    }
}
