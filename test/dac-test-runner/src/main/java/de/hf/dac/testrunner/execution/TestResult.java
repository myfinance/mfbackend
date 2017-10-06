/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestResult.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution;

import de.hf.dac.testrunner.dsl.TestItem;

import java.io.Serializable;
import java.util.*;

public class TestResult<T extends TestItem> {
    private Serializable uid;
    private int repeatIndex = 0;

    public enum RESULT {
        READY, IGNORED, OK, NOK, FAILED_TO_LOAD;
    }

    /**
     * Indication of status
     */
    private RESULT result;
    /**
     * Message describing the current status
     */
    private String message;
    /**
     * Used instance of Test[Scenario|TestCase|TestStep]
     */
    private T testInstance;
    /**
     * List of children.
     */
    private List<TestResult<?>> children = new ArrayList<>();

    /**
     * Used ReferenceBag Values
     * @param
     */
    private Map<String, Object> params = new HashMap<>();

    private String displayName;

    public TestResult(RESULT result, String s, T any) {
        this.result = result;
        this.message = s;
        this.testInstance = any;
        this.uid = UUID.randomUUID();
    }


    public TestResult(T testInstance) {
        this.testInstance = testInstance;
        message = "Ready to be run";
        result = RESULT.READY;
        this.uid = UUID.randomUUID();
    }

    public void setResult(RESULT result, String message) {
        setResult(result);
        setMessage(message);
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public RESULT getResult() {
        return result;
    }

    public void setResult(RESULT result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getTestInstance() {
        return testInstance;
    }

    public void setTestInstance(T testInstance) {
        this.testInstance = testInstance;
    }

    public List<TestResult<?>> getChildren() {
        return children;
    }

    public void setChildren(List<TestResult<?>> children) {
        this.children = children;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public int getRepeatIndex() {
        return repeatIndex;
    }

    public void setRepeatIndex(int repeatIndex) {
        this.repeatIndex = repeatIndex;
    }

    @Override
    public String toString() {
        return "TestResult{" + testInstance.toString() + " result=" + result + ", message='" + message + '\'' + '}';
    }

    public Serializable getUid() {
        return uid;
    }

    public void setUid(Serializable uid) {
        this.uid = uid;
    }

    public void resultOk(String message) {
        setResult(RESULT.OK);
        setMessage(message != null && message.length() != 0 ? message.substring(0,Math.min(30, message.length())) : "Success");
    }

    public void resultNOK(Throwable thr) {
        resultNOK(thr.getMessage());
    }

    public void resultNOK(String message) {
        setResult(RESULT.NOK);
        setMessage(message != null ? message : "Failed");
    }

    public void resultIgnored(String message) {
        setResult(RESULT.IGNORED);
        setMessage(message != null ? message : "Ignored");
    }
}

