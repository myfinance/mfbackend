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
 *  Author(s)   : xn01598
 *
 *  Created     : 18.07.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.common.util.jpa.impl;

import java.util.Collection;
import java.net.URL;

import javax.persistence.spi.PersistenceUnitTransactionType;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

public class OSGiPersistenceUnit extends PersistenceUnit {

    private Bundle bundle;

    public OSGiPersistenceUnit(Bundle bundle, String persistenceUnitName,
        PersistenceUnitTransactionType transactionType) {
        super(bundle.adapt(BundleWiring.class).getClassLoader(),persistenceUnitName, transactionType);
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