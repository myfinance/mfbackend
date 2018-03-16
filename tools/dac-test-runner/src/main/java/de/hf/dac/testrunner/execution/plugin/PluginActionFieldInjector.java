/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : PluginActionFieldInjector.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution.plugin;

import de.hf.dac.testrunner.execution.ReferenceBag;
import de.hf.dac.testrunner.execution.TestExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PluginActionFieldInjector {

    private static final Logger LOG = LoggerFactory.getLogger(PluginActionFieldInjector.class);

    private PluginActionFieldInjector() {
    }

    public static void inject(Object action, Map<String, Object> params, ReferenceBag bag) {
        inject(action, params, bag, true);
    }

    public static void inject(Object action, Map<String, Object> params, ReferenceBag bag, boolean failOnMandatory) {
        if (params == null) {
            return;
        }
        getAllFields((PluginAction) action).stream().forEach(declaredField -> injectField(declaredField, action, params, bag, failOnMandatory));
    }

    @SuppressWarnings("squid:S134")
    protected static void injectField(Field declaredField, Object action, Map<String, Object> params, ReferenceBag bag, boolean failOnMandatory) {
        try {
            if (declaredField.isAnnotationPresent(ActionField.class)) {
                ActionField anno = declaredField.getAnnotation(ActionField.class);
                if (params.containsKey(anno.name())) {
                    declaredField.setAccessible(true);

                    Object paramValue = params.get(anno.name());
                    //if param ist given as <> reference into bag try to resolve from there
                    // override if needed
                    if (isReplaceableString(paramValue)) {
                        handleStringParam(action, bag, declaredField, paramValue);
                    } else if (paramValue instanceof Map) {
                        declaredField.set(action, handleMapParam(bag, (Map) paramValue));
                    } else if (paramValue instanceof List) {
                        declaredField.set(action, handleListParam(bag, (List) paramValue));
                    } else {
                        declaredField.set(action, getObjectFromString(paramValue, declaredField.getType()));
                    }
                } else if (anno.mandatory() && failOnMandatory) {
                    throw new TestExecutionException(String.format("Mandatory field %s not set for %s", anno.name(), action.getClass().getName()));
                }
            }
        } catch (TestExecutionException e) {
            throw e;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new TestExecutionException(e.getMessage());
        }
    }

    public static Object injectParameter(String parameterName, Class paramType, Map<String, Object> params, ReferenceBag bag,
        boolean failOnMandatory) {
        try {
            if (params.containsKey(parameterName)) {

                Object paramValue = params.get(parameterName);
                //if param ist given as <> reference into bag try to resolve from there
                // override if needed
                if (isReplaceableString(paramValue)) {
                    return getObjectFromString(getFromBag(paramValue, bag), paramType);
                } else if (paramValue instanceof Map) {
                    return handleMapParam(bag, (Map) paramValue);
                } else if (paramValue instanceof List) {
                    return handleListParam(bag, (List) paramValue);
                } else {
                    return getObjectFromString(paramValue, paramType);
                }
            }
            // nothing to be injected
            return null;
        } catch (TestExecutionException e) {
            throw e;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new TestExecutionException(e.getMessage());
        }
    }

    protected static void handleStringParam(Object action, ReferenceBag bag, Field declaredField, Object paramValue) throws IllegalAccessException {
        declaredField.set(action, getObjectFromString(getFromBag(paramValue, bag), declaredField.getType()));
    }

    protected static Map handleMapParam(ReferenceBag bag, Map paramValue) throws IllegalAccessException {
        Map clone = new HashMap();
        clone.putAll(paramValue);
        paramValue.forEach((key, value) -> {
            if (isReplaceableString(value)) {
                clone.put(key, getFromBag(value, bag));
            }
        });
        return clone;
    }

    protected static List handleListParam(ReferenceBag bag, List paramValue) throws IllegalAccessException {
        List clone = new ArrayList();
        paramValue.forEach(e -> {
            if (isReplaceableString(e)) {
                clone.add(getFromBag(e, bag));
            }
        });
        return clone;
    }

    public static Object getFromBag(Object paramValue, ReferenceBag bag) {
        String o = ((String) paramValue).replaceAll("<", "").replaceAll(">", "");
        if (bag.getKeys().contains(o)) {
            return bag.get(o);
        } else {
            return paramValue;
        }
    }

    public static boolean isReplaceableString(Object paramValue) {
        return paramValue instanceof String && ((String) paramValue).startsWith("<") && ((String) paramValue).endsWith(">");
    }

    public static Object getObjectFromString(Object value, Class type) {
        if (value instanceof String && type.isAssignableFrom(Integer.class)) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException nfe) {
                LOG.warn(nfe.getMessage(), nfe);
            }
        } else if (value instanceof String && type.isAssignableFrom(LocalDate.class)) {
            // eventually parse a number of formats
            return LocalDate.parse((String) value, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (value instanceof Integer && type.isAssignableFrom(LocalDate.class)) {
            // eventually parse a number of formats
            return LocalDate.parse(Integer.toString((Integer) value), DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        return value;
    }

    public static List<Field> getAllFields(PluginAction action) {
        return getAllFields(action, ActionField.class);
    }

    public static List<Field> getAllFields(Object action, Class annotationClazz) {
        Class c = action.getClass();
        List<Field> all = new ArrayList<>();
        while (c.getSuperclass() != null) {
            List<Field> list = new ArrayList<>();
            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(annotationClazz)) {
                    list.add(f);
                }
            }
            all.addAll(list);
            c = c.getSuperclass();
        }
        return all;
    }

}

