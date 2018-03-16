/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ClassBasedPlugin.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.base;

import de.hf.dac.testrunner.execution.plugin.Action;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.execution.plugin.PluginAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class ClassBasedPlugin implements Plugin {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private transient Map<String, Constructor> actionProviders = new TreeMap<>();
    private Map<String, PluginAction> reusableActions = new TreeMap<>();

    private ArrayList<String> actionNames;

    public ClassBasedPlugin() throws NoSuchMethodException {
        findPluginClasses(getClass().getPackage().getName());
    }


    public void findPluginClasses(String basePackage) throws NoSuchMethodException {
        // use class path scanning to find plugin actions
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        // Filter to include only classes that have a particular annotation.
        provider.addIncludeFilter(new AnnotationTypeFilter(Action.class));
        // Find classes in the given package (or subpackages)
        Set<BeanDefinition> beans = provider.findCandidateComponents(basePackage);
        Class[] foundActionClasses = new Class[beans.size()];
        int i = 0;
        for (BeanDefinition bd : beans) {
            try {
                foundActionClasses[i++] = Class.forName(bd.getBeanClassName());
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        }
        extractPluginActions(foundActionClasses);
    }

    private void extractPluginActions(Class[] pluginActions) throws NoSuchMethodException {
        for (Class pluginAction : pluginActions) {
            if (pluginAction.isAnnotationPresent(Action.class)) {
                Action anno = (Action) pluginAction.getAnnotation(Action.class);
                // save class to instantiate new actions
                actionProviders.put(anno.name(), pluginAction.getConstructor(Plugin.class));
                // mark this as reusable
                if (anno.reuse()) {
                    reusableActions.put(anno.name(), null);
                }
            } else {
                // fallback
                log.warn("PluginAction missing @Action annotation. Can only use the FQN");
                actionProviders.put(pluginAction.getName(), pluginAction.getConstructor(Plugin.class));
            }
        }
        actionNames = new ArrayList<>(actionProviders.keySet());
    }

    @Override
    public List<String> getPluginActions() {
        return actionNames;
    }

    @Override
    public PluginAction getAction(String name) {
        if (reusableActions.containsKey(name)) {
            if (reusableActions.get(name) == null) {
                try {
                    reusableActions.put(name, (PluginAction) actionProviders.get(name).newInstance(this));
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            }
            return reusableActions.get(name);
        }
        if (actionProviders.containsKey(name)) {
            try {
                return (PluginAction) actionProviders.get(name).newInstance(this);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
        return null;
    }
}

