/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestMethodPlugin.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.junit.plugin;

import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.plugin.DefaultExecutionContext;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.execution.plugin.PluginAction;
import de.hf.dac.testrunner.execution.plugin.TestPlugin;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Using Methods of TestClass as Action Implementation
 */
@TestPlugin(description = "Usage of TestMethods for actions")
public class TestMethodPlugin implements Plugin {

    private Class testclass;

    private Map<String, MethodAction> actions = new LinkedHashMap<>();

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
        String value();
    }

    public TestMethodPlugin(Class testclass) throws Exception {
        this.testclass = testclass;

        // find actionMethods annotated with @Test ?? Semantic problem ?
        getAllAnnotatedMethods(testclass, Test.class).forEach(method -> {
            actions.put(method.getName(), new MethodAction(method, testclass, this));
        });

    }

    @Override
    public ExecutionContext provideContext(String env) {
        return new DefaultExecutionContext(env, this);
    }

    @Override
    public List<String> getPluginActions() {
        return Arrays.asList(actions.keySet().toArray(new String[0]));
    }

    @Override
    public PluginAction getAction(String name) {
        return actions.get(name);
    }

    @Override
    public String getName() {
        return "TestMethodPlugin";
    }


    public static List<Method> getAllAnnotatedMethods(Class c, Class annotation) {
        List<Method> all = new ArrayList<>();
        while (c.getSuperclass() != null) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    all.add(method);
                }
            }
            c = c.getSuperclass();
        }
        return all;
    }
}
