/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseFileLoadingParentRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.junit;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionReferenceBag;
import de.hf.dac.testrunner.execution.TestResult;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseFileLoadingParentRunner extends Runner {

    private static final Logger LOG = LoggerFactory.getLogger(BaseFileLoadingParentRunner.class);

    public static final String TEST_RESOURCES = "./src/test/resources";

    protected List<TestResult> scenarios;
    private Class<?> testClass;

    /**
     * Constructs a new {@code ParentRunner} that will run {@code @TestClass}
     *
     * @param testClass testClass
     */
    protected BaseFileLoadingParentRunner(Class<?> testClass) {
        this.testClass = testClass;

        this.scenarios = loadFiles();
    }

    public Class getTestClass() {
        return testClass;
    }

    public List<TestResult> loadFiles() {
        AllScenarioRunner.TestFileLocation annotation = testClass.getAnnotation(AllScenarioRunner.TestFileLocation.class);
        String dir = TEST_RESOURCES;
        String filter = getFilefilter();

        if (annotation != null) {
            dir = annotation.directory();
        }
        List<File> allFilesFrom = getAllFilesFrom(new File(dir), filter);
        List<TestResult> allScenarioResultHolder = new ArrayList<>();
        for (File file : allFilesFrom) {
            try {
                allScenarioResultHolder.add(getFromFile(file));
            } catch (FileNotFoundException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return allScenarioResultHolder;

    }

    protected abstract String getFilefilter();

    protected abstract TestResult getFailedResult(File file, Throwable e);

    protected abstract TestResult getFromFile(File file) throws FileNotFoundException;

    protected abstract String getName();

    private List<File> getAllFilesFrom(File file, String filter) {
        List<File> all = new ArrayList<>();
        File[] ts = file.listFiles(f -> f.getName().endsWith(filter) );
        if (ts != null) {

            Arrays.stream(ts).forEach(theFile -> {
                if (theFile.isDirectory()) {
                    all.addAll(getAllFilesFrom(theFile, filter));
                } else {
                    all.add(theFile);
                }
            });
        }
        return all;
    }

    public abstract TestResult run(List<ExecutionContext> ctxt, ExecutionReferenceBag references, ExecutionReferenceBag sharedState, RunNotifier notifier);

}

