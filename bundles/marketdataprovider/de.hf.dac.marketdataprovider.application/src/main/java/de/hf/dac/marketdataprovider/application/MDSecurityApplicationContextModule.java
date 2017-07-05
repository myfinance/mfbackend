/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDSecurityApplicationContextModule.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 17.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.efmb.tx.WrappedEntityManagerFactory;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.EnvironmentTargetInfo;
import de.hf.dac.marketdataprovider.api.application.EnvTarget;
import de.hf.dac.security.restauthorization.domain.RestAuthorization;

import javax.persistence.EntityManagerFactory;

public class MDSecurityApplicationContextModule extends AbstractModule {

    private final String env;
    private final EnvironmentService envService;
    private final EntityManagerFactorySetup emfb;

    public MDSecurityApplicationContextModule(String env, EnvironmentService envService, EntityManagerFactorySetup emfb) {
        this.env = env;
        this.envService = envService;
        this.emfb = emfb;
    }

    private String buildPersistenceUnitName(String target) {
        return  "MD/" + "Security" + "/" + env + "/" + target;
    }

    @Override
    protected void configure() {

        bind(EntityManagerFactory.class).annotatedWith(Names.named(EnvTarget.SDB)).toInstance(provideSdbEntityManagerFactory());


    }

    EntityManagerFactory provideSdbEntityManagerFactory() {
        EnvironmentTargetInfo sdbTargetInfo = envService.getTarget(env, EnvTarget.SDB).get();
        DatabaseInfo dbi = (DatabaseInfo) sdbTargetInfo.getTargetDetails();
        EntityManagerFactory emf = null;
        try {
            emf = emfb
                .getOrCreateEntityManagerFactory(this.buildPersistenceUnitName("SECURITY"), EntityManagerFactorySetup.PoolSize.SMALL, new Class[] {}, new ClassLoader[] { RestAuthorization.class.getClassLoader() }, dbi);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return emf;
    }
}
