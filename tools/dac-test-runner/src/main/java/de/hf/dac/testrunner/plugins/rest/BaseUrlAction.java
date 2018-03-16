/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseUrlAction.java
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
import de.hf.dac.testrunner.execution.TestExecutionException;
import de.hf.dac.testrunner.execution.plugin.ActionField;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.plugins.base.BaseRepeatablePluginAction;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.ProxySpecification;
import io.restassured.specification.RequestSpecification;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Base URL Action
 */
public abstract class BaseUrlAction extends BaseRepeatablePluginAction {

    static SSLContext sc;

    @ActionField(name = "baseUrl", description = "common part of url including protocol://host")
    String baseUrl;

    @ActionField(name = "basePort", description = "common pirt ")
    Integer basePort;

    @ActionField(name = "url", description = "additional url ", mandatory = true)
    protected String url;

    @ActionField(name = "user", description = "User for BASIC URL Authentication")
    String user;

    @ActionField(name = "password", description = "The Basic Authentication Password")
    String password;

    @ActionField(name = "proxyUsesHttps", description = "do we need to use https for proxy")
    boolean proxyUsesHttps = true;

    // PROXY SUPPORT
    @ActionField(name = "proxyHost", description = "Proxy Host")
    private String proxyHost;

    @ActionField(name = "proxyPort", description = "Proxy Port")
    private Integer proxyPort;

    @ActionField(name = "proxyUser", description = "Proxy User if authentication needed")
    private String proxyUser;

    @ActionField(name = "proxyPassword", description = "Proxy User Password")
    private String proxyPassword;

    @ActionField(name = "pathParams", description = "Parameters for Path")
    private Map<String, String> pathParams;

    @ActionField(name = "queryParams", description = "Query Params")
    private Map<String, String> queryParams;

    @ActionField(name = "formParams", description = "Form Multipart Params")
    private Map<String, String> formParams;

    @ActionField(name = "file", description = "path to file for upload")
    private String file;

    @ActionField(name = "fileVariable", description = "path to file for upload")
    private String fileVariable;

    public BaseUrlAction(Plugin plugin) {
        super(plugin);
    }

    public RequestSpecification init() {
        RequestSpecification g = initEmptyGiven();
        g = g.relaxedHTTPSValidation();
        g = applyProxyConfiguration(g);
        if (user != null && password != null) {
            g = g.auth().basic(user, password);
        }

        if (pathParams != null) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                g = g.pathParam(entry.getKey(), entry.getValue());
            }
        }
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                g = g.param(entry.getKey(), entry.getValue());
            }
        }
        if (formParams != null) {
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                g = g.formParam(entry.getKey(), entry.getValue());
            }
        }

        g = g.baseUri(baseUrl).port(basePort);
        g = g.log().all();

        return g;
    }

    private RequestSpecification applyProxyConfiguration(RequestSpecification g) {
        RequestSpecification result = g;
        if (proxyHost != null && proxyPort != null) {
            if (proxyUser != null && proxyPassword != null)
                if (proxyUsesHttps) {
                    result = result
                        .proxy(new ProxySpecification(proxyHost, proxyPort, "http").withAuth(proxyUser, proxyPassword));
                } else {
                    result = result.proxy(ProxySpecification.host(proxyHost).withPort(proxyPort).withAuth(proxyUser, proxyPassword));
                }
            else {
                result = result.proxy(proxyHost, proxyPort);
            }

        }
        return result;
    }

    protected Response getJSON(String url, Method method) {
        Response result = null;
        if (method == Method.GET) {
            result = init().when().get(url);
        } else if (method == Method.POST) {
            if (file != null && fileVariable == null) {
                return init().multiPart(new File(file)).when().post(url);
            } else if (file != null && fileVariable != null) {
                return init().multiPart(fileVariable, new File(file)).when().post(url);
            } else {
                result = init().when().post(url);
            }
        } else if (method == Method.PUT) {
            result = init().when().put(url);
        } else if (method == Method.DELETE) {
            result = init().when().delete(url);
        }
        if (result == null || result.getStatusCode() != 200) {
            throw new TestExecutionException("Rest URL Not finished with 200");
        }
        return result;
    }

    protected RequestSpecification initEmptyGiven() {
        return given();
    }

    @Override
    public void actionIsRepeated(int repeatCount, ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {
        log.info(this.getName() + " Repeated for {} times", repeatCount);
    }
}

