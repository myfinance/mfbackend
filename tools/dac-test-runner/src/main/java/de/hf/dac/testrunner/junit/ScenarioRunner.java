/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ScenarioRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.junit;

import de.hf.dac.testrunner.dsl.TestCase;
import de.hf.dac.testrunner.dsl.TestScenario;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionReferenceBag;
import de.hf.dac.testrunner.execution.TestExecutionException;
import de.hf.dac.testrunner.execution.TestResult;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.junit.plugin.TestMethodPlugin;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ScenarioRunner extends BaseFileLoadingParentRunner {

    private static Logger LOG = org.slf4j.LoggerFactory.getLogger(ScenarioRunner.class);

    private ArrayList<Plugin> plugins;
    private TestResult<TestScenario> scenario;
    private ArrayList<TestCaseRunner> allrunner;
    private Description description;
    private String environment;

    private Optional<Method> beforeScenarioMethod;
    private Optional<Method> afterScenarioMethod;

    /**
     * Constructs a new {@code ParentRunner} that will run {@code @TestClass}
     *
     * @param testClass testClass
     * @param scenario  scenario
     */
    public ScenarioRunner(Class<?> testClass, TestResult<TestScenario> scenario) {
        super(testClass);

        this.scenario = scenario;
        this.allrunner = new ArrayList<>();
        retrievePlugins();
        for (TestCase testCase : scenario.getTestInstance().getTestCases()) {
            // if case contains background table with more than 0 rows
            int repeat = 1;
            if (testCase.getBackground() != null && testCase.getBackground().getTableRows() != 0) {
                repeat = testCase.getBackground().getTableRows();
            }
            for (int i = 0; i < repeat; i++) {
                TestResult<TestCase> testCaseResult = new TestResult<>(testCase);
                // set index to refer inside background Data
                testCaseResult.setRepeatIndex(i);
                allrunner.add(new TestCaseRunner(getTestClass(), testCaseResult, i, repeat));
                scenario.getChildren().add(testCaseResult);
            }
        }

        // find BeforeTestCase / AfterTestCase Method
        // find BeforeStep / AfterStep Methods
        findLifeCycleMethods();

    }

    private void findLifeCycleMethods() {
        beforeScenarioMethod = TestMethodPlugin.getAllAnnotatedMethods(getTestClass(), AllScenarioRunner.BeforeScenario.class).stream().limit(1).findFirst();
        afterScenarioMethod = TestMethodPlugin.getAllAnnotatedMethods(getTestClass(), AllScenarioRunner.AfterScenario.class).stream().limit(1).findFirst();
    }


    private void retrievePlugins() {
        AllScenarioRunner.TestPlugins annotation = (AllScenarioRunner.TestPlugins) getTestClass().getAnnotation(AllScenarioRunner.TestPlugins.class);
        this.plugins = new ArrayList<>();
        if (annotation != null) {
            for (Class<? extends Plugin> aClass : annotation.value()) {
                try {
                    try {
                        Constructor<? extends Plugin> constructor = aClass.getConstructor(Class.class);
                        if (constructor != null) {
                            plugins.add(constructor.newInstance(getTestClass()));
                        }
                    } catch (Exception ex) {
                        plugins.add(aClass.newInstance());
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    protected String getFilefilter() {
        return ".testcase";
    }

    @Override
    protected TestResult<TestCase> getFailedResult(File file, Throwable e) {
        TestResult<TestCase> testCaseTestResult = new TestResult<>(TestResult.RESULT.FAILED_TO_LOAD,
            "Exception loading " + file.getAbsolutePath() + " " + e.getMessage(), new TestCase());
        testCaseTestResult.getTestInstance().setName("FAILED TO LOAD : " + file.getAbsolutePath());
        return testCaseTestResult;
    }

    @Override
    protected TestResult<TestCase> getFromFile(File file) throws FileNotFoundException {
        return new TestResult<>(TestCase.load(new FileInputStream(file)));
    }

    @Override
    protected String getName() {
        return scenario.getTestInstance().getName();
    }

    @Override
    public TestResult run(List<ExecutionContext> ctxt, ExecutionReferenceBag references, ExecutionReferenceBag sharedState, RunNotifier notifier) {
        return scenario;
    }

    @Override
    public Description getDescription() {
        description = Description.createSuiteDescription(scenario.getTestInstance().getName(), UUID.randomUUID());
        for (TestCaseRunner caseRunner : allrunner) {
            description.addChild(caseRunner.getDescription());
        }

        return description;
    }

    public TestResult runWithResult(RunNotifier notifier) {
        notifier.fireTestStarted(description);

        boolean failed = false;
        int failCount = 0;
        int ignoreCount = 0;

        // prepare contexts for each plugin
        List<ExecutionContext> ctxt = new ArrayList<>();
        ExecutionReferenceBag references = new ExecutionReferenceBag();
        // Repeated TestCase RefernceBag
        ExecutionReferenceBag repeatedReferencesBag = new ExecutionReferenceBag();
        for (Plugin plugin : plugins) {
            ExecutionContext e = plugin.provideContext(getEnvironmentFromAnnotation());
            if (e != null) {
                ctxt.add(e);
                e.setReferenceBag(references);
            } else {
                LOG.warn("Plugin {} doesn't provide valid ExecutionContext", plugin.getName());
            }
        }

        // call lifecycleMethod
        if (beforeScenarioMethod.isPresent()) {
            invokeWithPossibleParams(beforeScenarioMethod, getTestClass(), this.scenario.getTestInstance(), references);
        }

        for (TestCaseRunner testCaseRunner : allrunner) {
            try {
                ctxt.stream().forEach(ExecutionContext::reset);
            } catch (Throwable throwable) {
                LOG.warn(throwable.getMessage(), throwable);
            }

            // reset the referncesBag used for this single TestCase Execution
            references.reset();

            // only reset this if we are in the next iteration. So Testcases with multiple repetitions will keep a shared state
            if (testCaseRunner.getRepeatIndex() == 0) {
                repeatedReferencesBag.reset();
            } else {
                // copy shared state to references bag
                repeatedReferencesBag.getBag().forEach((k, v) ->{
                    references.set(k, v);
                });
            }
            TestResult result = testCaseRunner.run(ctxt, references, repeatedReferencesBag, notifier);
            if (!failed) {
                failed = result.getResult() == TestResult.RESULT.NOK;
            }
            failCount += result.getResult() == TestResult.RESULT.OK ? 0 : 1;
            ignoreCount += result.getResult() == TestResult.RESULT.IGNORED ? 1 : 0;
        }
        if (failed) {
            scenario.setMessage(String.format("%d of %d Testcases failed.", failCount, scenario.getChildren().size()));
            scenario.setResult(TestResult.RESULT.NOK);
        } else {
            scenario.setMessage("Success");
            scenario.setResult(TestResult.RESULT.OK);
        }

        if (ignoreCount == allrunner.size()) {
            scenario.setMessage("Scenario Ignored completely");
            scenario.setResult(TestResult.RESULT.IGNORED);
        }

        // call lifecycleMethod
        if (afterScenarioMethod.isPresent()) {
            invokeWithPossibleParams(afterScenarioMethod, getTestClass(), this.scenario, references);
        }
        notifier.fireTestFinished(description);
        return scenario;
    }

    public static void invokeWithPossibleParams(Optional<Method> method, Class testClass, Object... args) {
        if (method.isPresent()) {
            try {
                if (args != null && args.length > 0 && method.get().getParameterTypes().length != 0) {
                    Class<?>[] parameterTypes = method.get().getParameterTypes();
                    Object[] methodArgs = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        for (Object arg : args) {
                            if (arg.getClass().isAssignableFrom(parameterTypes[i])) {
                                methodArgs[i] = arg;
                            }
                        }
                    }
                    method.get().invoke(testClass.newInstance(), methodArgs);
                } else {

                    method.get().invoke(testClass.newInstance());

                }
            } catch (Exception e) {
                throw new TestExecutionException("Unable to execute " + method.get().getName(), e);
            }
        }
    }


    @Override
    public void run(RunNotifier notifier) {
        // Not used. Using RunwitResults
    }

    private String getEnvironmentFromAnnotation() {
        if (environment == null) {
            if (getTestClass().isAnnotationPresent(AllScenarioRunner.TestPlugins.class)) {
                AllScenarioRunner.TestPlugins annotation = (AllScenarioRunner.TestPlugins) getTestClass()
                    .getAnnotation(AllScenarioRunner.TestPlugins.class);
                environment = annotation.environemt();
            } else {
                environment = "local";
            }
        }

        return environment;
    }
}
