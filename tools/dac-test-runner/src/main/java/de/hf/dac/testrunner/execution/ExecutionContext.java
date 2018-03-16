/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ExecutionContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution;

import de.hf.dac.testrunner.execution.plugin.Plugin;

import java.io.Serializable;

/**
 * Each running TestScenario will be bound to an instance of
 * ExecutionContext.
 */
public interface ExecutionContext extends Serializable{
    /**
     * Reset Context
     */
    default void reset() {}

    /**
     * Provide all currently collected References including their values
     * @param references references
     */
    void setReferenceBag(ReferenceBag references);

    /**
     * Get reference to current ReferenceBag holding all variables
     * @return ReferenceBag
     */
    ReferenceBag getReferenceBag();

    /**
     * Get Reference to actual Plugin
     * @return Plugin instance which provided this context instance
     */
    Plugin getPlugin();
}
