/** ----------------------------------------------------------------------------
 *
 * ---         HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ContextBuilderConfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 26.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "DAC ContextBuilderConfiguration")
public @interface ContextBuilderConfiguration {
    boolean enableCaching() default true;
}
