/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : GroovyAssert.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.common;

import de.hf.dac.testrunner.dsl.TestData;
import de.hf.dac.testrunner.dsl.TestStep;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.TestExecutionException;
import de.hf.dac.testrunner.execution.plugin.Action;
import de.hf.dac.testrunner.execution.plugin.ActionField;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@Action(name = "Assert", description = "Evaluate condition as Groovy Scripts. Line by line. Returns true if all evaluate to true.")
public class GroovyAssert extends AssertAction {

    private static final Logger LOG = LoggerFactory.getLogger(GroovyAssert.class);

    @ActionField(name = "isBoolean", description = "If set to false, this returns the groovy script result as is")
    private boolean isBoolean = true;

    public GroovyAssert(Plugin plugin) {
        super(plugin);
    }

    @Override
    public Object execute(ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {

        String[] lines = condition.split("\n");
        boolean fail = false;
        StringBuilder message = new StringBuilder();
        Object result = null;
        for (String line : lines) {
            try {
                result = executeLineAsScriptAssertion(line, ctxt, stepInformation.getAction(), backgroundData, "NONE");
                if (isBoolean && result != null && result instanceof Boolean && !(Boolean) result) {
                    fail = !fail ? !(Boolean) result : fail;
                    message.append(line).append("\t = False").append("\n");
                }
            } catch (Exception th) {
                fail = true;
                LOG.error(th.getMessage(), th);
            }
        }
        if (fail) {
            throw new TestExecutionException(message.toString());
        }
        setLastMessage("" + result);
        return isBoolean ? fail : result;
    }

    public static Object executeLineAsScriptAssertion(String line, ExecutionContext ctxt, String displayName, TestData backgroundData,
        Object current) {
        if (displayName != null) {
            LOG.debug("Eval from {}", displayName);
        }

        if (backgroundData != null) {
            // will be used somewhere
            LOG.debug("Using BackgroundDate {}", backgroundData);
        }

        Binding binding = new Binding();
        Set<String> keys = ctxt.getReferenceBag().getKeys();
        for (String key : keys) {
            binding.setVariable(key, ctxt.getReferenceBag().get(key));
        }
        binding.setVariable("current", current);

        GroovyShell shell = new GroovyShell(binding);

        return shell.evaluate(line);
    }

}

