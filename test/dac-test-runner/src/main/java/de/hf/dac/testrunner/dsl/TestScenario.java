/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestScenario.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.dsl;

import de.hf.dac.testrunner.datetime.DateTimePropertyConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wrapper around TestScenario.
 */
public class TestScenario extends TestItem implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TestScenario.class);

    private String name;
    private String author;
    private String description;
    private LocalDateTime createAt;
    private Long version;

    private List<TestCase> testCases = Collections.emptyList();

    public TestScenario() {
        // unusual construction
    }

    public static TestScenario load(File f) throws FileNotFoundException {
        TestScenario scenario = load(new FileInputStream(f));
        scenario.setLocation(f.getAbsolutePath());
        // first check all testcases if they are fully loaded
        handleIncludedTestCases(f, scenario);
        // second pass check testSteps for inclusions
        handleIncludedTestSteps(scenario);
        return scenario;
    }

    protected static void handleIncludedTestSteps(TestScenario scenario) {


        scenario.testCases.stream().filter(testCase -> testCase.getSteps() != null).forEach(testCase -> {
            List<TestStep> allSteps = new ArrayList<>();
            File parent = new File(testCase.getLocation());
            for (TestStep testStep : testCase.getSteps()) {
                if (testStep.getLocation() != null) {
                    File locationFile = getLocationFile(parent.getParentFile(), testStep.getLocation());
                    TestStep includedStep = null;
                    try {
                        includedStep = TestStep.load(locationFile);
                        allSteps.add(includedStep);
                    } catch (FileNotFoundException e) {
                        LOG.error(e.getMessage(), e);
                    }
                } else {
                    allSteps.add(testStep);
                    testStep.setLocation(parent.getAbsolutePath());
                }
            }
            testCase.setSteps(allSteps);
        });

    }

    private static void handleIncludedTestCases(File f, TestScenario scenario) throws FileNotFoundException {
        if (scenario.getTestCases() != null) {
            List<TestCase> allCases = new ArrayList<>();
            for (TestCase testCase : scenario.getTestCases()) {
                // during load this is used for include feature
                if (testCase.getLocation() != null) {
                    // only suppport relative path
                    File locationFile = getLocationFile(f.getParentFile(), testCase.getLocation());
                    TestCase includedTestCase = TestCase.load(locationFile);
                    allCases.add(includedTestCase);
                } else {
                    allCases.add(testCase);
                    testCase.setLocation(f.getAbsolutePath());
                }
            }
            scenario.setTestCases(allCases);
        }
    }

    private static File getLocationFile(File parent, String location) {
        File testRel = new File(parent, location);
        if (!testRel.exists()) {
            testRel = new File(location);
        }
        return testRel;
    }

    public static TestScenario load(InputStream inputStream) {
        Yaml yaml = new Yaml(new DateTimePropertyConstructor(TestScenario.class));
        return (TestScenario) yaml.load(new InputStreamReader(inputStream, Charset.forName("utf-8")));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    @Override
    public String toString() {
        return "TestScenario{" + "name='" + name + '\'' + ", author='" + author + '\'' + ", description='" + description + '\'' + ", createAt="
            + createAt + ", version=" + version + '}';
    }
}

