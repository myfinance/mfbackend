/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DefaultPlugin.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.common;

import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.plugin.DefaultExecutionContext;
import de.hf.dac.testrunner.execution.plugin.TestPlugin;
import de.hf.dac.testrunner.plugins.base.ClassBasedPlugin;

@TestPlugin(description = "Number of unspecific Base Actions.")
public class DefaultPlugin extends ClassBasedPlugin {

    public DefaultPlugin() throws NoSuchMethodException {
    }

    @Override
    public ExecutionContext provideContext(String env) {
        return new DefaultExecutionContext(env, this);
    }
}
