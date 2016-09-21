/** ----------------------------------------------------------------------------
 *
 * ---          Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : OSGiPersistenceUnit.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.efmb.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import javax.persistence.spi.PersistenceUnitTransactionType;
import java.net.URL;
import java.util.Collection;

public class OSGiPersistenceUnit extends PersistenceUnitInfoImpl {

    private Bundle bundle;

    public OSGiPersistenceUnit(Bundle bundle, String persistenceUnitName, PersistenceUnitTransactionType transactionType) {
        super(bundle.adapt(BundleWiring.class).getClassLoader(), persistenceUnitName, transactionType);
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void addAnnotated() {
        if (!excludeUnlistedClasses()) {
            Collection<String> detected = JPAAnnotationScanner.findJPAAnnotatedClasses(bundle);
            for (String name : detected) {
                addClassName(name);
            }
        }
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return new TempBundleDelegatingClassLoader(bundle, classLoader);
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return bundle.getResource("/");
    }
}

