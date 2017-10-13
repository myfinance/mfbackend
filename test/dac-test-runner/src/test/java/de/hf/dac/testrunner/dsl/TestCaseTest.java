/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestCaseTest.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.dsl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCaseTest {
    @Test
    public void testLoadSingleCaseFromsingleFiel_ShouldReturnValidInstance() throws Exception {
        TestCase.load(this.getClass().getResourceAsStream("/testcase_1.yaml"));
    }

    @Test
    public void testLoadSingleCaseFromsingleFiel_ShouldPrintTable() throws Exception {
        TestCase load = TestCase.load(this.getClass().getResourceAsStream("/testcase_1.yaml"));
        assertTrue(load.getName().contains("Test"));
        assertTrue(load.getDescription().contains("Beschreibung"));
        assertEquals(1, load.getSteps().size());
        assertTrue(load.getSteps().get(0) instanceof TestStep);
        assertEquals(2, load.getBackground().getDataTable().size());
    }

}