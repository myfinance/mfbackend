/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestData.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.dsl;

import de.hf.dac.testrunner.datetime.DateTimePropertyConstructor;
import de.hf.dac.testrunner.execution.TestExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple way of table test data
 */
public class TestData implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TestData.class);

    private String table;
    /**
     * Try to parse all Table Elements using Yaml
     * If set to false, all are taken as Strings
     */
    private boolean parseValuesToTypes = true;

    /**
     * Extra Param to logically group backupData table by columns
     */
    private String[] idCols;

    private transient Map<String, Object> params;

    public TestData(String value) {
        this.table = value;
    }

    public TestData() {
    }

    public String getTable() {
        return table;
    }

    public void setTable(String data) {
        this.table = data;
    }

    // Support Methods
    @SuppressWarnings("all")
    public List<Map<String, Object>> getDataTable() {
        List<Map<String, Object>> result = new ArrayList<>();
        if (table == null) {
            return result;
        }
        String[] lines = this.table.split("\n");
        if (lines.length < 2) {
            throw new TestExecutionException("Data Table needs at least a header row and one data row");
        }
        String[] keys = null;
        Yaml yaml = new Yaml(new Constructor());
        for (int i = 0; i < lines.length; i++) {

            // strip trailing and ending whitespaces
            String line = lines[i].trim();
            if (!(line.startsWith("|") && line.endsWith("|"))) {
                throw new TestExecutionException("each row needs to be surrounded by |");
            }
            line = line.substring(1, line.length() - 1);

            if (i == 0) {
                // header
                keys = line.split("\\|");
                if (idCols == null) {
                    idCols = new String[]{keys[0].trim()};
                }

            } else {
                StringBuilder buff = new StringBuilder();
                String[] values = line.split("\\|");

                if (keys == null || keys.length != values.length) {
                    throw new TestExecutionException("Row " + i + "does not contain the correct number of columns");
                }
                for (int a = 0; a < keys.length; a++) {

                    String quotedValue = values[a].trim();
                    if (!parseValuesToTypes && !quotedValue.startsWith("\"") && !quotedValue.endsWith("\"")) {
                        quotedValue = "\"" + values[a].trim() + "\"";
                    }
                    buff.append(keys[a].trim()).append(": ").append(quotedValue).append("\n");
                }
                result.add((Map<String, Object>) yaml.load(buff.toString()));
            }
        }
        return result;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String[] getIdCols() {
        return idCols;
    }

    public void setIdCols(String[] idCols) {
        this.idCols = idCols;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public int getTableRows() {
        try {
            List<Map<String, Object>> dataTable = getDataTable();
            return dataTable.size();
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return 0;
        }
    }

    public static TestData load(InputStream resourceAsStream) {
        Yaml yaml = new Yaml(new DateTimePropertyConstructor(TestData.class));
        return (TestData) yaml.load(resourceAsStream);
    }

    public boolean isParseValuesToTypes() {
        return parseValuesToTypes;
    }

    public void setParseValuesToTypes(boolean parseValuesToTypes) {
        this.parseValuesToTypes = parseValuesToTypes;
    }


    public String getColumnValue(int row, String key) {
        if (row < getTableRows()) {
            return String.format("%s", getDataTable().get(row).get(key));
        }
        return null;
    }
}


