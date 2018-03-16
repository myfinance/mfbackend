/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ReportGeneratorBase.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ReportGeneratorBase {

    private static final Logger LOG = LoggerFactory.getLogger(ReportGeneratorBase.class);


    static public String readFileFromStream(InputStream resourceAsStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));

        StringBuilder buff = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        try {
            while ((line = reader.readLine()) != null) {
                buff.append(line);
                buff.append(ls);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            reader.close();
        }
        return buff.toString();
    }


    public String getResourceFileAsString(String resourceName) throws IOException {
        return readFileFromStream(this.getClass().getResourceAsStream(resourceName));
    }

}
