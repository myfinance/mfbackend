/** ----------------------------------------------------------------------------
 *
 * ---                                 ---
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

package de.hf.marketdataprovider.springcommon.config.security;

import de.hf.marketdataprovider.springcommon.security.CompanyPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;

@Configuration
@Profile({"localsecurity"})
public class LocalSecurityConfig extends AbsSecurityConfig {

    //gegen lokalen ldap
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .ldapAuthentication()
            .userDetailsContextMapper(userDetailsContextMapper())
            .userDnPatterns("uid={0},ou=people")
            .groupSearchBase("ou=apps")
            .groupRoleAttribute("cn").groupSearchFilter("uniqueMember={0}")
            .contextSource().ldif("classpath:test-server.ldif").root("dc=hf,dc=org");
    }

    /**
     * Role-mapping if enabled(useRoleMapping in properties). Necessary to handle new security-Requirement more flexible and without code change.
     * Internal Roles/Permissions should be more specific to simply map new external roles (e.G. a new user group in LDAP) instead of changing
     * annotations in code
     */
    @Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new LdapUserDetailsMapper() {
            @Override
            public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                return mapRoles(new CompanyPrincipal(username), authorities);
            }
        };
    }
}
