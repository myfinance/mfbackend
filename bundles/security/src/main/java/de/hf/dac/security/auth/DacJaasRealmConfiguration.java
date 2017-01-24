/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DacJaasRealmConfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 23.01.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Dac Jaas Realm Configuration")
public @interface DacJaasRealmConfiguration {
    String auth_mode() default "karaf";
    String auth_ldap_url();
    String auth_ldap_baseCtxDN();
    String auth_ldap_rolesCtxDN();
    String auth_ldap_user();
    String auth_ldap_credentials();
    String role_ldap_user();
    String role_ldap_credentials();
    String role_ldap_url();
    String role_ldap_baseCtxDN();
    String role_ldap_rolesCtxDN();
    //to map the persions from the ldap to internal roles. for karaf admin and view are necessary
    String role_mappings();
    String role_userAttributes();
}


