/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestStep.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.dsl;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestStep extends TestItem implements Serializable {


    /**
     * Not used but may provide more context
     */
    private String comment;

    /**
     *Name of the action to be taken
     */
    private String action;
    /**
     * Symbolic name of result to be used in following steps
     */
    private String reference;

    /**
     * Set of Data for this single Step
     * Not used yet. Will be used if this Step is actually a list of sub steps
     */
    private TestData examples;

    /**
     * Mark this step as negative Test. If Action returns false
     * this Step will be considered sucessful
     */
    private boolean shouldFail;

    /**
     * Undefined map of parammeters. Will be provided during runtime
     * processing of actions. Action instances will be injected using these values
     */
    private Map<String, Object> params = Collections.emptyMap();

    /**
     * May be set to conditionally execute Step/group
     */
    private String executeIf;

    /**
     * Simple way of grouping TestSTeps
     */
    private List<TestStep> group = new ArrayList<>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public TestData getExamples() {
        return examples;
    }

    public void setExamples(TestData examples) {
        this.examples = examples;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public boolean isShouldFail() {
        return shouldFail;
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    public String getExecuteIf() {
        return executeIf;
    }

    public void setExecuteIf(String executeIf) {
        this.executeIf = executeIf;
    }

    public List<TestStep> getGroup() {
        return group;
    }

    public void setGroup(List<TestStep> group) {
        this.group = group;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static TestStep load(File file) throws FileNotFoundException {
        TestStep step = load(new FileInputStream(file));
        step.setLocation(file.getAbsolutePath());
        return step;
    }

    public static TestStep load(InputStream resourceAsStream) {
        new Yaml(
        );
        return (TestStep) new Yaml(new Constructor(TestStep.class)).load(resourceAsStream);
    }

    @Override
    public String toString() {
        return "TestStep{" + "action='" + action + '\'' + '}';
    }


}
