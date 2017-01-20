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

import de.hf.dac.api.io.env.EnvironmentConfiguration;
import de.hf.dac.api.io.lookup.OSGIServiceLookup;
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
    public static final String REALM_NAME = "karaf";

    private AppConfigurationEntry[] configEntries;

    EnvironmentConfiguration serviceByInterface = null;

    @Activate
    public void activate(BundleContext bc)
    {
        // create the configuration entry field using ProxyLoginModule class
        serviceByInterface = OSGIServiceLookup.getServiceByInterface(EnvironmentConfiguration.class);

        configEntries = new AppConfigurationEntry[2];

        addAuthentificationModule(bc);
        addAuthorisationModule(bc);
    }

    private void addAuthentificationModule(BundleContext bc) {
        Map<String, Object> options = new HashMap<>();
        configEntries[0] = new AppConfigurationEntry(ProxyLoginModule.class.getName(),
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);

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

        options.put("java.naming.provider.url", serviceByInterface.getString("LDAP", "auth.ldap.url"));
        options.put("bindDN", serviceByInterface.getString("LDAP", "auth.ldap.user"));
        options.put("bindCredential",serviceByInterface.getString("LDAP", "auth.ldap.credentials") );
        options.put("baseCtxDN", serviceByInterface.getString("LDAP", "auth.ldap.baseCtxDN"));
        options.put("searchTimeLimit", "30000");
        options.put("baseFilter", "(sAMAccountName={0})");
        options.put("password-stacking", "useFirstPass");
        options.put("storePass", "true");
        options.put("useFirstPass", "false");
        options.put("rolesCtxDN", serviceByInterface.getString("LDAP", "auth.ldap.rolesCtxDN"));
        options.put("roleFilter", "(sAMAccountName={0})");
        options.put("roleAttributeID", "memberOf");
        options.put("roleAttributeIsDN", "true");
        options.put("roleNameAttributeID", "cn");
        options.put("roleRecursion", "-1");
        options.put("searchScope", "ONELEVEL_SCOPE");
        options.put("allowEmptyPasswords", "false");
        options.put("principalClass", "org.apache.karaf.jaas.boot.principal.UserPrincipal");
        options.put("debug", "false");
    }

    private void addAuthorisationModule(BundleContext bc) {
        Map<String, Object> options = new HashMap<>();
        configEntries[1] = new AppConfigurationEntry(ProxyLoginModule.class.getName(),
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);

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

        options.put("java.naming.provider.url", serviceByInterface.getString("LDAP", "role.ldap.url"));
        options.put("bindDN", serviceByInterface.getString("LDAP", "role.ldap.user"));
        options.put("bindCredential", serviceByInterface.getString("LDAP", "role.ldap.credentials"));
        options.put("baseCtxDN",serviceByInterface.getString("LDAP", "role.ldap.baseCtxDN") );
        options.put("baseFilter", "(dzuid={0})");
        options.put("rolesCtxDN", serviceByInterface.getString("LDAP", "role.ldap.rolesCtxDN"));
        options.put("roleFilter", "(uniqueMember={1})");
        options.put("roleAttributeID", "cn");
        options.put("permissionAttributeID", "dzpermissions");
        options.put("searchScope", "ONELEVEL_SCOPE");
        options.put("allowEmptyPasswords", "false");
        options.put("useFirstPass", "true");
        options.put("permissionRoleMapping", serviceByInterface.getString("LDAP", "role.mappings"));
        options.put("userAttributes", serviceByInterface.getString("LDAP", "role.userAttributes"));
        options.put("debug", "false");
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
        return 2;
    }
}