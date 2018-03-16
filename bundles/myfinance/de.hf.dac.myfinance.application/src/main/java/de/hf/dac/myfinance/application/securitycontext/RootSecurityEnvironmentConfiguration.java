/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RootSecurityEnvironmentConfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 19.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.securitycontext;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Not all Requests have an Environment e.G. listRunner (there are jobs for multiple environments listed).
 * so I have to configure a environment to secure these operations as well
 */
@ObjectClassDefinition(name = "Runner Security Source Configuration")
public @interface RootSecurityEnvironmentConfiguration {
    String sourceEnvironmentForSecurityDB() default "md";
}
