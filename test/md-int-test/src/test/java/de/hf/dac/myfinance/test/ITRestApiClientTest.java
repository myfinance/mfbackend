/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ITRestApiClientTest.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.test;

import de.hf.dac.myfinance.test.plugin.api.MdRestAssuredPlugin;
import de.hf.dac.testrunner.junit.AllScenarioRunner;
import de.hf.dac.testrunner.plugins.common.DefaultPlugin;
import de.hf.dac.testrunner.report.impl.HtmlDashboardGenerator;
import org.junit.runner.RunWith;

@RunWith(AllScenarioRunner.class)
@AllScenarioRunner.TestPlugins(environemt = "local", value={MdRestAssuredPlugin.class, DefaultPlugin.class})
@AllScenarioRunner.TestFileLocation(directory = "./src/test/resources/tests/apiclient")
@AllScenarioRunner.TestResultReportGenerator(value = HtmlDashboardGenerator.class, resultDir = "./target/reports")
public class ITRestApiClientTest {
}
