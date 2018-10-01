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

import de.hf.dac.common.BuildMetadataUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.apache.log4j.Level;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Option;


/** Base class for all runners, contains common method for initialization. */
public abstract class BaseRunner implements Callable<Integer> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private boolean exitOnShutdown = true;

    @Option(names = { "-v", "--verbose" }, description = "Be verbose.")
    private boolean verbose = false;

    @Option(names = { "-debug", "--debug" }, description = "Debug Options enabled. Use Debug Options like data filtering")
    private boolean debug = false;

    @Option(names = { "-res", "--res" }, split = ",", description = "Resource file")
    private String[] resFiles;

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

    public Integer call() {
        if(verbose) {
            org.apache.log4j.Logger.getLogger(getClass()).setLevel(Level.DEBUG);
        }
        // log pid if available
        logPid();

        // log build info if available
        logBuildInfo();

        ResFileHandler.initFromResFile(resFiles, debug);
        int rc = 0;

        try {
            run();
        }
        catch (Exception e) {
            new CommandLine(this).usage(System.err);
            rc = SysExit.GENERIC_ERROR;
            log.error(e.getLocalizedMessage(), e);
        } finally {
            this.shutdown(rc);
        }



        return rc;
    }

    /**
     * Run.
     *
     */
    protected abstract void run();

    public boolean isExitOnShutdown() {
        return exitOnShutdown;
    }

    protected void setExitOnShutdown(boolean exitOnShutdown) {
        this.exitOnShutdown = exitOnShutdown;
    }

    protected void shutdown(int rc) {
        this.logRuntime();
        if(this.isExitOnShutdown()) {
            System.exit(rc);
        }
    }

    protected void shutdown(int rc, String message, Object... params) {
        log.error(message, params);
        new CommandLine(this).usage(System.err);
        shutdown(rc);
    }

    protected void logBuildInfo() {
        BuildMetadataUtil buildMetadata = BuildMetadataUtil.get(this.getClass());
        log.info("Dac Build version     : {} ", buildMetadata.getVersion());
        log.info("Dac Build timestamp   : {} ", buildMetadata.getBuildTimestamp());
        log.info("Dac Build User: {} ", buildMetadata.getUserName());

        MDC.put("DacVersion", buildMetadata.getVersion());
    }

    protected int secondsSinceStart() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }

    protected void logRuntime() {
        log.info("Runtime: {} seconds", secondsSinceStart());
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
