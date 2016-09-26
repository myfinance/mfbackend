/** ----------------------------------------------------------------------------
 *
 * ---          Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : PAXExamTestSetup.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 14.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.paxexam.support;

import de.hf.dac.api.io.efmb.DatabaseInfo;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.io.File;

import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.vmOption;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;

abstract public class PAXExamTestSetup {

    @Inject
    protected BundleContext bundleContext;

    @Configuration
    abstract public Option[] config();

    protected Option getDebugOption() {
        return when(System.getProperty("PAXDEBUG") != null).useOptions(composite(vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005")));
    }


    public Option embeddedH2Option() {
        return features(maven().groupId("de.hf.dac.features").artifactId("dac-io-features").type("xml").classifier("features").versionAsInProject(),
            "dac-h2-feature");
    }


    protected DatabaseInfo inMemoryH2DatabaseInfo() {

        DatabaseInfo dbi = new DatabaseInfo("jdbc:h2:mem:test;DATABASE_TO_UPPER=FALSE;MVCC=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS dbo;","sa", "sa", "org.h2.Driver");
        dbi.setDialect("org.hibernate.dialect.H2Dialect");
        // force schema / table creation
        dbi.getExtraHibernateProperties().put("hibernate.hbm2ddl.auto","create");
        return dbi;
    }

    protected DatabaseInfo postgresDatabaseInfo() {

        DatabaseInfo dbi = new DatabaseInfo("jdbc:postgresql://localhost:5432/marketdata","postgres", "vulkan", "org.postgresql.Driver");
        dbi.setDialect("org.hibernate.dialect.PostgreSQL82Dialect");
        // force schema / table creation
        dbi.getExtraHibernateProperties().put("hibernate.hbm2ddl.auto","create-drop");
        return dbi;
    }

    public Option karafContainerSetup() {
        return composite(karafDistributionConfiguration().frameworkUrl(maven().groupId("org.apache.karaf").artifactId("apache-karaf").versionAsInProject().type("zip"))
                .useDeployFolder(false).unpackDirectory(new File("target/paxexam/unpack/")),

            KarafDistributionOption.editConfigurationFilePut("etc/org.ops4j.pax.url.mvn.cfg", "org.ops4j.pax.url.mvn.proxySupport", "true"),
            KarafDistributionOption.editConfigurationFilePut("etc/org.ops4j.pax.web.cfg", "org.osgi.service.http.port", "8765"),
            KarafDistributionOption.editConfigurationFilePut("etc/org.apache.karaf.management.cfg", "rmiRegistryPort", "8768"),
            KarafDistributionOption.editConfigurationFilePut("etc/org.apache.karaf.management.cfg", "rmiServerPort", "8769"),
            KarafDistributionOption.editConfigurationFilePut("etc/org.apache.karaf.shell.cfg", "sshPort", "8770"),
            keepRuntimeFolder());
    }

    public Option karafStandardFeatures() {
        return composite(
            features(maven().groupId("org.apache.karaf.features").artifactId("standard").type("xml").classifier("features").versionAsInProject(),
                "standard", "http", "scr", "webconsole", "wrap"));
    }

    public Option ioFeatures() {
        return composite(
            features(maven().groupId("de.hf.dac.features").artifactId("dac-io-features").type("xml").classifier("features").versionAsInProject(),
                "dac-io-features")
        );
    }

    public Option restFeatures() {
        return composite(
            features(maven().groupId("de.hf.dac.features").artifactId("marketdata-restservice-features").type("xml").classifier("features").versionAsInProject(),
                "marketdata-restservice-features")
        );
    }

    public Option configDefaults() {
        return composite(
            getDebugOption(),
            // extract container into target directory
            karafContainerSetup(), //
            // deploy standard karaf Fetures
            karafStandardFeatures()); //
    }
}

