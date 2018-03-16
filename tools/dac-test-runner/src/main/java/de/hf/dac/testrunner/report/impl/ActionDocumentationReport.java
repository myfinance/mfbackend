/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ActionDocumentationReport.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.report.impl;

import de.hf.dac.testrunner.execution.plugin.Action;
import de.hf.dac.testrunner.execution.plugin.ActionField;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.execution.plugin.PluginAction;
import de.hf.dac.testrunner.execution.plugin.PluginActionFieldInjector;
import de.hf.dac.testrunner.execution.plugin.TestPlugin;
import de.hf.dac.testrunner.report.ReportGeneratorBase;
import j2html.tags.Tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static j2html.TagCreator.b;
import static j2html.TagCreator.body;
import static j2html.TagCreator.br;
import static j2html.TagCreator.div;
import static j2html.TagCreator.document;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.link;
import static j2html.TagCreator.p;
import static j2html.TagCreator.script;
import static j2html.TagCreator.span;
import static j2html.TagCreator.style;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.title;
import static j2html.TagCreator.tr;

/*
 * Generate Documentation of all Actions provided
 */
public class ActionDocumentationReport extends ReportGeneratorBase {

    public ByteArrayOutputStream generate(List<Plugin> plugins) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(document(html(head(title("Plugin Documentation"),
            link().withRel("stylesheet").withHref("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"),
            link().withRel("stylesheet").withHref("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"),
            script().withSrc("https://code.jquery.com/jquery-1.12.4.min.js").attr("integrity", "sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=")
                .attr("crossorigin", "anonymous"), script().withSrc("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js")
                .attr("integrity", "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa").attr("crossorigin", "anonymous"),
            script().withSrc("https://www.gstatic.com/charts/loader.js"), style(getResourceFileAsString("/inlineStylesDashboard.css"))), body(
            div().withClass("container").with(div().withClass("jumbotron").withStyle("text-align: center;")
                    .with(h1("Plugin / Action Documentation"), span().withText(new Date().toString())),
                // for each plugin one Box
                each(plugins, this::getPluginDocumentation)

            )))).getBytes());
        return outputStream;
    }

    private Tag getPluginDocumentation(Plugin plugn) {
        String description = "No @TestPlugin Annotation present";

        if (plugn.getClass().isAnnotationPresent(TestPlugin.class)) {
            description = plugn.getClass().getAnnotation(TestPlugin.class).description();
        }

        return div().withClass("col panel panel-info scenariobox")
            .with(div().withClass("panel-heading").with(h1(plugn.getName())), p().withText(description),
                each(plugn.getPluginActions(), actionName -> getActionInfo(plugn, actionName)));
    }

    private Tag getActionInfo(Plugin plugn, String actionName) {
        PluginAction action = plugn.getAction(actionName);
        if (!action.getClass().isAnnotationPresent(Action.class)) {
            return div().withClass("actionBox");
        }
        return div().withClass("actionBox").with(h2(b(actionName)), br(), p(action.getClass().getAnnotation(Action.class).description()),
            table(thead(th("Parameter"), th("Description"), th("Mandatory"), th("Java Type")),
                tbody(each(PluginActionFieldInjector.getAllFields(action), field -> {
                    ActionField desc = field.getAnnotation(ActionField.class);
                    return tr(td(desc.name()), td(desc.description()), td(desc.mandatory() ? "TRUE" : "FALSE"), td(field.getType().getSimpleName()));
                }))).withClass("table table-striped"));
    }

}

