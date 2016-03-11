/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : LocalSecurityConfig.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 11.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
@Profile({"localsecurity"})
public class LocalSecurityConfig extends GlobalAuthenticationConfigurerAdapter {

    //gegen lokalen ldap
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .ldapAuthentication()
            .userDnPatterns("uid={0},ou=people")
            .groupSearchBase("ou=apps")
            //.userSearchFilter("(uid={0})").userSearchBase("ou=people,dc=springframework,dc=org")
            .groupRoleAttribute("cn").groupSearchFilter("uniqueMember={0}")
            //.contextSource().url("ldaps://ldap.xxxx.yyy:636/cn=cw-grpreader,ou=people,dc=xxx,dc=xxxx,dc=xxx")
            .contextSource().ldif("classpath:test-server.ldif").root("dc=hf,dc=org");
    }
}
