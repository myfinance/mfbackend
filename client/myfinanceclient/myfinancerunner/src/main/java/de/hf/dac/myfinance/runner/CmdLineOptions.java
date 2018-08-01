package de.hf.dac.myfinance.runner;

import de.hf.dac.io.baserunner.OptionsParser;

/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : CmdLineOptions.java
 * Author(s)   : xn01598
 * Created     : 30.07.2018
 * ----------------------------------------------------------------------------
 */

public enum CmdLineOptions {
    ENVIRONMENT("env", "environment", "the data environment of the backend", false),
    ACTIONTYPE("a", "action", "the job to trigger", true);

    private String shortName;
    private String longName;
    private String desc;
    private Boolean required;

    public String getShortName(){
        return this.shortName;
    }

    public void addOption(OptionsParser parser){
        parser.addOptionWithArg(shortName,longName,desc,shortName,required);
    }

    CmdLineOptions(String shortName, String longName, String desc, Boolean required){
        this.shortName = shortName;
        this.longName = longName;
        this.desc = desc;
        this.required = required;
    }
}
