/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : Plugin.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution.plugin;

import de.hf.dac.testrunner.execution.ExecutionContext;

import java.io.Serializable;
import java.util.List;

/**
 * In order to actually execute the defined Tests against a System under Test
 * we need the implmenetation for each test step used in the test specs.
 * Plugins will be discovered automatically
 *
 */
public interface Plugin extends Serializable {
    /**
     * Will be called for each Test Run per Scenario
     * @return ExecutionContext Instance
     * @param env Name of environment to be used. (Plugins may resolve this to whtever config they like
     */
    ExecutionContext provideContext(String env);

    /**
     * Each Plugin knows how to execute a set of TestStep.actions by their name.
     * @return List of all known / implemented TestStep.action
     */
    List<String> getPluginActions();

    PluginAction getAction(String name);

    default String getName( ) {
        return this.getClass().getName();
    }
}
