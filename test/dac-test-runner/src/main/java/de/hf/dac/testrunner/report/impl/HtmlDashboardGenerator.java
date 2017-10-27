/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : HtmlDashboardGenerator.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.report.impl;

import de.hf.dac.testrunner.dsl.TestScenario;
import de.hf.dac.testrunner.execution.TestResult;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.junit.AllScenarioRunner;
import de.hf.dac.testrunner.report.ReportGeneratorType;
import de.hf.dac.testrunner.report.ReportGeneratorTypes;
import de.hf.dac.testrunner.report.SelfDescribingReportGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import j2html.tags.DomContent;
import j2html.tags.Tag;
import static j2html.TagCreator.*;

@ReportGeneratorTypes(value = {
    @ReportGeneratorType(name = "HTML", description = "Generate Simple One Page HTML Result Page", configParams = {})})
public class HtmlDashboardGenerator extends SelfDescribingReportGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlDashboardGenerator.class);

    public static final String COL_LG_4 = "col-lg-4";
    public static final String CENTERED = "centered";

    @Override
    public ByteArrayOutputStream generate(Class annotatedTestClass, List<TestResult<TestScenario>> scenarios, String type, Map<String, String> configuration) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HashMap<String, String> resultFiles;
        try {

            File parent = new File( getConfigParam(configuration, "outputDir","./target/ReportDashboards/"+ UUID.randomUUID()));
            parent.mkdirs();
            String indexFileName = getConfigParam(configuration,"fileName","index_org.html");
            String title = getConfigParam(configuration, "title", "Test Results");

            // create one HTML file per Scenario
            HtmlReportGenerator scenarioDetailGenerator = new HtmlReportGenerator();
            resultFiles = new HashMap<>();

            scenarios.forEach(scenario -> {
                File file = new File(parent, "Report_" + scenario.getTestInstance().getName()+".html");
                resultFiles.put(scenario.getTestInstance().getName(), file.getName());
                writeScenarioDetailReport(annotatedTestClass, configuration, scenarioDetailGenerator, scenario, file);
            });

            String apiDocs = generateAPIDocs(annotatedTestClass, parent.getAbsolutePath());

            String styleCentered = "text-align: center; width: 100%; height: 100%;";
            outputStream.write(document(
                html(
                    head(
                        title("Test Scenario Result"),
                        link().withRel("stylesheet").withHref("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"),
                        link().withRel("stylesheet").withHref("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"),
                        script().withSrc("https://code.jquery.com/jquery-1.12.4.min.js").attr("integrity", "sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=").attr("crossorigin", "anonymous"),
                        script().withSrc("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js").attr("integrity", "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa").attr("crossorigin", "anonymous"),
                        script().withSrc("https://www.gstatic.com/charts/loader.js"),
                        style(getResourceFileAsString("/inlineStylesDashboard.css"))),
                    body(
                        div().withClass("container").with(
                            div().withClass("jumbotron").withStyle("text-align: center;").with(h1("TestResults"),h3(title),span().withText(new Date().toString()),
                                br(),getLinkButton("API Documentation", apiDocs)),
                            div().withClass("row").withId("charting").with(

                                div().withClass(COL_LG_4).with(
                                    div().withClass(CENTERED).withText("Scenarios"),
                                    div().withId("donut1").withStyle(styleCentered)
                                ),
                                div().withClass(COL_LG_4).with(
                                    div().withClass(CENTERED).withText("Cases"),
                                    div().withId("donut2").withStyle(styleCentered)
                                ),
                                div().withClass(COL_LG_4).with(
                                    div().withClass(CENTERED).withText("Steps"),
                                    div().withId("donut3").withStyle(styleCentered)
                                )
                            ),
                            hr().withClass("style-four"),
                            each(getRows(scenarios), rowlist -> getScenarioRow(rowlist, resultFiles))
                        ),script().with(rawHtml(getDonutChartScript(scenarios)))
                    )
                )).getBytes());
            File dashboardFile = new File(parent, indexFileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(dashboardFile)) {
                fileOutputStream.write(outputStream.toByteArray());
                LOG.error("Wrote Dashboard to file:///"+dashboardFile.getCanonicalPath());
            }

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        return outputStream;
    }

    private String generateAPIDocs(Class testClass, String resultDir) {
        AllScenarioRunner.TestPlugins plugannotations = (AllScenarioRunner.TestPlugins) testClass.getAnnotation(AllScenarioRunner.TestPlugins.class);
        try {
            String filename = testClass.getName()+ "_PluginAPI.html";
            File file = new File(resultDir, filename);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(new ActionDocumentationReport()
                    .generate((plugannotations != null) ? getPLugins(testClass, plugannotations.value()) : Collections.emptyList()).toByteArray());
            }
            return file.getName();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    private List<Plugin> getPLugins(Class testClass, Class[] value) {
        List<Plugin> all = new ArrayList<>();
        for (Class aClass : value) {

            try {
                try {
                    Constructor<? extends Plugin> constructor = aClass.getConstructor(Class.class);
                    if (constructor != null) {
                        all.add(constructor.newInstance(testClass));
                    }
                } catch (Exception ex) {
                    all.add((Plugin) aClass.newInstance());
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

        }
        return all;
    }

    private void writeScenarioDetailReport(Class annotatedTestClass, Map<String, String> configuration, HtmlReportGenerator scenarioDetailGenerator,
        TestResult<TestScenario> scenario, File file) {
        try (FileOutputStream fout = new FileOutputStream(file)) {
            fout.write(scenarioDetailGenerator.generate(annotatedTestClass, Arrays.asList(scenario), "HTML", configuration).toByteArray());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String getConfigParam(Map<String,String> config, String name, String def) {
        if (config!= null && config.containsKey(name)) {
            return config.get(name);
        }
        return def;
    }

    public DomContent getScenarioRow(List<TestResult<TestScenario>> list, Map<String, String> resultFiles) {
        return div().withClass("row scenariorow row-eq-height").with(each(list, el -> getScenarioBox(el, resultFiles)));
    }

    public DomContent getScenarioBox(TestResult<TestScenario> scenarioResult,Map<String, String> resultFiles) {
        return div().withClass("col-lg-4 panel scenariobox "+getPanelHeadingStyle(scenarioResult)).with(div().withClass("panel-heading").withText(scenarioResult.getTestInstance().getName()),
            p().with(text(scenarioResult.getTestInstance().getDescription()), br(), getSourceButton(scenarioResult.getTestInstance().getName(),resultFiles)),getProgressBar(scenarioResult)
        );
    }

    private String getPanelHeadingStyle(TestResult<TestScenario> scenarioResult) {
        if (scenarioResult.getResult() == TestResult.RESULT.OK) {
            return "panel-success";
        } else if (scenarioResult.getResult() == TestResult.RESULT.NOK) {
            return "panel-danger";
        } else if (scenarioResult.getResult() == TestResult.RESULT.IGNORED) {
            return "panel-warning";
        }
        return "panel-info";
    }

    public static Tag getSourceButton(String location, Map<String, String> resultFiles) {
        return getLinkButton("Details",resultFiles.get(location));
    }

    public static Tag getLinkButton(String text, String location) {
        return a().withText(text).withHref(location).withClass("btn btn-info");
    }

    public DomContent getProgressBar(TestResult result) {
        double countFailed = 0;
        for (Object o : result.getChildren()) {
            countFailed += ((TestResult) o).getResult() == TestResult.RESULT.NOK ? 1 : ((TestResult) o).getResult() == TestResult.RESULT.IGNORED ? 1: 0;
        }
        double total = result.getChildren().size();
        int percent = (int)(result.getResult() == TestResult.RESULT.OK ? 100 : ((total -countFailed) / total)*100);
        String progressColorStyle = result.getResult() == TestResult.RESULT.IGNORED ? "ignored" :"okprogress";
        return div().withClass("progress nokprogress").with(
            div().withClass("progress-bar "+progressColorStyle).withRole("progressbar").withStyle("width: " + percent + "%;").attr("aria-valuenow", Integer.toString(60))
                .attr("aria-valuemin", "0").attr("aria-valuemax", "100").withText(Integer.toString(percent) + "%"));
    }

    public List<List<TestResult<TestScenario>>> getRows(List<TestResult<TestScenario>> scenarios) {
        List<List<TestResult<TestScenario>>> result = new ArrayList<>();
        ArrayList<TestResult<TestScenario>> e = new ArrayList<>();
        for (int i = 0; i < scenarios.size(); i++) {
            if (i % 3 == 0) {
                e = new ArrayList<>();
                result.add(e);
            }
            e.add(scenarios.get(i));
        }
        return result;
    }




    @SuppressWarnings("squid:S3776")
    public String getDonutChartScript(List<TestResult<TestScenario>> scenarios) throws IOException {
        StringBuilder buff = new StringBuilder("var allCharts = [");

        AtomicInteger totalScenarios = new AtomicInteger(0);
        AtomicInteger failedScenarios = new AtomicInteger(0);
        AtomicInteger ignoredScenarios = new AtomicInteger(0);
        AtomicInteger totalCases = new AtomicInteger(0);
        AtomicInteger failedCases = new AtomicInteger(0);
        AtomicInteger ignoredCases = new AtomicInteger(0);
        AtomicInteger totalSteps = new AtomicInteger(0);
        AtomicInteger failedSteps = new AtomicInteger(0);
        AtomicInteger ignoredSteps = new AtomicInteger(0);

        // count all
        scenarios.forEach(scenario -> {
            applyCounts(totalScenarios, failedScenarios, ignoredScenarios, scenario);
            scenario.getChildren().forEach(testCase -> {
                applyCounts(totalCases, failedCases, ignoredCases, testCase);
                testCase.getChildren().forEach(step -> applyCounts(totalSteps, failedSteps, ignoredSteps, step));
            });
        });

        String ignored = ", ignored: ";
        String sucess = ", success: ";
        buff.append("{id: 'donut1', labelName: \"Scenarios\", failed: ").append(failedScenarios.get()).append(ignored + ignoredScenarios.get()).append(
            sucess + (totalScenarios.get()-ignoredScenarios.get()-failedScenarios.get())).append("},");
        buff.append("{id: 'donut2', labelName: \"Cases\", failed: ").append(failedCases.get()).append(ignored + ignoredCases.get()).append(
            sucess + (totalCases.get()-ignoredCases.get()-failedCases.get())).append("},");
        buff.append("{id: 'donut3', labelName: \"Steps\", failed: ").append(failedSteps.get()).append(ignored + ignoredSteps.get()).append(
            sucess + (totalSteps.get()-ignoredSteps.get()-failedSteps.get())).append("}");
        buff.append("];");

        buff.append(readFileFromStream(this.getClass().getResourceAsStream("/googleChart.js")));

        return buff.toString();
    }

    protected void applyCounts(AtomicInteger totalScenarios, AtomicInteger failedScenarios, AtomicInteger ignoredScenarios,
        TestResult scenario) {
        totalScenarios.incrementAndGet();
        if (scenario.getResult() == TestResult.RESULT.NOK) {
            failedScenarios.incrementAndGet();
        }
        if (scenario.getResult() == TestResult.RESULT.IGNORED) {
            ignoredScenarios.incrementAndGet();
        }
    }

}

