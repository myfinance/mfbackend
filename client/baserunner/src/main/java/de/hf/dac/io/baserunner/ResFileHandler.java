/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ResFileHandler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.baserunner;

import de.hf.dac.io.config.resfile.Configuration;
import de.hf.dac.io.config.resfile.ResfileConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the arguments that were given on the command line.
 */
public class ResFileHandler {

    private static final String DAC_LOGIN_INFO = ResfileConfigurationImpl.DAC_LOGIN_INFO;

    private static final String DEVPROPS_FILENAME = ResfileConfigurationImpl.DEVPROPS_FILENAME;
    private static final String DAC_RES_PATH_ENV = ResfileConfigurationImpl.DAC_RES_PATH_ENV;

    private static final Logger LOG = LoggerFactory.getLogger(ResFileHandler.class);

    /**
     *
     * @param resFileArg
     * @param debug
     */
    public static void initFromResFile(String[] resFileArg, Boolean debug) {
        String devResFileName = System.getenv("DEV_RES_FILE") != null ? System.getenv("DEV_RES_FILE") : DEVPROPS_FILENAME;
        String[] resFiles = null;
        if (resFileArg!=null) {
            LOG.info("parse res Files from cmdline " + resFileArg + " reading it.");
            resFiles=resFileArg;
        } else if (new File(devResFileName).exists()) {
            LOG.info("Found development res file " + devResFileName + " reading it.");
            Configuration.parseResFile(devResFileName);
        } else {
            // collect files from environment variables
            List<String> resFileList = new ArrayList<String>();
            if (System.getenv(DAC_LOGIN_INFO) != null) {
                String loginResFileName = System.getenv(DAC_LOGIN_INFO);
                if (new File(loginResFileName).exists()) {
                    resFileList.add(loginResFileName);
                } else {
                    LOG.info("Res File {}  from environment variable {} does not exist", loginResFileName, DAC_LOGIN_INFO);
                }
            } else {
                LOG.info("Environment variable {} not set.", DAC_LOGIN_INFO);
            }
            if (System.getenv(DAC_RES_PATH_ENV) != null) {
                // take it from environment
                String poetResFileName = System.getenv(DAC_RES_PATH_ENV) + "/" + ResfileConfigurationImpl.DAC_RES_FILENAME;

                if (new File(poetResFileName).exists() && !resFileList.contains(poetResFileName)) {
                    resFileList.add(poetResFileName);
                } else {
                    LOG.info("Res File {}  from environment variable {} does not exist", poetResFileName, DAC_RES_PATH_ENV);
                }
            } else {
                LOG.info("Environment variable {} not set.", DAC_RES_PATH_ENV);
            }
            if (resFileList.isEmpty()) {
                LOG.error("Using development defaults", DAC_LOGIN_INFO);
                resFiles = new String[] { "../" + ResfileConfigurationImpl.DAC_RES_FILENAME };
            } else {
                resFiles = resFileList.toArray(new String[resFileList.size()]);
            }
        }


        for (String filename : resFiles) {
            LOG.info("Loading res file {}", filename);
            if (filename != null) {
                Configuration.parseResFile(filename);
            }
        }

        // set DEBUG if provided as commandLine Parameter
        if (debug) {
            Configuration.setIsDebug(true);
            LOG.error("RUNNING IN DEBUG MODE");
        } else {
            Configuration.setIsDebug(false);
        }
    }
}

