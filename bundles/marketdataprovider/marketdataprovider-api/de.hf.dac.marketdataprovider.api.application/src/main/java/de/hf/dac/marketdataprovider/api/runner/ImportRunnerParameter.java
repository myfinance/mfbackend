/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ImportRunnerParameter.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.runner;

public class ImportRunnerParameter extends BaseMDRunnerParameter{
    public static final String IMPORTTYPE = "importtype";

    public enum IMPORTTYPES {
        YAHOO,
        HTML
    }


    public ImportRunnerParameter(String env, IMPORTTYPES importtype) {
        super(env);
        setImportType(importtype);
    }

    public String getImportType() {
        return (String) get(IMPORTTYPE);
    }

    public void setImportType(IMPORTTYPES importtype) {
        put(IMPORTTYPE, importtype);
    }
}
