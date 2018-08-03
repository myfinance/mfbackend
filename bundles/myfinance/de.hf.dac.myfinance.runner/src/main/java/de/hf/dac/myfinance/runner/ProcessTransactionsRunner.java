/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : ProcessTransactionsRunner.java
 * Author(s)   : xn01598
 * Created     : 30.07.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hf.dac.myfinance.api.runner.BaseMFRunnerParameter;
import de.hf.dac.myfinance.api.runner.Runner;

/**
 * for future use
 * Only used now as a show case how to handle a second runner (additional to ImportRunner) in the cmdline tool
 */
public class ProcessTransactionsRunner implements Runner {
    public static final Logger LOG = LoggerFactory.getLogger(ProcessTransactionsRunner.class);


    @Override
    public void run(BaseMFRunnerParameter params) {
        LOG.info("process transactions... ");
        LOG.info("process transactions sucessful");
    }
}