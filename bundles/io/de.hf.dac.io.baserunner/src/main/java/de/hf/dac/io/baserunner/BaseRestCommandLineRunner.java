/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseRestCommandLineRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.baserunner;

import de.hf.dac.common.BuildMetadataUtil;
import org.apache.commons.cli.ParseException;
import org.slf4j.MDC;

import java.util.Base64;

/**
 * Base class for all runners which starts jobs in an container via rest interface
 */
public abstract class BaseRestCommandLineRunner extends BaseRunner {

    private boolean exitOnShutdown = true;
    private OptionsParser optionsParser;

    public BaseRestCommandLineRunner(OptionsParser optionsParser) {
        this.optionsParser = optionsParser;
    }

    /**
     * Run.
     *
     * @param args the args
     * @throws Exception the exception
     */
    public void run(String[] args) throws Exception {
        // log pid if available
        logPid();

        // log build info if available
        logBuidInfo();

        if (args != null) {
            prepareCommandLineParser(args);
        } else {
            log.info("No Programm Arguments provided. ");
            System.exit(0);
        }
    }

    /**
     * Subclass can add extra CLI Options here
     */
    protected abstract void addCustomCommandLineOptions();

    protected void prepareCommandLineParser(String[] args) throws ParseException {
        addBaseCommandLineOptions();
        addCustomCommandLineOptions();

        optionsParser.parse(args);

        // handle defaults like db connect
        handleDefaultOptions(optionsParser);

    }

    private void addBaseCommandLineOptions() {
        // enable DEBUGGING by using Command Line Parameter
        optionsParser.addOption(OptionsParser.DEBUG_OPTION, "Debug Options enabled", "Use Debug Options like data filtering .", false);
    }

    public boolean isExitOnShutdown() {
        return exitOnShutdown;
    }

    public void setExitOnShutdown(boolean exitOnShutdown) {
        this.exitOnShutdown = exitOnShutdown;
    }

    protected void logBuidInfo() {
        BuildMetadataUtil buildMetadata = BuildMetadataUtil.get(this.getClass());
        log.info("CCR Build version     : {} ", buildMetadata.getCCRVersion());
        log.info("CCR Build timestamp   : {} ", buildMetadata.getBuildTimestamp());
        log.info("CCR Build User: {} ", buildMetadata.getUserName());

        MDC.put("CCRVersion", buildMetadata.getCCRVersion());
    }

    protected abstract void passParamsToExternal(String var1, RunnerParameter var2);

}

