/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MethodAction.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.junit.plugin;

import de.hf.dac.testrunner.dsl.TestData;
import de.hf.dac.testrunner.dsl.TestStep;
import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ExecutionReferenceBag;
import de.hf.dac.testrunner.execution.TestExecutionException;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.execution.plugin.PluginActionFieldInjector;
import de.hf.dac.testrunner.plugins.base.BaseRepeatablePluginAction;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodAction extends BaseRepeatablePluginAction {

    private final Method delegate;
    private final Class testclass;
    private Object[] args;
    private Object theInstance;
    private Method actionMaxRepeatExceeded;
    private Method actionTimedOut;
    private Method actionIsRepeated;

    public MethodAction(Method delegate, Class testclass, Plugin plugin) {
        super(plugin);
        this.delegate = delegate;
        this.testclass = testclass;

        // create and inject instance
        createInstance();
    }

    private void createInstance() {
        try {
            this.theInstance = testclass.newInstance();
            for (Method method : testclass.getMethods()) {
                if (method.getName().equals("actionMaxRepeatExceeded")) {
                    this.actionMaxRepeatExceeded = method;
                }
                if (method.getName().equals("actionTimedOut")) {
                    this.actionTimedOut = method;
                }
                if (method.getName().equals("actionIsRepeated")) {
                    this.actionIsRepeated = method;
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void actionMaxRepeatExceeded(int repeatCount, ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {
        if (this.actionMaxRepeatExceeded != null) {
            try {
                this.actionMaxRepeatExceeded.invoke(theInstance, repeatCount, ctxt, stepInformation, backgroundData);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void actionTimedOut(int repeatCount, ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {
        if (this.actionTimedOut != null) {
            try {
                this.actionTimedOut.invoke(theInstance, repeatCount, ctxt, stepInformation, backgroundData);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void actionIsRepeated(int repeatCount, ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {
        if (this.actionIsRepeated != null) {
            try {
                this.actionIsRepeated.invoke(theInstance, repeatCount, ctxt, stepInformation, backgroundData);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public Object executeRepeated(ExecutionContext ctxt, TestStep stepInformation, TestData backgroundData) {
        try {
            Object[] fieldsToInject = new Object[] { ctxt, stepInformation, backgroundData };
            // inject ctxt, stepInformation and backgroundData if available
            PluginActionFieldInjector.getAllFields(theInstance, Inject.class).forEach(field -> {
                for (Object o : fieldsToInject) {
                    if (o != null && field.getType().isAssignableFrom(o.getClass())) {
                        field.setAccessible(true);
                        try {
                            field.set(theInstance, o);
                        } catch (IllegalAccessException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            });
            return delegate.invoke(theInstance, args);
        } catch (InvocationTargetException e) {
            throw new TestExecutionException(e.getTargetException().getMessage(), e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new TestExecutionException(e.getMessage(), e);
        }
    }

    @Override
    public void injectParameters(TestStep step, ExecutionReferenceBag references) {
        PluginActionFieldInjector.inject(this, step.getParams(), references);
        // injection only if all params are @Name annotated
        Annotation[][] parameterAnnotations = this.delegate.getParameterAnnotations();

        Class<?>[] parameterTypes = delegate.getParameterTypes();
        this.args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            if (parameterAnnotation.length != 1 || !(parameterAnnotation[0] instanceof TestMethodPlugin.Name)) {
                throw new TestExecutionException("Every Parameter needs to annotated with @TestMethodPlugin.Name");
            }

            String paramName = ((TestMethodPlugin.Name) parameterAnnotation[0]).value();
            args[i] = PluginActionFieldInjector.injectParameter(paramName, parameterTypes[i], step.getParams(), references, false);
        }
    }
}

