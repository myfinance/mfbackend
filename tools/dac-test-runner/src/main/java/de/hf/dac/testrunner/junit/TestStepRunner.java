/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestStepRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.junit;

import de.hf.dac.testrunner.dsl.TestData;
import de.hf.dac.testrunner.dsl.TestStep;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionReferenceBag;
import de.hf.dac.testrunner.execution.TestResult;
import de.hf.dac.testrunner.execution.plugin.DefaultExecutionContext;
import de.hf.dac.testrunner.execution.plugin.PluginAction;
import de.hf.dac.testrunner.junit.plugin.TestMethodPlugin;
import de.hf.dac.testrunner.plugins.common.DefaultPlugin;
import de.hf.dac.testrunner.plugins.common.GroovyAssert;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TestStepRunner extends Runner {

    private static final Logger LOG = LoggerFactory.getLogger(TestStepRunner.class);

    private final Class testClass;
    private final TestResult<TestStep> testStep;
    private List<TestStepRunner> children = new ArrayList<>();
    private Description description;

    private Optional<Method> beforeStepMethod;
    private Optional<Method> afterStepMethod;


    public TestStepRunner(Class testClass, TestResult<TestStep> testStep) {
        this.testClass = testClass;
        this.testStep = testStep;
        description = Description.createTestDescription(TestStep.class.getName(), getName(), UUID.randomUUID());
        beforeStepMethod = TestMethodPlugin.getAllAnnotatedMethods(testClass, AllScenarioRunner.BeforeStep.class).stream().limit(1).findFirst();
        afterStepMethod = TestMethodPlugin.getAllAnnotatedMethods(testClass, AllScenarioRunner.AfterStep.class).stream().limit(1).findFirst();
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestStarted(description);
        notifier.fireTestFinished(description);
    }

    public String getName() {
        return testStep.getTestInstance().getAction();
    }

    public TestResult run(List<ExecutionContext> ctxt, ExecutionReferenceBag references, RunNotifier notifier, TestData background) {
        TestStep step = testStep.getTestInstance();
        LOG.debug("Running TestStep {} within {}", testStep.getTestInstance(), testClass.getName());
        notifier.fireTestStarted(description);

        // evaluate ifCondition if this Step is skipped. Will be marked as ignored
        Boolean executeif = checkConditionalExecution(references, background);

        // recursive execution

        if (testStep.getTestInstance().isIgnored() || !executeif) {
            testStep.setResult(TestResult.RESULT.IGNORED);
            testStep.setMessage(!executeif ? "executeIf == false" : "TestStepIgnored");
            notifier.fireTestIgnored(description);
        } else {
            if (beforeStepMethod.isPresent()) {
                ScenarioRunner.invokeWithPossibleParams(beforeStepMethod, testClass, this.testStep.getTestInstance(), references);
            }
            // lookup plugin action
            try {
                Object[] object = ctxt.stream().filter(c -> c.getPlugin().getPluginActions().contains(step.getAction())).toArray();
                if (object != null && object.length != 0) {
                    ExecutionContext actionProvider = (ExecutionContext) object[0];
                    PluginAction action = actionProvider.getPlugin().getAction(step.getAction());

                    if (action != null) {
                        injectParamsAndExecute(references, notifier, background, step, actionProvider, action);
                    } else {
                        testStep
                            .resultNOK(String.format("Plugin %s has no Action of name %s", step.getAction(), actionProvider.getPlugin().getName()));
                    }
                } else {
                    testStep.resultIgnored(String.format("No Implementation Found for %s in %s", step.getAction(), testClass.getName()));
                }
            } catch (Exception ex) {
                testStep.resultNOK(ex);
                Assert.fail(ex.getMessage());
            } finally {
                notifier.fireTestFinished(description);
                if (afterStepMethod.isPresent()) {
                    ScenarioRunner.invokeWithPossibleParams(afterStepMethod, testClass, this.testStep, references);
                }
            }

        }
        return testStep;
    }

    private void injectParamsAndExecute(ExecutionReferenceBag references, RunNotifier notifier, TestData background, TestStep step,
        ExecutionContext actionProvider, PluginAction action) {
        try {
            // allow the action to override Parameter injection for advanced use cases
            action.injectParameters(step, references);

            Object result = action.execute(actionProvider, step, background);

            if (step.getReference() != null && !"".equalsIgnoreCase(step.getReference())) {
                references.set(step.getReference(), result);
            }

            if (testStep.getTestInstance().isShouldFail() || action.shouldFail()) {
                testStep.resultNOK("Should have failed.");
                notifier.fireTestFailure(new Failure(description, new AssertionError("Action was marked to fail but returned success")));
            } else {
                testStep.resultOk(action.getLastMessage());
            }
        } catch (Exception e) {
            if (testStep.getTestInstance().isShouldFail() || action.shouldFail()) {
                testStep.resultOk("Expected Fail: " + e.getMessage());
            } else {
                testStep.resultNOK(e);
                notifier.fireTestFailure(new Failure(description, new AssertionError(e.getMessage())));
            }
        }
    }

    private Boolean checkConditionalExecution(ExecutionReferenceBag references, TestData background) {
        Boolean result = true;
        if (testStep.getTestInstance().getExecuteIf() != null) {
            try {
                ExecutionContext dummyPLugin = new DefaultExecutionContext("ll", new DefaultPlugin());
                dummyPLugin.setReferenceBag(references);
                result = (Boolean) GroovyAssert
                    .executeLineAsScriptAssertion(testStep.getTestInstance().getExecuteIf(), dummyPLugin, testStep.getTestInstance().getAction(), background,
                        false);

            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public void addParams(Map<String, Object> bag) {
        testStep.getParams().putAll(bag);
    }

    public List<TestStepRunner> getChildren() {
        return children;
    }
}


