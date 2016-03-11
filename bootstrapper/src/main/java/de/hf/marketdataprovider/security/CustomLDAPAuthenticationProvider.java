/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : CustomLDAPAuthenticationProvider.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 09.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomLDAPAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String password = authentication.getCredentials()==null ? "" : authentication.getCredentials().toString();

        LdapLoginModule ldap = new LdapLoginModule();
        try {
            ldap.login(authentication.getName());
        } catch(Exception e) {
                System.out.print(e);
            return null;
        }

        ((SimplePrincipal)ldap.getIdentity()).addRole("ROLE_ADMIN");

        return new UsernamePasswordAuthenticationToken(ldap.getIdentity(), password, ((SimplePrincipal) ldap.getIdentity()).getAuthorities());


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
