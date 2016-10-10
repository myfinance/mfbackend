/** ----------------------------------------------------------------------------
 *
 * ---          HF- Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EntityManagerFactorySetup.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.efmb;

import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

/**
 * Created by xne0133 on 05.09.2016.
 */
public interface EntityManagerFactorySetup {
    EntityManagerFactory buildEntityManagerFactory(String persistenceUnit,
        Class<?>[] entities,
        ClassLoader[] classLoaders,
        DatabaseInfo dbi) throws SQLException;
    EntityManagerFactory buildEntityManagerFactory(String persistenceUnit,
        ClassLoader[] classLoaders,
        DatabaseInfo dbi) throws SQLException;
}