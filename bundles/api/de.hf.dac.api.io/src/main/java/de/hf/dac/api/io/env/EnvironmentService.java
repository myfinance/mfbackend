/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentService.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 22.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.env;

import java.util.Set;

/**
 * Lookup Service to translate Environment names to actual Environment Settings.
 *
 */
public interface EnvironmentService {

    /**
     * List all available EnvironmentInfo.
     * @return List of EnvironmentInfo instances
     */
    Set<EnvironmentInfo> availableEnvironments();

    /**
     * Retrieve information of actual target.
     * @param environmentName Name of Environment
     * @param target Target Name in Env
     * @return Concrete Instance containing typed information for the target type. This will differ as targets of type DB may have other needs than
     * targets of type Eisdiele
     */
    EnvironmentTargetInfo getTarget(String environmentName, String target);

    /**
     * Retrieve poet configuration, initially read from ResFiles
     * @return EnvironmentConfiguration parsed
     */
    EnvironmentConfiguration getConfiguration();

}