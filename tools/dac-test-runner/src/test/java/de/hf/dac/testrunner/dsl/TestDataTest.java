/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestDataTest.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.dsl;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TestDataTest {

    @Test
    public void test_getTable_ShouldParseTableString() throws Exception {
        TestData testData = new TestData();
        testData.setTable("|key|anotherKey|Geburtstag |\n    | value1   | value2       | 14.10.1972 |\t\n");
        testData.getDataTable();
    }

    @Test
    public void testLoadTestDataFromString_ShouldTreatStringAsTableString() throws Exception {
        TestData testData = new TestData("|key|anotherKey|Geburtstag |\n    | value1   | value2       | 14.10.1972 |\t\n");
        List<Map<String, Object>> dataTable = testData.getDataTable();
        assertTrue(dataTable.size() > 0);
    }

    @Test
    public void testLoadTestDataFromFile_NotParsingTableTypes() throws Exception {
        TestCase data = TestCase.load(getClass().getResourceAsStream("/testcase_testdata_nonparsing.yaml"));
        assertEquals(2,data.getBackground().getTableRows());
        data.getBackground().getDataTable().forEach(a -> a.forEach((key, value) -> assertTrue(value instanceof String)));
    }

    @Test
    public void testLoadTestDataFromFile_ParsingTableTypes() throws Exception {
        TestCase data = TestCase.load(getClass().getResourceAsStream("/testcase_testdata_nonparsing.yaml"));
        data.getBackground().setParseValuesToTypes(true);
        assertEquals(2,data.getBackground().getTableRows());
        assertTrue(data.getBackground().getDataTable().get(0).get("Geburtsdatum") instanceof Integer);
        assertTrue(data.getBackground().getDataTable().get(0).get("Familienstand") instanceof String);
    }


    @Test
    public void testLoadFromFileSimgletestData_ShouldParseParamsIncludingProperTypeConversionIfPossible_() throws Exception {
        TestData testData = TestData.load(this.getClass().getResourceAsStream("/testcase_data_1.yaml"));
        assertEquals(4,testData.getParams().size());
        assertEquals("Ein normaler String", testData.getParams().get("key1"));
        assertEquals("Ein gequoteter String", testData.getParams().get("key2"));
        assertEquals(123.10, testData.getParams().get("key3"));
        // next one should be parsed as int and not as double
        assertNotEquals(12.0, testData.getParams().get("key4"));
        assertEquals(12, testData.getParams().get("key4"));
        assertTrue(testData.getTable().length() != 0);
    }

    @Test(expected = Exception.class)
    public void testLoadFromString_TablecontainsIncosnistentData_ShouldThrowException() throws Exception {
        TestData data = new TestData();
        data.setTable("|header1|\n|wert1|unnoetiger wert|");
        data.getDataTable();
    }

    @Test(expected = Exception.class)
    public void testLoadFromString_TableNotProperlyFormated_ShouldThrowException() throws Exception {
        TestData data = new TestData();
        data.setTable("|header1|\n|wert1");
        data.getDataTable();
    }

    @Test(expected = Exception.class)
    public void testLoadFromString_TableOnlyHeader_ShouldThrowException() throws Exception {
        TestData data = new TestData();
        data.setTable("|header1|");
        data.getDataTable();
    }

    @Test
    public void testLoadFromString_noTableatAll() throws Exception {
        TestData data = new TestData();
        assertEquals(0, data.getDataTable().size());
    }
}
