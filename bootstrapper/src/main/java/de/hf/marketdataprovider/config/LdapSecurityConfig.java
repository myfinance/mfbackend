/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : LdapSecurityConfig.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 11.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.config;

import de.hf.marketdataprovider.security.CustomLDAPAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
@Profile({"serversecurity"})
public class LdapSecurityConfig  extends GlobalAuthenticationConfigurerAdapter {

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth
            .ldapAuthentication()
            .userSearchFilter(userSearchFilter)
            .userSearchBase(userSearchBase)
            .groupSearchFilter(groupSearchFilter)
            .groupSearchBase(groupSearchBase)
            .groupRoleAttribute(groupRoleAttribute)
            .contextSource().url(ldapurl)
            .root(root)
            .managerDn(managerDn)
            .managerPassword(managerPassword);
        auth.authenticationProvider(new CustomLDAPAuthenticationProvider());
    }

    @Value("${userSearchFilter}")
    private String userSearchFilter;

    @Value("${userSearchBase}")
    private String userSearchBase;

    @Value("${groupSearchFilter}")
    private String groupSearchFilter;

    @Value("${groupSearchBase}")
    private String groupSearchBase;

    @Value("${groupRoleAttribute}")
    private String groupRoleAttribute;

    @Value("${ldapurl}")
    private String ldapurl;

    @Value("${root}")
    private String root;

    @Value("${managerDn}")
    private String managerDn;

    @Value("${managerPassword}")
    private String managerPassword;

}
