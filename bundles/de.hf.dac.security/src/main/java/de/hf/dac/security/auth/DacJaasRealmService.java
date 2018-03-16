/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DacJaasRealmService.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 28.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth;

import de.hf.dac.security.auth.modules.LdapLoginModule;
import de.hf.dac.security.auth.modules.NoSecurityLoginModule;
import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.config.JaasRealm;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.HashMap;
import java.util.Map;

@Designate(ocd = DacJaasRealmConfiguration.class)
@Component(immediate = true, name="DAC.DacJaasRealmService")
public class DacJaasRealmService implements JaasRealm
{
    public static final String REALM_NAME = "karaf";

    private AppConfigurationEntry[] configEntries;
    private DacJaasRealmConfiguration dacJaasRealmConfiguration;
    private int rank = 2;

    @Activate
    public void activate(BundleContext bc, DacJaasRealmConfiguration dacJaasRealmConfiguration)
    {
        this.dacJaasRealmConfiguration=dacJaasRealmConfiguration;

        if(dacJaasRealmConfiguration.auth_mode().equals("ldap")){
            configEntries = new AppConfigurationEntry[2];
            addLdapAuthentificationModule(bc);
            addLdapAuthorisationModule(bc);

        } else if (dacJaasRealmConfiguration.auth_mode().equals("off")) {
            configEntries = new AppConfigurationEntry[1];
            addNoSecurityLoginModule(bc);
        }else{
            //use karaf auth
            rank=0;
        }

    }

    private void addNoSecurityLoginModule(BundleContext bc) {
        Map<String, Object> options = new HashMap<>();
        configEntries[0] = new AppConfigurationEntry(ProxyLoginModule.class.getName(),
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);

        // actual LoginModule class name will be passed using the options object

        options.put(ProxyLoginModule.PROPERTY_MODULE, NoSecurityLoginModule.class.getName());

        // put bundle id of the LoginModule and bundlecontext of it
        // (in this case, it is the same bundle)
        // This is a neat trick to adapt to OSGI classloader

        long bundleId = bc.getBundle().getBundleId();
        options.put(ProxyLoginModule.PROPERTY_BUNDLE, String.valueOf(bundleId));
        options.put(BundleContext.class.getName(), bc);

        // add extra options if needed; for example, karaf encryption
        // ....


    }

    private void addLdapAuthentificationModule(BundleContext bc) {
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

        options.put("java.naming.provider.url", dacJaasRealmConfiguration.auth_ldap_url());
        options.put("bindDN", dacJaasRealmConfiguration.auth_ldap_user());
        options.put("bindCredential",dacJaasRealmConfiguration.auth_ldap_credentials());
        options.put("baseCtxDN", dacJaasRealmConfiguration.auth_ldap_baseCtxDN());
        options.put("searchTimeLimit", "30000");
        options.put("baseFilter", "(sAMAccountName={0})");
        options.put("password-stacking", "useFirstPass");
        options.put("storePass", "true");
        options.put("useFirstPass", "false");
        options.put("rolesCtxDN", dacJaasRealmConfiguration.auth_ldap_rolesCtxDN());
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

    private void addLdapAuthorisationModule(BundleContext bc) {
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

        options.put("java.naming.provider.url", dacJaasRealmConfiguration.role_ldap_url());
        options.put("bindDN", dacJaasRealmConfiguration.role_ldap_user());
        options.put("bindCredential", dacJaasRealmConfiguration.role_ldap_credentials());
        options.put("baseCtxDN", dacJaasRealmConfiguration.role_ldap_baseCtxDN() );
        options.put("baseFilter", "(dzuid={0})");
        options.put("rolesCtxDN", dacJaasRealmConfiguration.role_ldap_rolesCtxDN());
        options.put("roleFilter", "(uniqueMember={1})");
        options.put("roleAttributeID", "cn");
        options.put("permissionAttributeID", "dzpermissions");
        options.put("searchScope", "ONELEVEL_SCOPE");
        options.put("allowEmptyPasswords", "false");
        options.put("useFirstPass", "true");
        options.put("permissionRoleMapping", dacJaasRealmConfiguration.role_mappings());
        options.put("userAttributes", dacJaasRealmConfiguration.role_userAttributes());
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
        return rank;
    }

}