/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseRepeatablePluginAction.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.base;

import de.hf.dac.testrunner.dsl.TestData;
import de.hf.dac.testrunner.dsl.TestStep;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.TestExecutionException;
import de.hf.dac.testrunner.execution.plugin.ActionField;
import de.hf.dac.testrunner.execution.plugin.Plugin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static de.hf.dac.testrunner.plugins.common.GroovyAssert.executeLineAsScriptAssertion;

/**
 * Base Plugin Action supporting the Repeated execution including timeout and local assert
 */
public abstract class BaseRepeatablePluginAction extends BasePluginAction {

    private static final long SECOND = 1000;

    public enum NOTIFYSTATUS {
        REPEAT, TIMEDOUT, MAXITERATION
    }

    @ActionField(name = "delay", description = "Time to wait between iterations. Defaults to 2sec")
    private long delay = SECOND * 2;

    @ActionField(name = "maxIterations", description = "Maximum number of iterations. If exceeded this task will fail. Defaults to 1")
    private int maxIterations = 1;

    @ActionField(name = "timeout", description = "Maximal time allowed per iteration. Defaults to 60sec")
    private long timeout = SECOND * 60;

    @ActionField(name = "assertion", description = "If set, this will be evaluated as boolean. ")
    protected String assertion;

    @ActionField(name = "extract", description = "Extract derived value from Action Result using Groovy Expression. \nCurrent result available in current Variable.")
    protected String extract;

    public BaseRepeatablePluginAction() {

        super(null);
    }

    public BaseRepeatablePluginAction(Plugin plugin) {
        super(plugin);
    }

    /**
     * Make this method final to make sure sub classes override the repeatable method
     *
     * @param ctxt            Execution Context
     * @param stepInformation Information including all Params and TestData
     * @param backgroundData  Background Testdata from Parent TestCase
     * @return Result of repeated Action
     * @throws TestExecutionException If action failed
     */
    @Override
    public final Object execute(ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Exception catchedException = null;
        for (int i = 0; i < maxIterations; i++) {
            if (i > 0) {
                waitDelay();
                notify(NOTIFYSTATUS.REPEAT, ctxt, stepInformation, backgroundData, i);
            }
            Future<Object> objectFuture = executorService.submit(() -> executeRepeated(ctxt, stepInformation, backgroundData));
            try {
                // if this returns without Exception simply return
                Object result = objectFuture.get(timeout, TimeUnit.MILLISECONDS);

                return handleCurrentAssert(ctxt, stepInformation, backgroundData, result);

            } catch (TimeoutException e) {
                // the get timed out. retry
                actionTimedOut(i, ctxt, stepInformation, backgroundData);
                catchedException = e;
            } catch (TestExecutionException | InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
                catchedException = e;
            }

        }
        // if this is reached we retried too often
        notify(NOTIFYSTATUS.MAXITERATION, ctxt, stepInformation, backgroundData, maxIterations);
        throw catchedException != null ?
            new TestExecutionException(catchedException.getMessage()) :
            new TestExecutionException("Max Retries. Action failed");
    }

    protected Object handleCurrentAssert(ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData, Object result) {
        if (this.assertion != null) {
            if (extract != null) {
                log.error("Extracting Values only possible if no assertion set.");
            }
            Object evalResult = executeLineAsScriptAssertion(this.assertion, ctxt, stepInformation.getAction(), backgroundData, result);
            if (evalResult instanceof Boolean && (Boolean) evalResult) {
                return result;
            } else {
                setLastMessage("Assertion Failed " + this.assertion + " = " + evalResult);
                throw new TestExecutionException("Assertion Failed " + this.assertion + " = " + evalResult);
            }
        } else if (extract != null) {
            return extractValue(ctxt, stepInformation, backgroundData, result);
        } else {
            return result;
        }
    }

    protected void waitDelay() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    protected Object extractValue(ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData, Object result) {
        Object theretVal = executeLineAsScriptAssertion(this.extract, ctxt, stepInformation.getAction(), backgroundData, result);
        setLastMessage("" + theretVal);
        return theretVal;

    }

    private void notify(NOTIFYSTATUS status, ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData, int i) {
        try {
            switch (status) {
                case REPEAT:
                    actionIsRepeated(i, ctxt, stepInformation, backgroundData);
                    break;
                case MAXITERATION:
                    actionMaxRepeatExceeded(i, ctxt, stepInformation, backgroundData);
                    break;
                case TIMEDOUT:
                    actionTimedOut(i, ctxt, stepInformation, backgroundData);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * As this Action is repeatable, this method needs to be callable repeatedly.
     * It will also be timed out if the timeout value is reached. In this case the Action will be notified
     *
     * @param ctxt ExecutionContext from Plugin
     * @param stepInformation TestStep Definition
     * @param backgroundData Any TestCase Background Data
     * @return result of actual Execution
     * @throws TestExecutionException If not successfull
     */
    public abstract Object executeRepeated(ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData);

    /**
     * Will be called to signal repeat execution. This will not be called for the first execution
     *
     * @param repeatCount Current repeat count (>=1)
     * @param ctxt Execution Context from Plugin
     * @param stepInformation TestStep Definition
     * @param backgroundData Any TestCase Background Data
     */
    public void actionIsRepeated(int repeatCount, ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {

    }

    /**
     * Will be called to signal the timeout event of one iteration
     *
     * @param repeatCount Current repeat count (>=1)
     * @param ctxt Execution Context from Plugin
     * @param stepInformation TestStep Definition
     * @param backgroundData Any TestCase Background Data
     */
    public void actionTimedOut(int repeatCount, ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {

    }

    /**
     * Called if maximum number of iterations tried w/o success
     *
     * @param repeatCount Current repeat count (>=1)
     * @param ctxt Execution Context from Plugin
     * @param stepInformation TestStep Definition
     * @param backgroundData Any TestCase Background Data
     */
    public void actionMaxRepeatExceeded(int repeatCount, ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {

    }

}

