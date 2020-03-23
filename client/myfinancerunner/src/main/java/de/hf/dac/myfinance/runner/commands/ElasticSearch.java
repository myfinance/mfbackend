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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import de.hf.dac.io.baserunner.BaseRunner;
import de.hf.dac.io.config.resfile.Configuration;
import de.hf.dac.io.web.WebRequest;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import picocli.CommandLine;

@CommandLine.Command(description = "Executes Elastic Search commands",
    name = "elasticsearch", aliases = "es", mixinStandardHelpOptions = true, version = "Elastic Search 1.0"
)
public class ElasticSearch  extends BaseRunner {

    private static final String CONFIG_SECTION = "MF_ELASTIC_SEARCH";

    @Override
    protected void run() {
        WebRequest request = new WebRequest();
        String url = Configuration.getString(CONFIG_SECTION, "ES_URL", "http://192.168.100.71:9200/");
        int days = Configuration.getInt(CONFIG_SECTION, "ES_DAYS_AVAILABLE", 5);
        LocalDate lastDay = LocalDate.now().minusDays(days);
        try {
            Map<String, Object> response = request.getJsonMapFromUrl(url+"karaf-*/_alias");
            for (String index: response.keySet()) {
                log.info("found index for day:" + index.substring(6));
                LocalDate indexdate = LocalDate.parse(index.substring(6), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                if(indexdate.isBefore(lastDay)){
                    log.info("delete:" + request.deleteRequest(url+index));
                }
            }


        } catch (IOException e) {
            throw new MFException(MFMsgKey.NO_RESPONSE_FROM_URL_EXCEPTION, "no response form "+url, e);
        }

    }
}
