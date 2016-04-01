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

package de.hf.marketdataprovider.config.security;

import de.hf.marketdataprovider.security.LdapLoginModule;
import de.hf.marketdataprovider.security.LdapProperties;
import de.hf.marketdataprovider.security.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LdapSecurityConfig  extends AbsSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(LdapSecurityConfig.class);

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .ldapAuthentication()
            .userDetailsContextMapper(userDetailsContextMapper())
            .userSearchFilter(userSearchFilter)
            .userSearchBase(userSearchBase)
            .groupSearchFilter(groupSearchFilter)
            .groupSearchBase(groupSearchBase)
            .groupRoleAttribute(groupRoleAttribute)
            .contextSource().url(ldapurl)
            .root(root)
            .managerDn(managerDn)
            .managerPassword(managerPassword);
    }

    /**
     * Override Standard Userdetail functionality of the Spring-LDAP-Authentication, because the authentication LDAP contains no roles for authorisation.
     * Another LDAP-Server has to be connected for authorisation. The Authorisation-Server is connected via a special class "LdapLoginModule" due to company specific roll and permission mapping
     *
     * Role-mapping if enabled(useRoleMapping in properties). Necessary to handle new security-Requirement more flexible and without code change.
     * Internal Roles/Permissions should be more specific to simply map new external roles (e.G. a new user group in LDAP) instead of changing
     * annotations in code
     * @return
     */
    @Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new LdapUserDetailsMapper() {
            @Override
            public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                LdapProperties prop = new LdapProperties();
                prop.setUrl(authorisationUrl);
                prop.setBindDN(authorisationManagerDn);
                prop.setBindCredential(authorisationManagerPassword);
                prop.setBaseFilter(authorisationBaseFilter);
                prop.setRoleFilter(authorisationRoleFilter);
                prop.setRoleAttributeID(authorisationRoleAttributeID);
                prop.setPermissionAttributeID(authorisationPermissionAttributeID);
                prop.setRoleCtxDN(authorisationRoleCtxDN);
                prop.setBaseCtxDN(authorisationBaseCtxDN);
                prop.setRoleAttributeIsDN(authorisationRoleAttributeIsDN);
                prop.setUserAttr(authorisationUserAttr);

                LdapLoginModule ldap = new LdapLoginModule(prop);
                try {
                    ldap.login(username);
                } catch(Exception e) {
                    log.error(e.getMessage());
                    return null;
                }

                SimplePrincipal principal = (SimplePrincipal)ldap.getIdentity();

                return mapRoles(principal, principal.getPermissions().memberCollection());
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

    @Value("${authorisationUrl}")
    private String authorisationUrl;

    @Value("${authorisationManagerDn}")
    private String authorisationManagerDn;

    @Value("${authorisationManagerPassword}")
    private String authorisationManagerPassword;

    @Value("${authorisationBaseFilter}")
    private String authorisationBaseFilter;

    @Value("${authorisationRoleFilter}")
    private String authorisationRoleFilter;

    @Value("${authorisationRoleAttributeID}")
    private String authorisationRoleAttributeID;

    @Value("${authorisationPermissionAttributeID}")
    private String authorisationPermissionAttributeID;

    @Value("${authorisationRoleCtxDN}")
    private String authorisationRoleCtxDN;

    @Value("${authorisationBaseCtxDN}")
    private String authorisationBaseCtxDN;

    @Value("${authorisationRoleAttributeIsDN}")
    private Boolean authorisationRoleAttributeIsDN;

    @Value("${authorisationUserAttr}")
    private String authorisationUserAttr;
}
