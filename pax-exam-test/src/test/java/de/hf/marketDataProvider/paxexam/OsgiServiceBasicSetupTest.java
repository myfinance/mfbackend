/** ----------------------------------------------------------------------------
 *
 * ---          DApplication Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : OsgiServiceBasicSetupTest.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 14.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.paxexam;

import de.hf.marketDataProvider.paxexam.support.PAXExamTestSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;

import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
public class OsgiServiceBasicSetupTest extends PAXExamTestSetup {

    @Configuration
    public Option[] config() {
        return new Option[] { super.configDefaults(), restFeatures() };
    }

    @Test
    public void testInjectAndBasicOsgiContainerSetupIsWorking() {
        assertNotNull(bundleContext);
    }
}
