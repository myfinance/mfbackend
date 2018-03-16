/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestScenarioTest.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.dsl;

import de.hf.dac.testrunner.datetime.DateTimePropertyConstructor;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestScenarioTest {

    @Test
    public void testLoadEmptySuite_ShouldReturnValidObject_() throws Exception {
        Yaml loader = new Yaml(new DateTimePropertyConstructor(TestScenario.class));
        Object load = loader.load(new InputStreamReader(this.getClass().getResourceAsStream("/testsuite_1.yaml")));
        assertTrue(load instanceof TestScenario);
    }

    @Test
    public void testLoadEmptySuite_ShouldContainValidParsedData_() throws Exception {
        Yaml loader = new Yaml(new DateTimePropertyConstructor(TestScenario.class));
        Object load = loader.load(new InputStreamReader(this.getClass().getResourceAsStream("/testsuite_1.yaml")));
        LocalDateTime parsed = LocalDateTime.parse("2017-08-24 14:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime createAt = ((TestScenario) load).getCreateAt();
        assertEquals(parsed, createAt);
        System.out.println(createAt);
    }

    @Test
    public void testLoadTestFileUsingStaticLoader_ShouldReturnvalidInstance() throws Exception {
        TestScenario load = TestScenario.load(this.getClass().getResourceAsStream("/testsuite_1.yaml"));
        assertNotNull(load);
        assertTrue(load instanceof TestScenario);
        assertEquals(1, load.getVersion().longValue());
    }

    @Test
    public void testLoadTestSuiteincludingCases_ShouldReturnvalidInstance() throws Exception {
        TestScenario load = TestScenario.load(this.getClass().getResourceAsStream("/testsuite_IncludingTestCases.yaml"));
        assertNotNull(load);
        assertTrue(load instanceof TestScenario);
        assertEquals(2, load.getTestCases().size());

        assertTrue(load.getTestCases().get(0) instanceof TestCase);

        assertEquals("hf", load.getAuthor());
        assertTrue(load.getDescription() != null && load.getDescription().contains("Beispiel"));
        assertEquals("Sample TestScenario", load.getName());
    }
}
