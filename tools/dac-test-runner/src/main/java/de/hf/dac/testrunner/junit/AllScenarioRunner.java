/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AllScenarioRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.junit;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hf.dac.testrunner.dsl.TestScenario;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionReferenceBag;
import de.hf.dac.testrunner.execution.TestResult;
import de.hf.dac.testrunner.plugins.common.DefaultPlugin;
import de.hf.dac.testrunner.report.ReportGenerator;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AllScenarioRunner extends BaseFileLoadingParentRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AllScenarioRunner.class);

    private ArrayList<ScenarioRunner> allrunner;
    private Description allRunnerDescription;



    /**
     * Constructs a new {@code ParentRunner} that will run {@code @TestClass}
     *
     * @param testClass testClass
     * @throws org.junit.runners.model.InitializationError InitializationError
     */
    public AllScenarioRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        this.allrunner = new ArrayList<>();

        for (TestResult scenario : this.scenarios) {
            allrunner.add(new ScenarioRunner(getTestClass(), scenario));
        }
        getDescription();


    }


    @Override
    public Description getDescription() {
        this.allRunnerDescription = Description.createSuiteDescription(getTestClass().getName());
        for (ScenarioRunner scenarioRunner : allrunner) {
            allRunnerDescription.addChild(scenarioRunner.getDescription());
        }
        return allRunnerDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestStarted(allRunnerDescription);

        List<TestResult<TestScenario>> results = new ArrayList<>();
        for (ScenarioRunner scenarioRunner : this.allrunner) {
            results.add(scenarioRunner.runWithResult(notifier));
        }
        reportResult(results);
        notifier.fireTestFinished(allRunnerDescription);
    }

    private void reportResult(List<TestResult<TestScenario>> scenario) {
        if (getTestClass().isAnnotationPresent(AllScenarioRunner.TestResultReportGenerator.class)) {
            AllScenarioRunner.TestResultReportGenerator annotation = (TestResultReportGenerator) getTestClass()
                .getAnnotation(TestResultReportGenerator.class);
            try {
                ReportGenerator reportGenerator = annotation.value().newInstance();
                String resultDir = annotation.resultDir();
                Map<String, String> configuration = new HashMap<>();
                configuration.put("outputDir", resultDir);
                configuration.put("fileName", getTestClass().getName().replaceAll("\\.", "_") + ".html");
                configuration.put("title", getTestClass().getSimpleName());
                reportGenerator.generate(getTestClass(), scenario, reportGenerator.getReportTypes().iterator().next().name(), configuration);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestFileLocation {
        String directory() default TEST_RESOURCES;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestPlugins {
        Class[] value() default { DefaultPlugin.class };

        String environemt() default "local";
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestResultReportGenerator {
        Class<? extends ReportGenerator> value();

        String resultDir() default "./";
    }


    /**
     * Setup Method to be executed before each Testcase
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BeforeScenario {
    }

    /**
     * Teardown Method to be executed after each Testcase
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AfterScenario {
    }


    /**
     * Setup Method to be executed before each Testcase
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BeforeTestCase {
    }

    /**
     * Teardown Method to be executed after each Testcase
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AfterTestCase {
    }

    /**
     * Setup Method to be executed before each Step
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BeforeStep {
    }

    /**
     * Teardown Method to be executed after each Step
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AfterStep {
    }


    @Override
    protected String getFilefilter() {
        return ".scenario";
    }

    @Override
    protected TestResult<TestScenario> getFailedResult(File file, Throwable e) {
        TestResult<TestScenario> testScenarioTestResult = new TestResult<>(TestResult.RESULT.FAILED_TO_LOAD,
            "Exception loading " + file.getAbsolutePath() + " " + e.getMessage(), new TestScenario());
        testScenarioTestResult.getTestInstance().setName("FAILED TO LOAD " + file.getAbsolutePath());
        return testScenarioTestResult;
    }

    @Override
    protected TestResult<TestScenario> getFromFile(File file) throws FileNotFoundException {
        TestScenario load = TestScenario.load(file);
        load.setLocation(file.getAbsolutePath());
        return new TestResult<>(load);
    }

    @Override
    protected String getName() {
        return getTestClass().getName();
    }

    @Override
    public TestResult run(List<ExecutionContext> ctxt, ExecutionReferenceBag references, ExecutionReferenceBag shared, RunNotifier notifier) {
        return null;
    }

}