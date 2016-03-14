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

import de.hf.marketdataprovider.security.LdapLoginModule;
import de.hf.marketdataprovider.security.SimplePrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;

@Configuration
@Profile({"serversecurity"})
public class LdapSecurityConfig  extends GlobalAuthenticationConfigurerAdapter {

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        //aus den Parametern werden noch nicht die richtigen werte gelesen
        /*auth
            .ldapAuthentication()
            .userSearchFilter(userSearchFilter)
            .userSearchBase(userSearchBase)
            .groupSearchFilter(groupSearchFilter)
            .groupSearchBase(groupSearchBase)
            .groupRoleAttribute(groupRoleAttribute)
            .contextSource().url(ldapurl)
            .root(root)
            .managerDn(managerDn)
            .managerPassword(managerPassword);*/
        auth
            .ldapAuthentication()
            .userDetailsContextMapper(userDetailsContextMapper())
            .userSearchFilter("(sAMAccountName={0})")
            .userSearchBase("ou=Accounts,dc=dzag,dc=vrnet")
            .groupSearchFilter("(sAMAccountName={0})")
            .groupSearchBase("ou=FR,ou=Accounts,dc=dzag,dc=vrnet")
            .groupRoleAttribute("memberOf")
            .contextSource().url("ldap://dzag.vrnet:389")
            .root("dc=dzag,dc=vrnet")
            .managerDn("cn=XNMEE01,ou=FR,ou=Accounts,dc=dzag,dc=vrnet")
            .managerPassword("Ready2Work11#");
    }

    /**
     * Override Standard Userdetail functionality of the Spring-LDAP-Authentication, because the authentication LDAP contains no roles for authorisation.
     * Another LDAP-Server has to be connected for authorisation. The Authorisation-Server is connected via a special class "LdapLoginModule" due to company specific roll and permission mapping
     * @return
     */
    @Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new LdapUserDetailsMapper() {
            @Override
            public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                LdapLoginModule ldap = new LdapLoginModule();
                try {
                    ldap.login(username);
                } catch(Exception e) {
                    System.out.print(e);
                    return null;
                }

                ((SimplePrincipal)ldap.getIdentity()).addRole("ROLE_ADMIN");

                return (SimplePrincipal)ldap.getIdentity();
            }
        };
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
