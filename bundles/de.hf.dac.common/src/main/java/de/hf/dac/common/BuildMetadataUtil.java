/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BuildMetadataUtil.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 *
 * Helper to get build metadata via a simple java api at runtime.
 *
 * @author hf
 *
 */
public class BuildMetadataUtil {

    private static final Logger LOG = LoggerFactory.getLogger(BuildMetadataUtil.class);

    private static final String VERSION_PROPERTY = "dac.version";

    private static final String TIMESTAMP_PROPERTY = "dac.buildTimestamp";

    private static final String USER_PROPERTY = "dac.buildUser";

    private static final String META_DATA_PATH = "/version.properties";

    private static BuildMetadataUtil INSTANCE;

    private String version = null;

    private String buildTimestamp = null;

    private LocalDate buildTimestampDate;

    private String userName;


    private BuildMetadataUtil() {
        this(BuildMetadataUtil.class);
    }

    /**
     *
     */
    private BuildMetadataUtil(Class bundleClass) {
        try (InputStream in = bundleClass.getResourceAsStream(META_DATA_PATH)) {
            Properties p = new Properties();
            p.load(in);
            version = p.getProperty(VERSION_PROPERTY);
            buildTimestamp = p.getProperty(TIMESTAMP_PROPERTY);
            buildTimestampDate = getBuildTimestampAsDate();
            userName = p.getProperty(USER_PROPERTY, "N/A");
        } catch (IOException e) {
            LOG.error("Cannot read/use build meta data from " + META_DATA_PATH,e);
        }
    }

    public String getVersion() {
        return version;
    }

    public String getBuildTimestamp() {
        return buildTimestamp;
    }

    private LocalDate getBuildTimestampAsDate() {
        if (buildTimestampDate == null) {
            if (getBuildTimestamp() != null) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                buildTimestampDate = LocalDate.parse(getBuildTimestamp(), format);
            } else if (buildTimestampDate == null) {
                buildTimestampDate = LocalDate.of(1970, 1, 1);
            }
        }
        return buildTimestampDate;
    }

    /**
     * Accessor getting the singleton BuildMetadataUtil object.
     * Lazily initializes (reads) the instance upon first call.
     *
     * @return singleton instance of {@link BuildMetadataUtil}
     */
    synchronized public static BuildMetadataUtil get(Class bundleClass) {
        if (INSTANCE == null) {
            INSTANCE = new BuildMetadataUtil(bundleClass);
        }
        return INSTANCE;
    }

    public LocalDate getBuildTimestampDate() {
        return buildTimestampDate;
    }

    public String getUserName() {
        return userName;
    }
}
