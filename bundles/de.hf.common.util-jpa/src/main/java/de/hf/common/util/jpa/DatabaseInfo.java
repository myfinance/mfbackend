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
 *  Author(s)   : xn01598
 *
 *  Created     : 18.07.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.common.util.jpa;

import lombok.Data;

@Data
public class DatabaseInfo {
    private String url;
    private String user;
    private String password;
    private String driver;
    private String server;
    private String database;

    public DatabaseInfo(String url, String user, String password, String driver) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
    }

    public DatabaseInfo(String url, String user, String password, String driver, String server, String database) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
        this.server = server;
        this.database = database;
    }
}





