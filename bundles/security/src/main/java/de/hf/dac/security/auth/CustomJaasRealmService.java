/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : CustomJaasRealmService.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 28.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth;

import de.hf.dac.security.auth.modules.LdapLoginModule;
import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.config.JaasRealm;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true)
public class CustomJaasRealmService implements JaasRealm
{
    public static final String REALM_NAME = "customRealm";

    private AppConfigurationEntry[] configEntries;

    @Activate
    public void activate(BundleContext bc)
    {
        // create the configuration entry field using ProxyLoginModule class

        Map<String, Object> options = new HashMap<>();
        configEntries = new AppConfigurationEntry[1];
        configEntries[0] = new AppConfigurationEntry(ProxyLoginModule.class.getName(),
            AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT, options);

        // actual LoginModule class name will be passed using the options object

        options.put(ProxyLoginModule.PROPERTY_MODULE, LdapLoginModule.class.getName());

        // put bundle id of the LoginModule and bundlecontext of it
        // (in this case, it is the same bundle)
        // This is a neat trick to adapt to OSGI classloader

        long bundleId = bc.getBundle().getBundleId();
        options.put(ProxyLoginModule.PROPERTY_BUNDLE, String.valueOf(bundleId));
        options.put(BundleContext.class.getName(), bc);

        // add extra options if needed; for example, karaf encryption
        // ....
    }

    @Override
    public AppConfigurationEntry[] getEntries()
    {
        return configEntries;
    }

    // return the name and the rank of the realm

    @Override
    public String getName()
    {
        return REALM_NAME;
    }

    @Override
    public int getRank()
    {
        return 0;
    }
}