/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFEnvironmentBuilderModule.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import de.hf.dac.api.io.audit.AuditDao;
import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.DacMessages;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.efmb.tx.WrappedEntityManagerFactory;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.EnvironmentTargetInfo;
import de.hf.dac.api.io.web.WebRequestService;
import de.hf.dac.io.audit.AuditDaoImpl;
import de.hf.dac.io.audit.AuditServiceImpl;
import de.hf.dac.myfinance.api.application.EnvTarget;
import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.exceptions.MDException;
import de.hf.dac.myfinance.api.exceptions.MDMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.persistence.EndOfDayPriceDaoImpl;
import de.hf.dac.myfinance.persistence.InstrumentDaoImpl;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.persistence.TransactionDaoImpl;
import de.hf.dac.myfinance.service.InstrumentServiceImpl;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;
import java.util.Optional;

/**
 * Provides the application context of myfinance
 */
public class MFEnvironmentBuilderModule extends AbstractModule {

    private EnvironmentService envService;
    private EntityManagerFactorySetup emfb;
    TransactionManager jtaManager;
    private String env;
    WebRequestService webRequestService;

    public MFEnvironmentBuilderModule(EnvironmentService envService, EntityManagerFactorySetup emfb,
        TransactionManager jtaManager, String env, WebRequestService webRequestService){
        this.envService=envService;
        this.emfb=emfb;
        this.jtaManager=jtaManager;
        this.env = env;
        this.webRequestService = webRequestService;
    }

    @Override
    protected void configure() {
        bind(EnvironmentService.class).toInstance(envService);
        bind(WebRequestService.class).toInstance(webRequestService);

        bind(EntityManagerFactory.class).annotatedWith(Names.named(EnvTarget.MDB)).toInstance(provideEntityManagerFactory(EnvTarget.MDB, Instrument.class.getClassLoader()));
        bind(EntityManagerFactory.class).annotatedWith(Names.named(AuditDao.ADB)).toInstance(provideEntityManagerFactory(AuditDao.ADB, DacMessages.class.getClassLoader()));

        bind(String.class).annotatedWith(Names.named("envID")).toInstance(env);
        bind(AuditDao.class).to(AuditDaoImpl.class);
        bind(AuditService.class).to(AuditServiceImpl.class);
        bind(InstrumentDao.class).to(InstrumentDaoImpl.class);
        bind(TransactionDao.class).to(TransactionDaoImpl.class);
        bind(EndOfDayPriceDao.class).to(EndOfDayPriceDaoImpl.class);
        bind(InstrumentService.class).to(InstrumentServiceImpl.class);

        bind(MarketDataEnvironment.class).to(MFEnvironmentImpl.class);
    }

    private EntityManagerFactory provideEntityManagerFactory(String targetName, ClassLoader classLoader) {
        Optional<EnvironmentTargetInfo> targetInfo = envService.getTarget(env, targetName);
        EnvironmentTargetInfo dataTargetInfo = targetInfo.get();
        DatabaseInfo dbi = (DatabaseInfo) dataTargetInfo.getTargetDetails();
        // TODO: 09.01.2017 die extra hibernate properties sollten aus dertabelle dacenvironmentconfiguration gelesen werden
            /* Properties extraHibernateProperties = new Properties();
            extraHibernateProperties.put("hibernate.hbm2ddl.auto", "create-drop");
            dbi.setExtraHibernateProperties(extraHibernateProperties);*/
        if (targetInfo.isPresent()) {
            return new WrappedEntityManagerFactory(jtaManager,
                emfb.getOrCreateEntityManagerFactory(targetName, EntityManagerFactorySetup.PoolSize.SMALL, new Class[] {},
                    new ClassLoader[] { classLoader }, dbi));
        } else {
            throw new MDException(MDMsgKey.NO_TARGET_CONFIG_EXCEPTION,
                "No Config for Target " + targetName + " and Environment " + env + " found.");
        }
    }

}
