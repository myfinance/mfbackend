/** ----------------------------------------------------------------------------
 *
 * ---                                ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : BaseRunner.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 08.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.common.runner;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.lang.management.ManagementFactory;

@Slf4j
public abstract class BaseRunner {
    protected static final String DATE_PATTERN = "yyyy-MM-dd";
    protected long startTime = System.currentTimeMillis();


    /**
     * Run.
     *
     * @param args the args
     * @throws Exception the exception
     */
    public void run(String[] args) throws Exception {
        // log pid if available
        logPid();
    }

    protected int secondsSinceStart() {
        return (int)((System.currentTimeMillis() - this.startTime) / 1000L);
    }

    protected void logRuntime() {
        this.log.info("Runtime: {} seconds", Integer.valueOf(this.secondsSinceStart()));
    }

    protected void logDatabaseConnection(String type, DatabaseInfo info) {
        this.log.info(" connecting {} {} / {} / {}", new Object[]{type != null?type:"", info.getServer(), info.getDatabase(), info.getUser()});
        this.log.info(" URL: {}", info.getUrl());
    }


    protected void logPid() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf(64);
        if(index < 1) {
            this.log.info("Could not read Process ID: {} ", jvmName);
        } else {
            String pidString = jvmName.substring(0, index);
            this.log.info("Process ID: {} ", pidString);
            MDC.put("PID", pidString);
        }

    }
}
