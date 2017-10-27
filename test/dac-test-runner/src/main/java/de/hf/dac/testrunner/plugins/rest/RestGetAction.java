/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RestGetAction.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.rest;

import de.hf.dac.testrunner.dsl.TestData;
import de.hf.dac.testrunner.dsl.TestStep;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionReferenceBag;
import de.hf.dac.testrunner.execution.plugin.Action;
import de.hf.dac.testrunner.execution.plugin.ActionField;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.execution.plugin.PluginActionFieldInjector;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

@Action(name = "GetRestUrl", description = "Calling RestUrl using RestAssured.")
public class RestGetAction extends BaseUrlAction {

    @ActionField(name = "referenceType", description = "Either JSON, XML or REPSONSE(Default). RESPONSE will be the RestAssured Response, JSON /XML be JsonPath or XMLPath instances")
    private String referenceType = "RESPONSE";

    @ActionField(name = "extractPath", description = "Path to be extracted from JSON/XML Result")
    private String extractPath;

    @ActionField(name="method", description="Http Method Type (GET, POST, PUT, DELETE,...")
    private String method = "GET";

    public RestGetAction(Plugin plugin) {
        super(plugin);
    }


    @Override
    public Object executeRepeated(ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {
        RestAssuredExecutionContext raCtxt = (RestAssuredExecutionContext) ctxt;
        try {
            PluginActionFieldInjector.inject(this, raCtxt.getProperties(), raCtxt.getReferenceBag(), false);
            this.injectParameters(stepInformation, (ExecutionReferenceBag) ctxt.getReferenceBag());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        Response json = getJSON(url, Method.valueOf(method));
        if ("JSON".equalsIgnoreCase(referenceType)) {
            JsonPath path = JsonPath.from(json.asString());
            if (extractPath != null) {
                return path.get(extractPath);
            } else {
                return path;
            }
        } else if ("XML".equalsIgnoreCase(referenceType)) {
            XmlPath from = XmlPath.from(json.asString());
            if (extractPath != null) {
                return from.get(extractPath);
            } else {
                return from;
            }
        } else
            return json;
    }
}

