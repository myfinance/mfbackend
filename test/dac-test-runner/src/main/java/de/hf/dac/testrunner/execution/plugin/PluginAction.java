/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : PluginAction.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution.plugin;

import de.hf.dac.testrunner.dsl.TestData;
import de.hf.dac.testrunner.dsl.TestStep;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionReferenceBag;
import de.hf.dac.testrunner.execution.TestExecutionException;
import java.io.Serializable;

public interface PluginAction extends Serializable {
    default String getName() {
        return this.getClass().getName();
    }

    Plugin getPlugin();

    /**
     * Running the TestStep using this Plugin-provided Action Impl.
     *
     * @param ctxt Execution Context
     * @param stepInformation Information including all Params and TestData
     * @param backgroundData Background Testdata from Parent TestCase
     * @return May return a result or Null
     * @throws TestExecutionException Failed Tests need to be signaled by throwing this Exception
     */
    Object execute(ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData);

    default String getLastMessage() {
        return "";
    }

    default boolean shouldFail() {return false;}

    default void injectParameters(TestStep step, ExecutionReferenceBag references) {
        PluginActionFieldInjector.inject(this, step.getParams(), references);
    }
}