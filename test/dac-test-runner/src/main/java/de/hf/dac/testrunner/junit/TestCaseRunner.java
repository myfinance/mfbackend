/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestCaseRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.junit;

import de.hf.dac.testrunner.dsl.TestCase;
import de.hf.dac.testrunner.dsl.TestData;
import de.hf.dac.testrunner.dsl.TestStep;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionReferenceBag;
import de.hf.dac.testrunner.execution.TestResult;
import de.hf.dac.testrunner.execution.plugin.DefaultExecutionContext;
import de.hf.dac.testrunner.junit.plugin.TestMethodPlugin;
import de.hf.dac.testrunner.plugins.common.DefaultPlugin;
import de.hf.dac.testrunner.plugins.common.GroovyAssert;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TestCaseRunner extends BaseFileLoadingParentRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TestCaseRunner.class);
    private final int max;

    private List<TestStepRunner> children;
    private List<TestStepRunner> beforeSteps = Collections.emptyList();
    private List<TestStepRunner> afterSteps  = Collections.emptyList();
    private final int repeat;
    private TestResult<TestCase> testCase;
    private Description description;

    private Optional<Method> beforeTestCaseMethod;
    private Optional<Method> afterTestCaseMethod;


    public TestCaseRunner(Class<?> testClass, TestResult<TestCase> testCase, int repeat, int max) {
        super(testClass);
        this.testCase = testCase;
        this.children = extractTestRunner(testCase.getTestInstance().getSteps());
        // before steps only done in first iteration
        if (repeat == 0) {
            this.beforeSteps = extractTestRunner(testCase.getTestInstance().getBeforeSteps());
        }
        if (repeat == (max-1)) {
            this.afterSteps = extractTestRunner(testCase.getTestInstance().getAfterSteps());
        }
        this.repeat = repeat;
        this.max = max;
        beforeTestCaseMethod = TestMethodPlugin.getAllAnnotatedMethods(getTestClass(), AllScenarioRunner.BeforeTestCase.class).stream().limit(1).findFirst();
        afterTestCaseMethod = TestMethodPlugin.getAllAnnotatedMethods(getTestClass(), AllScenarioRunner.AfterTestCase.class).stream().limit(1).findFirst();
    }

    @Override
    public Description getDescription() {

        TestData background = testCase.getTestInstance().getBackground();
        StringBuilder builder = new StringBuilder();
        if (background != null && background.getTableRows() != 0) {
            builder.append("[");
            for (String s : background.getIdCols()) {
                if (builder.length() != 1) {
                    builder.append(" ,");
                }
                builder.append(background.getColumnValue(repeat, s));
            }
            builder.append("]");
        }
        this.testCase.setDisplayName(testCase.getTestInstance().getName() + builder.toString());
        this.description = Description.createSuiteDescription(testCase.getDisplayName(), UUID.randomUUID());
        List<TestStepRunner>[] all = new List[]{beforeSteps, children, afterSteps};
        for (List<TestStepRunner> testStepRunners : all) {
            for (TestStepRunner child : testStepRunners) {
                description.addChild(child.getDescription());
            }
        }
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestStarted(this.description);

        for (TestStepRunner child : children) {
            child.run(notifier);
        }

        notifier.fireTestFinished(this.description);
    }

    @Override
    protected String getFilefilter() {
        return ".case";
    }

    private List<TestStepRunner> extractTestRunner(List<TestStep> steps) {

        List<TestStepRunner> all = new ArrayList<>();
        for (TestStep testStep : steps) {
            // recursively create TestStepRunners as each Step may actuall be a group instead
            TestResult<TestStep> testResult = new TestResult<>(testStep);
            TestStepRunner result = createStepRunner(testResult);
            if (result != null) {
                testCase.getChildren().add(testResult);
                all.add(result);
            }
        }
        return all;

    }

    private TestStepRunner createStepRunner(TestResult<TestStep> testStep) {
        TestStepRunner runner = new TestStepRunner(getTestClass(), testStep);
        TestStep step = testStep.getTestInstance();
        return runner;
    }

    @Override
    protected TestResult<TestCase> getFailedResult(File file, Throwable e) {
        TestResult<TestCase> testCaseTestResult = new TestResult<>(TestResult.RESULT.FAILED_TO_LOAD,
            "Exception loading " + file.getAbsolutePath() + " " + e.getMessage(), new TestCase());
        testCaseTestResult.getTestInstance().setName("FAILED TO LOAD " + file.getAbsolutePath());
        return testCaseTestResult;
    }

    @Override
    protected TestResult<TestCase> getFromFile(File file) throws FileNotFoundException {
        return new TestResult<>(TestCase.load(new FileInputStream(file)));
    }

    @Override
    protected String getName() {
        return testCase.getTestInstance().getName();
    }

    @Override
    public TestResult run(List<ExecutionContext> ctxt, ExecutionReferenceBag references, ExecutionReferenceBag sharedState,RunNotifier notifier) {
        boolean failed = false;
        int failCount = 0;
        notifier.fireTestStarted(description);

        // evaluate ifCondition if this Step is skipped. Will be marked as ignored
        Boolean executeif = checkConditionalExecution(references, new TestData());

        // recursive execution

        if (isIgnored(testCase, notifier) || !executeif) {
            testCase.setResult(TestResult.RESULT.IGNORED);
            testCase.setMessage(!executeif ? "executeIf == false" : "TestCaseIgnored");
            notifier.fireTestIgnored(description);
        } else {

            prepareReferencebagFromBackgroundData(references);

            if (beforeTestCaseMethod.isPresent()) {
                ScenarioRunner.invokeWithPossibleParams(beforeTestCaseMethod, getTestClass(), this.testCase.getTestInstance(), references);
            }

            for (TestStepRunner child : beforeSteps) {
                child.addParams(sharedState.getBag());
                TestResult result = child.run(ctxt, sharedState, notifier, testCase.getTestInstance().getBackground());
                failed = !failed ? result.getResult() == TestResult.RESULT.NOK : failed;
                failCount += result.getResult() == TestResult.RESULT.NOK ? 1 : 0;

            }

            for (TestStepRunner child : children) {
                child.addParams(references.getBag());
                child.addParams(sharedState.getBag());

                references.getBag().putAll(sharedState.getBag());

                TestResult result = child.run(ctxt, references, notifier, testCase.getTestInstance().getBackground());
                failed = !failed ? result.getResult() == TestResult.RESULT.NOK : failed;
                failCount += result.getResult() == TestResult.RESULT.NOK ? 1 : 0;

            }

            for (TestStepRunner child : afterSteps) {
                child.addParams(references.getBag());
                child.addParams(sharedState.getBag());

                references.getBag().putAll(sharedState.getBag());

                TestResult result = child.run(ctxt, references, notifier, testCase.getTestInstance().getBackground());
                failed = !failed ? result.getResult() == TestResult.RESULT.NOK : failed;
                failCount += result.getResult() == TestResult.RESULT.NOK ? 1 : 0;

            }

            if (failCount != 0) {
                testCase.setMessage(String.format("%d of %d TestCases failed.", failCount, children.size()));
                testCase.setResult(TestResult.RESULT.NOK);
            } else {
                testCase.setMessage("Success");
                testCase.setResult(TestResult.RESULT.OK);
            }

            if (afterTestCaseMethod.isPresent()) {
                ScenarioRunner.invokeWithPossibleParams(afterTestCaseMethod, getTestClass(), this.testCase, references);
            }


        }

        notifier.fireTestFinished(description);
        return testCase;
    }

    private Boolean checkConditionalExecution(ExecutionReferenceBag references, TestData background) {
        Boolean result = true;
        if (testCase.getTestInstance().getExecuteIf() != null) {
            try {
                ExecutionContext dummyPLugin = new DefaultExecutionContext("ll", new DefaultPlugin());
                dummyPLugin.setReferenceBag(references);
                result = (Boolean) GroovyAssert
                    .executeLineAsScriptAssertion(testCase.getTestInstance().getExecuteIf(), dummyPLugin, testCase.getTestInstance().getName(), background,
                        false);

            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return result;
    }

    protected void prepareReferencebagFromBackgroundData(ExecutionReferenceBag references) {
        if (testCase.getTestInstance().getBackground() != null && testCase.getTestInstance().getBackground().getTableRows() != 0) {
            try {
                Map<String, Object> stringObjectMap = testCase.getTestInstance().getBackground().getDataTable().get(repeat);
                for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                    references.set(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private boolean isIgnored(TestResult<TestCase> testCase, RunNotifier notifier) {
        if (testCase.getTestInstance().isIgnored()) {
            this.testCase.setResult(TestResult.RESULT.IGNORED);
            this.testCase.setMessage("TestCaseIgnored");
            notifier.fireTestIgnored(description);
            return true;
        } else {
            return false;
        }
    }

    public int getRepeatIndex(){
        return testCase.getRepeatIndex();
    }

}

