/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : Action.java
 * Author(s)   : hf
 * Created     : 30.07.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner;

public enum Action {
    IMPORT("i", "import prices", ImportRunner.class.getName()),
    PROCESS_TRANSACTIONS("pt", "process transactions", ProcessTransactionsRunner.class.getName());

    private String cmdlineAlias;
    private String userHelp;
    private String jobType;

    public String getCmdlineAlias(){
        return this.cmdlineAlias;
    }

    public String getUserHelp(){
        return this.userHelp;
    }

    public String getJobType(){
        return this.jobType;
    }

    public Action getActionByAlias(String alias){
        if(alias.equals(Action.IMPORT.getCmdlineAlias())) return Action.IMPORT;
        if(alias.equals(Action.PROCESS_TRANSACTIONS.getCmdlineAlias())) return Action.PROCESS_TRANSACTIONS;
        throw new IllegalArgumentException("Invalid Action Argument: " + alias);
    }

    Action(String cmdlineAlias, String userHelp, String jobType){

        this.cmdlineAlias = cmdlineAlias;
        this.userHelp = userHelp;
        this.jobType = jobType;
    }
}