/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SecurityServiceBuilder.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

import de.hf.dac.api.io.env.EnvironmentBasedBuilder;

public interface SecurityServiceBuilder<ACCESS_TYPE extends Enum<ACCESS_TYPE>, RESOURCE_LEVEL extends Enum<RESOURCE_LEVEL>> extends
    EnvironmentBasedBuilder<RootSecurityProvider<ACCESS_TYPE, RESOURCE_LEVEL>> {
}
