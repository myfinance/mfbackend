/** ----------------------------------------------------------------------------
 *
 * ---          Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : DatabaseInfo.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.efmb;

import java.util.Properties;

public class DatabaseInfo {
    private String url;
    private String user;
    private String password;
    private String driver;
    private String server;
    private String database;
    private String dialect;
    private String jndiUrl;
    private Properties extraHibernateProperties;

    public DatabaseInfo(String url, String user, String password, String driver) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
        extraHibernateProperties = new Properties();
        this.jndiUrl = null;
    }

    public DatabaseInfo(String url, String user, String password, String driver, String server, String database) {
        this(url, user, password, driver);
        this.server = server;
        this.database = database;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return this.driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getJndiUrl() {
        return jndiUrl;
    }

    public void setJndiUrl(String jndiUrl) {
        this.jndiUrl = jndiUrl;
    }

    public Properties getExtraHibernateProperties() {
        return extraHibernateProperties;
    }

    public void setExtraHibernateProperties(Properties extraHibernateProperties) {
        this.extraHibernateProperties = extraHibernateProperties;
    }

    @Override
    public String toString() {
        return "url = " + url + "\nuser = " + user + "\ndriver = " + driver + "\nserver = " + server +
            "\ndatabase = " + database +"\ndialect = " + dialect + "\njndiUrl = " + jndiUrl + "\n";
    }
}