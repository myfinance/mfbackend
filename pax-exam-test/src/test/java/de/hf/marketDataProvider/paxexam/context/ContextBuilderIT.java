/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ContextBuilderIT.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 26.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.paxexam.context;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import de.hf.dac.api.io.env.context.ApplicationContext;
import de.hf.dac.api.io.env.context.ContextBuilder;
import de.hf.marketDataProvider.paxexam.support.PAXExamTestSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;

import javax.inject.Inject;

@RunWith(PaxExam.class)
public class ContextBuilderIT extends PAXExamTestSetup {


    @Inject
    ContextBuilder ctxtBuilder;

    static String collector = "";

    @Override
    @Configuration
    public Option[] config() {
        return new Option[] { //
            super.configDefaults(), ioFeatures() };
    }

    @Test
    public void testConfigService() throws Exception {


        Assert.assertNotNull(ctxtBuilder);

        PostConstructTestee spy = new PostConstructTestee();
        // create guice context
        ApplicationContext someID = ctxtBuilder.build("SomeID", new Module[] { new AbstractModule() {
            @Override
            protected void configure() {
                bind(PostConstructTestee.class).toInstance(spy);
            }
        } });

        PostConstructTestee autowired = someID.autowire(PostConstructTestee.class);
        // make sure PostConstruct was called
        Assert.assertNotNull(spy.getResult());

    }

}



