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
import org.osgi.framework.Bundle;
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

    /*public <T> T createServiceMock(Class<T> clazz) {
        T mock = Mockito.mock(clazz);
        bundleContext.registerService(clazz.getName(), mock, null);
        return mock;
    }*/

    public <T> T getService(Class<T> clazz) {
        return (T) bundleContext.getService(bundleContext.getServiceReference(clazz.getName()));
    }

    protected Option getDebugOption() {
        return when(System.getProperty("PAXDEBUG") != null).useOptions(composite(vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005")));
    }

    protected DatabaseInfo postgresDatabaseInfo() {

        DatabaseInfo dbi = new DatabaseInfo("jdbc:postgresql://localhost:5432/marketdata","postgres", "vulkan", "org.postgresql.Driver");
        dbi.setDialect("org.hibernate.dialect.PostgreSQL82Dialect");
        return dbi;
    }

    protected DatabaseInfo inMemoryH2DatabaseInfo() {

        DatabaseInfo dbi = new DatabaseInfo("jdbc:h2:mem:test;DATABASE_TO_UPPER=FALSE;MVCC=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS dbo;","sa", "sa", "org.h2.Driver");
        dbi.setDialect("org.hibernate.dialect.H2Dialect");
        // force schema / table creation
        dbi.getExtraHibernateProperties().put("hibernate.hbm2ddl.auto","create");
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

    protected Option installFeatures(String groupId, String artefactId, String... names) {
        return composite(features(maven().groupId(groupId).artifactId(artefactId).type("xml").classifier("features").versionAsInProject(), names));
    }

    public Option karafStandardFeatures() {
        return installFeatures("org.apache.karaf.features", "standard", "standard", "eventadmin", "http", "scr", "webconsole", "wrap", "war");
    }

    public Option ioFeatures() {
        return installFeatures("de.hf.dac.features", "dac-base-features", "dac-base-features");
    }

    public Option restFeatures() {
        return installFeatures("de.hf.dac.features", "marketdata-features", "marketdata-features");
    }

    public Option jdbcFeatures() {
        return installFeatures("org.ops4j.pax.jdbc", "pax-jdbc-features", "pax-jdbc-pool-c3p0");
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

