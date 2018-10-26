/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : MfShell.java
 * Author(s)   : xn01598
 * Created     : 27.07.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner;

import de.hf.dac.io.baserunner.BaseRunner;
import de.hf.dac.io.baserunner.SysExit;
import de.hf.dac.myfinance.runner.commands.Import;
import de.hf.dac.myfinance.runner.commands.ProcessTransactions;
import de.hf.dac.myfinance.runner.commands.ElasticSearch;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * command line runner for all mf commands
 */
@Command(description = "MyFinance shell. You can start Batch-jobs, view status ...",
    name = "MfShell", mixinStandardHelpOptions = true, versionProvider = de.hf.dac.io.baserunner.VersionProvider.class, subcommands = {
        Import.class, ProcessTransactions.class, ElasticSearch.class}
)
public class MfShell extends BaseRunner {

    /**
     * only executed if no subcommanf is called -> print help a
     * @return
     */
    @Override
    public void run() {
        CommandLine.usage(this, System.out, CommandLine.Help.Ansi.ON);
        this.shutdown(SysExit.USAGE);
    }

    public static void main(String[] args) {
        CommandLine.call(new MfShell(), args);
    }
}

