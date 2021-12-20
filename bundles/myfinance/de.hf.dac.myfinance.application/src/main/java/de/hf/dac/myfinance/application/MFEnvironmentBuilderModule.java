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
import de.hf.dac.api.io.web.WebRequestService;
import de.hf.dac.io.audit.AuditDaoImpl;
import de.hf.dac.io.audit.AuditServiceImpl;
import de.hf.dac.myfinance.api.application.EnvTarget;
import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.*;
import de.hf.dac.myfinance.persistence.*;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.PriceService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveCache;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;
import de.hf.dac.myfinance.api.service.ValueService;
import de.hf.dac.myfinance.service.InstrumentServiceImpl;
import de.hf.dac.myfinance.service.PriceServiceImpl;
import de.hf.dac.myfinance.service.SimpleCurveCache;
import de.hf.dac.myfinance.service.TransactionServiceImpl;
import de.hf.dac.myfinance.service.ValueServiceImpl;
import de.hf.dac.myfinance.valuehandler.ValueCurveHandlerImpl;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

/**
 * Provides the application context of myfinance
 */
public class MFEnvironmentBuilderModule extends AbstractModule {

    private EnvironmentService envService;
    private EntityManagerFactorySetup emfb;
    TransactionManager jtaManager;
    private String env;
    WebRequestService webRequestService;
    ValueCurveCache cache;

    public MFEnvironmentBuilderModule(EnvironmentService envService, EntityManagerFactorySetup emfb,
        TransactionManager jtaManager, String env, WebRequestService webRequestService){
        this.envService=envService;
        this.emfb=emfb;
        this.jtaManager=jtaManager;
        this.env = env;
        this.webRequestService = webRequestService;
        this.cache = new SimpleCurveCache();
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
        bind(RecurrentTransactionDao.class).to(RecurrentTransactionDaoImpl.class);
        bind(CashflowDao.class).to(CashflowDaoImpl.class);
        bind(TradeDao.class).to(TradeDaoImpl.class);
        bind(EndOfDayPriceDao.class).to(EndOfDayPriceDaoImpl.class);
        bind(InstrumentService.class).to(InstrumentServiceImpl.class);
        bind(ValueCurveCache.class).toInstance(cache);
        bind(ValueCurveHandler.class).to(ValueCurveHandlerImpl.class);
        bind(ValueService.class).to(ValueServiceImpl.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class);
        bind(PriceService.class).to(PriceServiceImpl.class);
        bind(MarketDataEnvironment.class).to(MFEnvironmentImpl.class);
    }

    private EntityManagerFactory provideEntityManagerFactory(String targetName, ClassLoader classLoader) {
        var targetInfo = envService.getTarget(env, targetName);
        // TODO: 09.01.2017 die extra hibernate properties sollten aus dertabelle dacenvironmentconfiguration gelesen werden
            /* Properties extraHibernateProperties = new Properties();
            extraHibernateProperties.put("hibernate.hbm2ddl.auto", "create-drop");
            dbi.setExtraHibernateProperties(extraHibernateProperties);*/
        if (targetInfo.isPresent()) {
            var dataTargetInfo = targetInfo.get();
            DatabaseInfo dbi = (DatabaseInfo) dataTargetInfo.getTargetDetails();
            return new WrappedEntityManagerFactory(jtaManager,
                emfb.getOrCreateEntityManagerFactory(targetName, EntityManagerFactorySetup.PoolSize.SMALL, new Class[] {},
                    new ClassLoader[] { classLoader }, dbi));
        } else {
            throw new MFException(MFMsgKey.NO_TARGET_CONFIG_EXCEPTION,
                "No Config for Target " + targetName + " and Environment " + env + " found.");
        }
    }
}
