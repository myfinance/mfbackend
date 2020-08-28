/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : GuiceLifecycleListenerModule.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 26.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import de.hf.dac.api.base.exceptions.DACException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Taken from https://www.holisticon.de/2012/08/postconstruct-mit-guice/
 */
public class GuiceLifecycleListenerModule implements Module, TypeListener {

    @Override
    public void configure(Binder binder) {

        binder.bindListener(Matchers.any(), this);
    }

    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> encounter) {
        encounter.register((InjectionListener<I>) injectee -> {
            // execute all postconstruct methods (never null).
            for (final Method postConstructMethod : Arrays.asList(injectee.getClass().getMethods()).stream()
                .filter(GuiceLifecycleListenerModule::predicate).collect(Collectors.toList())) {
                try {
                    postConstructMethod.invoke(injectee);
                } catch (final Exception e) {
                    throw new DACException(String.format("@PostConstruct %s", postConstructMethod), e);
                }
            }
        });
    }

    private static boolean predicate(Method method) {
        boolean result = false;
        Annotation[] annotations = method.getDeclaredAnnotations();
        if(annotations!=null){
            for (Annotation annotation : annotations) {
                if(annotation.toString().equals("@javax.annotation.PostConstruct()")) result=true;
                /* does not match:
                if(annotation.annotationType().equals(PostConstruct.class)) result3=true;
                does not match:
                if(annotation.getClass()==PostConstruct.class) result4=true;
                 */
            }
        }
        /*isAnnotationPresent can not match PostConstruct.class I don't know why */
        //result = method.isAnnotationPresent(PostConstruct.class);
        return result;
    }
}

