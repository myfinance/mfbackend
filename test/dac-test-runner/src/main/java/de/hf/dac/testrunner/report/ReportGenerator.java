/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ReportGenerator.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.report;

import de.hf.dac.testrunner.dsl.TestScenario;
import de.hf.dac.testrunner.execution.TestResult;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReportGenerator extends Serializable {
    Set<ReportGeneratorType> getReportTypes();

    ByteArrayOutputStream generate(Class annotatedTestClass, List<TestResult<TestScenario>> testResult, String type, Map<String, String> configuration);

}
