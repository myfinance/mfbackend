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

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Lookup Service to translate Environment names to actual Environment Settings.
 *
 */
public interface EnvironmentService {

    /**
     * List all available EnvironmentInfo.
     * @return List of EnvironmentInfo instances
     */
    Collection<EnvironmentInfo> availableEnvironments();

    /**
     * Retrieve Environemnt by ClientID.
     * @param clientID The client ID
     * @return EnvironmentInfo inculding all Target Infos
     */
    EnvironmentInfo getEnvironmentByClientid(String clientID);

    Map<String,String> getEnvironmentContextMap(String env);

    /**
     * Get All EnvironmentInfos.
     * @param env Name of Environment
     * @return EnvironmentInfo including all Targets
     */
    EnvironmentInfo getEnvironment(String env);

    void refresh();


    /**
     * Retrieve information of actual target.
     * @param environmentName Name of Environment
     * @param target Target Name in Env
     * @return Concrete Instance containing typed information for the target type. This will differ as targets of type DB may have other needs than
     * targets of type Eisdiele
     */
    Optional<EnvironmentTargetInfo> getTarget(String environmentName, String target);

    /**
     * Retrieve poet configuration, initially read from ResFiles
     * @return EnvironmentConfiguration parsed
     */
    EnvironmentConfiguration getConfiguration();

}