/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 22.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import de.hf.dac.api.io.domain.DacServiceconfiguration;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentConfiguration;
import de.hf.dac.api.io.env.EnvironmentInfo;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.EnvironmentTargetInfo;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Component(service = { EnvironmentService.class }, name = "DAC.EnvironmentServiceImpl")
public class EnvironmentServiceImpl implements EnvironmentService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(EnvironmentServiceImpl.class.getName());

    @Reference
    EnvironmentConfiguration configuration;

    @Reference
    EntityManagerFactorySetup emfb;

    private EnvironmentDAO environmentDAO;

    private BootstrapConfiguration bootstrapConfiguration = null;
    private EntityManagerFactory entityManagerFactory;

    private final Map<String, EnvironmentInfo> allEnvironments = new HashMap<>();
    private final Map<String, EnvironmentInfo> mappedByClientID = new HashMap<>();
    private final Map<String, Map<String,String>> contextMap = new HashMap<>();

    public boolean initEnvironments() {

        // already done
        if (bootstrapConfiguration != null) {
            return true;
        }

        try {
            DatabaseInfo bootstapDBInfo = null;
            // run the bootstrapper once
            if (bootstrapConfiguration == null) {
                bootstrapConfiguration = BootstrapConfiguration.buildFromProperties(configuration);
            }

            // try to extract DB Infos
            DatabaseInfo dbInfo = bootstrapConfiguration.getDatabaseInfo();

            // create JPA EntityManagerFactory for configuration classes
            this.entityManagerFactory = emfb.getOrCreateEntityManagerFactory("ENV_CONFIGURATION",
                EntityManagerFactorySetup.PoolSize.SMALL,
                new Class[] { },
                new ClassLoader[] { DacServiceconfiguration.class.getClassLoader() }, dbInfo);

            environmentDAO = new EnvironmentDAO(entityManagerFactory);

            // createEnvironmentInfos
            createEnvironmentInformation(dbInfo);

        } catch (Exception e) {
            LOG.error("Unable to Retrieve Environments from Bootstrap DB",e);
            return false;
        }
        return true;
    }

    private void createEnvironmentInformation(DatabaseInfo dbInfo) {
        Properties properties = configuration.getProperties(BootstrapConfiguration.LOGIN_INFO_SECTION);
        List<DacServiceconfiguration> envs = environmentDAO.getAllEnvironments();
        for (DacServiceconfiguration env : envs) {

            try {

                // see, if it's already created
                EnvironmentInfo info = allEnvironments.get(env.getEnvironment());
                if (info == null) {
                    info = new EnvironmentInfo(env.getEnvironment());
                    // default target sdb pointing to Bootstrap ConfigDB, because it should exist always only one security db for all environments
                    info.addTarget(new EnvironmentTargetInfo<>(env.getEnvironment(), "sdb", "db", dbInfo));
                    allEnvironments.put(env.getEnvironment(), info);
                }

                EnvironmentTargetInfo savedEnv = null;
                if ("db".equals(env.getEnvtype())) {
                    savedEnv = createDBEnvironentInfo(properties, env);
                }
                if (savedEnv != null) {
                    info.addTarget(savedEnv);
                }
            } catch (Exception ex) {
                // ignore and make sure this target becomes prominent Entry in Log File
                LOG.error("Skipping Target:" + env.getTarget(), ex.getLocalizedMessage());
            }
        }
    }


    private EnvironmentTargetInfo createDBEnvironentInfo(Properties properties, DacServiceconfiguration env) {
        EnvironmentTargetInfo<DatabaseInfo> savedEnv = new EnvironmentTargetInfo<>(env.getEnvironment(), env.getTarget(), env.getEnvtype());
        // look up database info from ResFile
        if (properties.containsKey(env.getIdentifier())) {
            DatabaseInfo dbi = BootstrapConfiguration.extractDatabaseInfoFromConfig(this.configuration, env.getIdentifier());
            savedEnv.setTargetDetails(dbi);
            return savedEnv;
        } else {
            LOG.warn("Unable to retrieve DB Details from Configuration for " + savedEnv.toString() + ", Identifier not found in Config: " + env.getIdentifier());
        }
        return null;
    }

    @Override
    public Collection<EnvironmentInfo> availableEnvironments() {
        if (initEnvironments()) {
            return allEnvironments.values();
        }
        return Collections.emptySet();
    }

    @Override
    public EnvironmentInfo getEnvironmentByClientid(String clientID) {
        return mappedByClientID.get(clientID);
    }

    @Override
    public Map<String,String> getEnvironmentContextMap(String env) {

        return contextMap.containsKey(env) ? contextMap.get(env) : new HashMap<>();
    }

    @Override
    public EnvironmentInfo getEnvironment(String env) {
        if (allEnvironments.containsKey(env)) {
            return allEnvironments.get(env);
        } else {
            throw new RuntimeException("Unknown Environment " + env);
        }
    }

    @Override
    public void refresh() {
        bootstrapConfiguration = null;
        allEnvironments.clear();
        mappedByClientID.clear();
        initEnvironments();
    }

    @Override
    public Optional<EnvironmentTargetInfo> getTarget(String environmentName, String target) {
        if (initEnvironments()) {
            EnvironmentInfo env = allEnvironments.get(environmentName);
            EnvironmentTargetInfo envTarget = null;
            if (env != null) {
                envTarget = env.getTargets().get(target);
                if (envTarget != null) {
                    return Optional.of(envTarget);
                } else {
                    return Optional.empty();
                }
            } else {
                throw new RuntimeException("Unknown Environment");
            }

        }
        throw new RuntimeException("Not initialised");
    }

    public EntityManagerFactorySetup getEmfb() {
        return emfb;
    }

    public void setEmfb(EntityManagerFactorySetup emfb) {
        this.emfb = emfb;
    }

    @Override
    public EnvironmentConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(EnvironmentConfiguration configuration) {
        this.configuration = configuration;
    }

}
