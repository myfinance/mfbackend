/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestCase.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.dsl;

import de.hf.dac.testrunner.datetime.DateTimePropertyConstructor;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * One single Test Case
 */
public class TestCase extends TestItem implements Serializable {
    private String name;
    private String description;
    /**
     * May be set to conditionally execute Step/group
     */
    private String executeIf;
    private TestData background;

    private List<TestStep> steps = Collections.emptyList();

    private List<TestStep> beforeSteps = Collections.emptyList();
    private List<TestStep> afterSteps = Collections.emptyList();

    public TestCase() {
        // unusual way to construct
    }

    public static TestCase load(File file) throws FileNotFoundException {
        TestCase testCase = load(new FileInputStream(file));
        testCase.setLocation(file.getAbsolutePath());
        return testCase;
    }

    public static TestCase load(InputStream inputStream) {
        Yaml yaml = new Yaml(new DateTimePropertyConstructor(TestCase.class));
        return (TestCase) yaml.load(new InputStreamReader(inputStream, Charset.forName("utf-8")));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TestStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TestStep> steps) {
        this.steps = steps;
    }

    public TestData getBackground() {
        return background;
    }

    public void setBackground(TestData background) {
        this.background = background;
    }
    public String getExecuteIf() {
        return executeIf;
    }

    public void setExecuteIf(String executeIf) {
        this.executeIf = executeIf;
    }

    public List<TestStep> getBeforeSteps() {
        return beforeSteps;
    }

    public void setBeforeSteps(List<TestStep> beforeSteps) {
        this.beforeSteps = beforeSteps;
    }

    public List<TestStep> getAfterSteps() {
        return afterSteps;
    }

    public void setAfterSteps(List<TestStep> afterSteps) {
        this.afterSteps = afterSteps;
    }

    @Override
    public String toString() {
        return "TestCase{" + "name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }


}
