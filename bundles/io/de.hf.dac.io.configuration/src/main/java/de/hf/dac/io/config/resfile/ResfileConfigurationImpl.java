/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ResfileConfigurationImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 22.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.config.resfile;

import de.hf.dac.api.io.env.EnvironmentConfiguration;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Environment Configuration based on Res Files located on
 * local file system.
 */
@OsgiServiceProvider( classes = {EnvironmentConfiguration.class})
@Singleton
public class ResfileConfigurationImpl implements EnvironmentConfiguration {

    private static final Logger LOG = Logger.getLogger(ResfileConfigurationImpl.class.getName());

    private ResFileParser resFileParser = new ResFileParser();

    public static final String DEVPROPS_FILENAME = "dev.res";
    public static final String DAC_RES_FILENAME = "dac.res";
    public static final String DAC_LOGIN_INFO = "DAC_LOGIN_INFO";
    public static final String DAC_RES_PATH_ENV = "CAD_RES_PATH";

    private List<String> knownResFiles = new ArrayList<>();

    /**
     * 1. get path to a res.file with login informations from Environment variable - only admin should have read access to this file
     * 2. get path to dac.res - resfile for an environment like UAT or Production - with all informations even 3rd level support are allowed to read
     * 3. get dac.res from target/.. directory for development use
     * 4. get dev.res from target/.. directory for local overrides from a single developer (never check this in)
     */
    private static ResFileLocation[] resFileSearchOrder =
        new ResFileLocation[] { ResFileLocation.DAC_LOGIN_INFO_FILE, ResFileLocation.DAC_RES_PATH_ENV_FILE, ResFileLocation.DEVELOPER_DAC_RES, ResFileLocation.DEVELOPER_LOCAL_RES,
        };

    public ResfileConfigurationImpl(ResFileParser parser) {
        this.resFileParser = parser;
        searchResFiles();
    }

    public ResfileConfigurationImpl() {
        searchResFiles();
    }

    protected void searchResFiles() {
        for (ResFileLocation resFileLocation : resFileSearchOrder) {
            parseResFile(resFileLocation);
        }
    }

    public ResFileParser getResFileParser() {
        return resFileParser;
    }

    private void parseResFile(ResFileLocation resFileLocation) {
        LOG.info("Search RES File using "+resFileLocation.toString());
        String pathToResFile = null;
        String fileName = null;
        // calculate path
        if (resFileLocation.type == ResFileType.SYSTEM_PROPERTY_PATH) {
            pathToResFile = System.getenv(resFileLocation.pathOrPropertyForPath);
            fileName = pathToResFile+ File.separator + resFileLocation.resFileName;
        } else if (resFileLocation.type == ResFileType.RELATIVE_PATH) {
            pathToResFile = resFileLocation.pathOrPropertyForPath;
            fileName = pathToResFile+ File.separator + resFileLocation.resFileName;
        } else if (resFileLocation.type == ResFileType.ABSOLUTE_PATH) {
            fileName = resFileLocation.pathOrPropertyForPath != null ? resFileLocation.pathOrPropertyForPath : resFileLocation.resFileName;
        }else if (resFileLocation.type == ResFileType.SYSTEM_PROPERTY_ABSOLUTEPATH) {
            fileName = System.getenv(resFileLocation.pathOrPropertyForPath);
        }

        if (fileName != null) {

            File f = new File(fileName);
            LOG.info("Search RES File using AbsolutePath"+f.getAbsolutePath());
            if (f.exists()) {
                LOG.info("Reading Properties from " + f.getAbsolutePath());
                knownResFiles.add(f.getAbsolutePath());
                getResFileParser().load(fileName);
            }
        }
    }

    public List<String> getKnownResFiles() {
        return knownResFiles;
    }

    private enum ResFileLocation {
        DEVELOPER_RES_FILE(ResFileType.RELATIVE_PATH, "../", DEVPROPS_FILENAME ), DAC_LOGIN_INFO_FILE(ResFileType.SYSTEM_PROPERTY_ABSOLUTEPATH, DAC_LOGIN_INFO, null),
        DAC_RES_PATH_ENV_FILE(ResFileType.SYSTEM_PROPERTY_PATH, DAC_RES_PATH_ENV, DAC_RES_FILENAME)
        , DEVELOPER_DAC_RES(ResFileType.RELATIVE_PATH, "../..", DAC_RES_FILENAME)
        ,DEVELOPER_LOCAL_RES(ResFileType.RELATIVE_PATH, "../..", DEVPROPS_FILENAME)
        ;

        @Override
        public String toString() {
            return "ResFileLocation{" + "type=" + type + ", pathOrPropertyForPath='" + pathOrPropertyForPath + '\'' + ", resFileName='" + resFileName
                + '\'' + '}';
        }

        ResFileLocation(ResFileType type, String pathOrPropertyForPath, String resFileName) {
            this.type = type;
            this.pathOrPropertyForPath = pathOrPropertyForPath;
            this.resFileName = resFileName;
        }

        private final ResFileType type;
        private final String pathOrPropertyForPath;
        private final String resFileName;

    }

    private static enum ResFileType {
        ABSOLUTE_PATH, SYSTEM_PROPERTY_PATH, SYSTEM_PROPERTY_ABSOLUTEPATH, RELATIVE_PATH
    }

    @Override
    public String decrypt(String value) {
        return ResFileParser.decrypt(value);
    }

    @Override
    public String getString(String section, String key) {
        return resFileParser.getString(section, key);
    }

    @Override
    public String getString(String section, String key, String def) {
        return resFileParser.getString(section, key, def);
    }

    public void load(String filename) {
        resFileParser.load(filename);
    }

    @Override
    public boolean getBoolean(String section, String key, boolean def) {
        return resFileParser.getBoolean(section, key, def);
    }

    @Override
    public Properties getProperties(String section) {
        Properties sectionProps = new Properties();
        Map<String, String> properties = resFileParser.getProperties(section);
        if (properties != null) {
            sectionProps.putAll(properties);
        }
        return sectionProps;
    }

}


