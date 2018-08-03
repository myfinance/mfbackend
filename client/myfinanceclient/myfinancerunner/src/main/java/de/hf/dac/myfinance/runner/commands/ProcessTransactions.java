/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : ProcessTransactions.java
 * Author(s)   : xn01598
 * Created     : 03.08.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner.commands;

import de.hf.dac.myfinance.runner.BaseMFRunnerWithEnv;
import de.hf.dac.myfinance.runner.ProcessTransactionsRunner;
import picocli.CommandLine;

@CommandLine.Command(description = "ProcessTransactions",
    name = "processtransactions", aliases = "pt", mixinStandardHelpOptions = true, version = "ProcessTransactions 1.0"
)
public class ProcessTransactions extends BaseMFRunnerWithEnv {

    @Override
    public String getJobType() {
        return ProcessTransactionsRunner.class.getName();
    }
}