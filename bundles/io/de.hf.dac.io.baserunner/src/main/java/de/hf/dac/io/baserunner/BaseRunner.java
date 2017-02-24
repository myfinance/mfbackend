/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.baserunner;

import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.common.BuildMetadataUtil;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.apache.log4j.Level;

import java.lang.management.ManagementFactory;

/** Base class for all runners, contains common method for initialization. */
public abstract class BaseRunner {
    protected static final String DATE_PATTERN = "yyyy-MM-dd";
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private boolean exitOnShutdown = true;
    protected OptionsParser optionsParser;
    private Class<?> clazz;

    /** The start time (System.currentTimeMillis) of the program set in the base constructor */
    protected long startTime;

    /**
     * The default constructor.
     */
    public BaseRunner() {
        startTime = System.currentTimeMillis();
        // Optionally remove existing handlers attached to j.u.l root logger
        SLF4JBridgeHandler.removeHandlersForRootLogger(); // (since SLF4J 1.6.5)

        // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
        // the initialization phase of your application
        SLF4JBridgeHandler.install();
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
        if(optionsParser.isVerbose()) {
            org.apache.log4j.Logger.getLogger(this.clazz).setLevel(Level.DEBUG);
        }
    }

    public boolean isExitOnShutdown() {
        return exitOnShutdown;
    }

    public void setExitOnShutdown(boolean exitOnShutdown) {
        this.exitOnShutdown = exitOnShutdown;
    }

    public void shutdown(int rc) {
        this.logRuntime();
        if(this.isExitOnShutdown()) {
            System.exit(rc);
        }

    }

    protected void logBuidInfo() {
        BuildMetadataUtil buildMetadata = BuildMetadataUtil.get(this.getClass());
        log.info("Dac Build version     : {} ", buildMetadata.getCCRVersion());
        log.info("Dac Build timestamp   : {} ", buildMetadata.getBuildTimestamp());
        log.info("Dac Build User: {} ", buildMetadata.getUserName());

        MDC.put("DacVersion", buildMetadata.getCCRVersion());
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

    protected int secondsSinceStart() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }

    protected void logRuntime() {
        log.info("Runtime: {} seconds", secondsSinceStart());
    }

    protected void logDatabaseConnection(String type, DatabaseInfo info) {
        log.info(" connecting {} {} / {} / {}", type != null ? type : "", info.getServer(), info.getDatabase(), info.getUser());
        log.info(" URL: {}", info.getUrl());
    }

    protected void handleDefaultOptions(OptionsParser op) {
        if (op.isVerboseSQL() || op.isExtraVerboseSQL()) {
            CustomPropertyPlaceholderConfigurer.setOverrideProperty("jdbc.showSql", "true");
            org.apache.log4j.Logger.getLogger("org.hibernate.id.IdentifierGeneratorHelper").setLevel(Level.DEBUG);

        }
        if (op.isExtraVerboseSQL()) {
            // output binding parameters as well
            org.apache.log4j.Logger.getLogger("org.hibernate.type.descriptor.sql.BasicBinder").setLevel(Level.TRACE);
        }
    }

    protected void logPid() {
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');

        if (index < 1) {
            log.info("Could not read Process ID: {} ", jvmName);
        } else {
            String pidString = jvmName.substring(0, index);
            log.info("Process ID: {} ", pidString);
            MDC.put("PID", pidString);
        }
    }

}
