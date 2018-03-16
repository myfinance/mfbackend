/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : HtmlReportGenerator.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.report.impl;

import de.hf.dac.testrunner.dsl.TestCase;
import de.hf.dac.testrunner.dsl.TestData;
import de.hf.dac.testrunner.dsl.TestScenario;
import de.hf.dac.testrunner.dsl.TestStep;
import de.hf.dac.testrunner.execution.TestResult;
import de.hf.dac.testrunner.report.ReportGeneratorType;
import de.hf.dac.testrunner.report.ReportGeneratorTypes;
import de.hf.dac.testrunner.report.SelfDescribingReportGenerator;

import j2html.TagCreator;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.br;
import static j2html.TagCreator.div;
import static j2html.TagCreator.document;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.i;
import static j2html.TagCreator.link;
import static j2html.TagCreator.pre;
import static j2html.TagCreator.script;
import static j2html.TagCreator.span;
import static j2html.TagCreator.style;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.text;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.title;
import static j2html.TagCreator.tr;

@ReportGeneratorTypes(value = { @ReportGeneratorType(name = "HTML", description = "Generate Simple One Page HTML Result Page", configParams = {}) })
public class HtmlReportGenerator extends SelfDescribingReportGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlReportGenerator.class);
    public static final String COLLAPSE = "collapse";

    @Override
    public ByteArrayOutputStream generate(Class annotatedTestClass, List<TestResult<TestScenario>> scenarios, String type, Map<String, String> configuration) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {

            outputStream.write(document(html(head(title("Test Scenario Result"),
                link().withRel("stylesheet").withHref("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"),
                link().withRel("stylesheet").withHref("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css")

                , script().withSrc("https://code.jquery.com/jquery-1.12.4.min.js")
                    .attr("integrity", "sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=").attr("crossorigin", "anonymous"),
                script().withSrc("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js")
                    .attr("integrity", "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa").attr("crossorigin", "anonymous"),
                style(".ignored {color: burlywood;} .nok { color: red;} .ok {color: green;} .selectedRow {background: lightgrey;}")),
                body(div().withClass("container").with(each(scenarios, this::getScenarioOutput))))).getBytes());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        return outputStream;
    }

    private DomContent getScenarioOutput(TestResult<? extends TestScenario> testResult) {
        return div().withClass("row").with(div().withClass("col"), div().withClass("col-6").with(
            div(span(h2("Scenario:" + testResult.getTestInstance().getName())), resultIcon(testResult), br(),
                span().withClass(resultClass(testResult)).withText(testResult.getMessage()),
                getTextPreSpan(((TestResult<TestScenario>) testResult).getTestInstance().getDescription()),
                getSourceButton(testResult.getTestInstance().getLocation())).withClass("jumbotron ")), each(testResult.getChildren(), testCase -> div(
            h1().withClass(resultClass(testCase)).withText("TestCase: " + ((TestResult<TestCase>) testCase).getDisplayName()),
            toggleLink("collapse_" + testCase.getUid(), "Open"),
            div(resultIcon(testCase), br(), span().withClass(resultClass(testCase)).withText(testCase.getMessage()),
                getTextPreSpan(((TestResult<TestCase>) testCase).getTestInstance().getDescription()),
                getSourceButton(((TestResult<TestCase>) testCase).getTestInstance().getLocation()), getBackgroundTable(testCase),
                table(thead(tr(th("x"), th("Action"), th("Status"), th("Message"), th("Reference"), th("Parameter"), th(),th("ContextParams"))), tbody(
                    each(testCase.getChildren(), testStep -> tr(td(getIcon(testStep)),
                        td(a().withText(((TestResult<TestStep>) testStep).getTestInstance().getAction())
                            .withHref(((TestResult<TestStep>) testStep).getTestInstance().getLocation())), td(testStep.getResult().name()),
                        td(testStep.getMessage()), td(((TestResult<TestStep>) testStep).getTestInstance().getReference()), td(pre(text(
                            ((TestResult<TestStep>) testStep).getTestInstance().getParams().entrySet().stream()
                                .map(entry -> entry.getKey() + "=" + removePassword(entry.getKey(),entry.getValue())).collect(Collectors.joining("\n"))))), td(toggleLink("collapse_params_"+((TestResult<TestStep>) testStep).getUid(), "Show Context")),td(pre(text(
                            ((TestResult<TestStep>) testStep).getParams().entrySet().stream().map(entry -> entry.getKey() + "=" + removePassword(entry.getKey(), entry.getValue()))
                                .collect(Collectors.joining("\n")))).withId("collapse_params_"+((TestResult<TestStep>) testStep).getUid()).withClass(
                            COLLAPSE)))))).withClass("table"))
                .withId("collapse_" + ((TestResult<TestCase>) testCase).getUid()).withClass(COLLAPSE))));
    }

    private Tag getBackgroundTable(TestResult<?> testCase) {
        try {
            TestData data = ((TestResult<TestCase>) testCase).getTestInstance().getBackground();
            if (data != null && data.getTableRows() != 0) {
                final AtomicInteger row = new AtomicInteger(0);
                ContainerTag header = null;
                header = thead(tr(each(data.getDataTable().get(0).keySet(), TagCreator::th)));
                return table(header, tbody(each(data.getDataTable(), rowElements -> tr(each(rowElements.values(), value -> td("" + value)))
                    .withClass(getBackgroundTableRowClass(testCase, row.getAndIncrement()))))).withClass("table");
            } else {
                return span();
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            return span();
        }

    }

    private String getBackgroundTableRowClass(TestResult<?> testCase, int row) {

        if (testCase.getRepeatIndex() == row) {
            if (testCase.getResult() == TestResult.RESULT.OK) {
                return "selectedRow ok";
            }
            if (testCase.getResult() == TestResult.RESULT.OK) {
                return "selectedRow ignored";
            } else {
                return "selectedRow nok";
            }
        }
        return "";

    }

    public static Tag toggleLink(String toggleId, String linkText) {
        return a().withHref("#" + toggleId).withData("toggle", COLLAPSE).withText(linkText).withClass("btn btn-info");
    }

    public static Tag getSourceButton(String location) {
        return a().withText("Source").withHref("file://" + (location != null && location.startsWith("/") ? location : "/" + location))
            .withClass("btn btn-info");
    }

    public static Tag getIcon(TestResult testResult) {
        return span(i().withClass("glyphicon " + (testResult.getResult() == TestResult.RESULT.OK ?
            "glyphicon-check ok" :
            testResult.getResult() == TestResult.RESULT.IGNORED ? "glyphicon glyphicon-pause ignored" : "glyphicon-remove nok")), text(" "));
    }

    public static Tag resultIcon(TestResult testResult) {
        return span(i().withClass("glyphicon" + (testResult.getResult() == TestResult.RESULT.OK ?
                " glyphicon-check " :
                testResult.getResult() == TestResult.RESULT.IGNORED ? " glyphicon-pause " : " glyphicon-remove")), text(" "),
            text(testResult.getResult().name())).withClass(resultClass(testResult));
    }

    public static Tag getTextPreSpan(String text) {
        return div().with(pre().withClass("").withText(text));
    }

    public static String resultClass(TestResult result) {
        if (result.getResult() == TestResult.RESULT.NOK) {
            return " nok ";
        }
        if (result.getResult() == TestResult.RESULT.IGNORED) {
            return " ignored ";
        } else {
            return " ok ";
        }
    }

    public static Object removePassword(String key, Object value) {
        if (key.toUpperCase().contains("PASSWORD") || key.toUpperCase().contains("PWD")) {
            return "XXXXXXXXXXXXXX";
        }
        return value;
    }

}

