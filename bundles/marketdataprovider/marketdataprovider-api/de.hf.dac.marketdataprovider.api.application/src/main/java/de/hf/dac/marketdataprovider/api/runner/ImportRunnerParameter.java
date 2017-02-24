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
    public static final String IMPORTTYPE = "yahoo";

    public ImportRunnerParameter(String env, String importtype) {
        super(env);
        setImportType(importtype);
    }

    public String getImportType() {
        return (String) get(IMPORTTYPE);
    }

    public void setImportType(String importtype) {
        put(IMPORTTYPE, importtype);
    }
}
