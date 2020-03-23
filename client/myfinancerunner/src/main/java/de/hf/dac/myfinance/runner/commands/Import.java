/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : Import.java
 * Author(s)   : xn01598
 * Created     : 03.08.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner.commands;

import de.hf.dac.myfinance.runner.BaseMFRunnerWithEnv;
import de.hf.dac.myfinance.runner.ImportRunner;
import picocli.CommandLine;

@CommandLine.Command(description = "Start all Price imports",
    name = "import", aliases = "i", mixinStandardHelpOptions = true, version = "Import 1.0"
)
public class Import extends BaseMFRunnerWithEnv {

    @Override
    public String getJobType() {
        return ImportRunner.class.getName();
    }
}
