/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ReportGeneratorTypes.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.report;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReportGeneratorTypes {
    ReportGeneratorType[] value();
}

