/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestStepTest.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 13.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.dsl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestStepTest {

    @Test
    public void testLoadSingleStepFile_ShouldReturnValidInstance() throws Exception {
        TestStep load = TestStep.load(this.getClass().getResourceAsStream("/teststep_1.yaml"));
        assertNotNull(load);
        assertEquals("BerechneAlter", load.getAction());
        assertEquals("alter", load.getReference());
        assertEquals(123.0, load.getExamples().getParams().get("anyUnknownKeyAsParamNameForDouble"));
    }

}