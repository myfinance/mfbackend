/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestPlugin.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution.plugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestPlugin {
    String description();
}

