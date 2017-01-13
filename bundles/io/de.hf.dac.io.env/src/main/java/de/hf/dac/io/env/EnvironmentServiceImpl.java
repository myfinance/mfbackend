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

import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.EnvironmentConfiguration;
import de.hf.dac.api.io.env.EnvironmentInfo;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.api.io.env.EnvironmentTargetInfo;
import de.hf.dac.api.io.env.domain.DacEnvironmentConfiguration;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class EnvironmentServiceImpl implements EnvironmentService {

    private static final Logger LOG = Logger.getLogger(EnvironmentServiceImpl.class.getName());

    @Reference
    EnvironmentConfiguration configuration;

    @Reference
    EntityManagerFactorySetup emfb;

    private EnvironmentDAO environmentDAO;

    private BootstrapConfiguration bootstrapConfiguration = null;
    private EntityManagerFactory entityManagerFactory;

    private final Map<EnvironmentInfo, EnvironmentTargetInfo> allEnvironments = new HashMap<>();

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
            this.entityManagerFactory = emfb.buildEntityManagerFactory("ENV_CONFIGURATION", new Class[] { DacEnvironmentConfiguration.class },
                new ClassLoader[] { DacEnvironmentConfiguration.class.getClassLoader() }, dbInfo);

            environmentDAO = new EnvironmentDAO(entityManagerFactory);

            // createEnvironmentInfos
            createEnvironmentInformation();

        } catch (SQLException e) {
            LOG.severe("Unable to Retrieve Environments from Bootstrap DB");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void createEnvironmentInformation() {
        Properties properties = configuration.getProperties(BootstrapConfiguration.LOGIN_INFO_SECTION);
        List<DacEnvironmentConfiguration> envs = environmentDAO.getAllEnvironments();
        for (DacEnvironmentConfiguration env : envs) {
            EnvironmentTargetInfo savedEnv = null;
            if ("db".equals(env.getEnvtype())) {
                savedEnv = createDBEnvironentInfo(properties, env);
            } else if ("cache".equals(env.getEnvtype())) {
                //savedEnv = createCacheEnvironentInfo(env);
            }
            if (savedEnv != null) {
                allEnvironments.put(new EnvironmentInfo(savedEnv.getEnvName(), savedEnv.getTargetName(), savedEnv.getTargetType()), savedEnv);
            }
        }
    }

    /*private EnvironmentTargetInfo createCacheEnvironentInfo(DacEnvironmentConfiguration env) {
        Properties properties = configuration.getProperties("CACHE#" + env.getIdentifier());
        EnvironmentTargetInfo<CouchBaseInfo> savedEnv = new EnvironmentTargetInfo<>(env.getEnvironment(), env.getTarget(), env.getType());
        if (properties.size() != 0) {
            // look up cache info from ResFile
            String bucket = properties.getProperty("BUCKET");
            String password = properties.getProperty("PASSWORD");
            String servers = properties.getProperty("SERVERS");
            CouchBaseInfo cbi = new CouchBaseInfo(servers, bucket, password);
            savedEnv.setTargetDetails(cbi);
            return savedEnv;
        } else {
            LOG.warning("Unable to retrieve Couchbase Details from Configuration for "+savedEnv.toString());
        }
        return null;
    }*/

    private EnvironmentTargetInfo createDBEnvironentInfo(Properties properties, DacEnvironmentConfiguration env) {
        EnvironmentTargetInfo<DatabaseInfo> savedEnv = new EnvironmentTargetInfo<>(env.getEnvironment(), env.getTarget(), env.getEnvtype());
        // look up database info from ResFile
        if (properties.containsKey(env.getIdentifier())) {
            DatabaseInfo dbi = BootstrapConfiguration.extractDatabaseInfoFromConfig(this.configuration, env.getIdentifier());
            savedEnv.setTargetDetails(dbi);
            return savedEnv;
        } else {
            LOG.warning("Unable to retrieve DB Details from Configuration for "+savedEnv.toString());
        }
        return null;
    }

    @Override
    public Set<EnvironmentInfo> availableEnvironments() {
        if (initEnvironments()) {
            return allEnvironments.keySet();
        }
        return Collections.emptySet();
    }

    @Override
    public EnvironmentTargetInfo getTarget(String environmentName, String target) {
        if (initEnvironments()) {
            EnvironmentTargetInfo envTarget = allEnvironments.get(new EnvironmentInfo(environmentName, target));
            if (envTarget != null) {
                return envTarget;
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

