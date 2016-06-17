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
 *  Created     : 08.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.common.runner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class DatabaseInfo {
    @NonNull
    private String url;
    @NonNull
    private String user;
    @NonNull
    private String password;
    @NonNull
    private String driver;
    private String server;
    private String database;
}

