/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BootstrapConfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 22.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import java.io.Serializable;
import java.util.Properties;

import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.env.EnvironmentConfiguration;

/**
 * Class holding all Bootstrapping config parameters.
 */
public class BootstrapConfiguration implements Serializable {

    public static final String COM_SYBASE_JDBC4_JDBC_SYB_DRIVER = "com.sybase.jdbc4.jdbc.SybDriver";
    public static final String CONFIGDB_BOOTSTRAP_SECTION = "CONFIGDB_BOOTSTRAP_SECTION";
    public static final String LOGIN_INFO_SECTION = "LOGIN_INFO";

    private DatabaseInfo databaseInfo;

    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    private enum PROPERTY_NAMES {
        /**
         * Alias used in res files for bootstrap DB
         */
        BOOTSTRAP_LOGININFO, /**
         * Bootstrap DB Driver if using explicit url
         */
        BOOTSTRAP_URL, /**
         * Bootstrap DB Driver if using explicit url
         */
        BOOTSTRAP_DRIVER, BOOTSTRAP_PWD, BOOTSTRAP_USER, /**
         * Comma separated list of known DBs.
         * Only known are extracted from res files
         */
        EXPORTED_DBS,
        /**
         * FIXME: DE UNKLAR
         */
        SUBSET_ENVIRONMENTS,
        SQL_INI_FILE
    }

    private String loginInfoAlias;

    private String sqlIniFile;

    public BootstrapConfiguration() {
    }

    public String getLoginInfoAlias() {
        return loginInfoAlias;
    }

    public void setLoginInfoAlias(String loginInfoAlias) {
        this.loginInfoAlias = loginInfoAlias;
    }


    public String getSqlIniFile() {
        return sqlIniFile;
    }

    public void setSqlIniFile(String sqlIniFile) {
        this.sqlIniFile = sqlIniFile;
    }

    public static BootstrapConfiguration buildFromProperties(EnvironmentConfiguration envConf) {
        Properties properties = envConf.getProperties(CONFIGDB_BOOTSTRAP_SECTION);

        BootstrapConfiguration result = new BootstrapConfiguration();
        //
        result.loginInfoAlias = properties.getProperty(PROPERTY_NAMES.BOOTSTRAP_LOGININFO.name());
        String dbdriver = properties.getProperty(PROPERTY_NAMES.BOOTSTRAP_DRIVER.name());

        result.sqlIniFile = properties.getProperty(PROPERTY_NAMES.SQL_INI_FILE.name());

        // if not set use default
        if (dbdriver == null || dbdriver.length() == 0) {
            dbdriver = COM_SYBASE_JDBC4_JDBC_SYB_DRIVER;
        }

        // only check if not using LOGIN INFO Alias from RES file
        String dbUser = null;
        String dbPasswd = null;
        String dbUrl = null;
        if (result.loginInfoAlias == null || result.loginInfoAlias.length() == 0) {
            // no alias so we need jdbcUrl and user/paasswd
            dbUser = properties.getProperty(PROPERTY_NAMES.BOOTSTRAP_USER.name());
            dbPasswd = properties.getProperty(PROPERTY_NAMES.BOOTSTRAP_PWD.name());
            dbUrl = properties.getProperty(PROPERTY_NAMES.BOOTSTRAP_URL.name());
            result.databaseInfo = new DatabaseInfo(dbUrl, dbUser, dbPasswd, dbdriver);
        } else {
            // lookup alias in configuration
            result.databaseInfo = extractDatabaseInfoFromConfig(envConf,result.loginInfoAlias );
            return result;
        }

        return result;
    }

    public static DatabaseInfo extractDatabaseInfoFromConfig(EnvironmentConfiguration conf, String loginInfoAlias) {
        DatabaseInfo dbi = null;
        Properties loginInfoSection = conf.getProperties(LOGIN_INFO_SECTION);
        String aliasLine = loginInfoSection.getProperty(loginInfoAlias);
        if (aliasLine == null) {
            throw new RuntimeException(
                "Database Key " + loginInfoAlias + " not found in Environment. Please check all ResFiles/Configurations");
        }
        String[] parts = aliasLine.split("\\s*,\\s*");
        String dbUrl = parts[0];
        if (dbUrl == null) {
            throw new RuntimeException("Url not found not for DB " + loginInfoAlias + " in res files");
        }
        String dbUser = parts[1];
        String dbPasswd = conf.decrypt(parts[2]);

        if (dbUser == null) {
            throw new RuntimeException("User not found not for DB " + loginInfoAlias + " in res files");
        }
        if (dbPasswd == null) {
            throw new RuntimeException("Password not found for DB " + loginInfoAlias + " in res files");
        }

        String dbdriver = parts[3];
        if (dbUrl == null) {
            throw new RuntimeException("dbdriver not found not for DB " + loginInfoAlias + " in res files");
        }
        DatabaseInfo dbinfo = new DatabaseInfo(dbUrl, dbUser, dbPasswd, dbdriver, null, null);
        //dbinfo.getExtraHibernateProperties().put("hibernate.hbm2ddl.auto", "create");
        return dbinfo;
    }
}

