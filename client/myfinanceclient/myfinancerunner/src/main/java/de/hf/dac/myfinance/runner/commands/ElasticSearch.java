/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : ElasticSearch.java
 * Author(s)   : hf
 * Created     : 26.10.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner.commands;

import java.io.IOException;

import de.hf.dac.io.baserunner.BaseRunner;
import de.hf.dac.io.web.WebRequest;
import de.hf.dac.myfinance.api.exceptions.MDException;
import de.hf.dac.myfinance.api.exceptions.MDMsgKey;
import picocli.CommandLine;

@CommandLine.Command(description = "Executes Elastic Search commands",
    name = "elasticsearch", aliases = "es", mixinStandardHelpOptions = true, version = "Elastic Search 1.0"
)
public class ElasticSearch  extends BaseRunner {

    @Override
    protected void run() {
        String url = "http://192.168.100.71:9200/karaf-2018.10.16/decanter/_search";
        try {
            new WebRequest().deleteRequest(url);
        } catch (IOException e) {
            throw new MDException(MDMsgKey.NO_RESPONSE_FROM_URL_EXCEPTION, "no response form "+url, e);
        }

    }
}
