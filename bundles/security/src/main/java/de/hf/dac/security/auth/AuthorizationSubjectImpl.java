/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AuthorizationSubjectImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth;

import de.hf.dac.api.security.AuthorizationSubject;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Defines how to get the internal Roles from the generic subject
 */
public class AuthorizationSubjectImpl implements AuthorizationSubject {

    private final List<String> permissions;
    private final List<String> internalRoles;
    private final List<String> roles;
    private final CompanyPrincipal principal;

    public AuthorizationSubjectImpl(Subject subject) {

        this.internalRoles = subject.getPrincipals(RolePrincipal.class).stream().map(x -> x.getName()).collect(Collectors.toList());

        if(subject.getPrincipals(SimpleGroup.class).contains(SimpleGroup.class)){
            Optional<SimpleGroup> simplePermissionGroup = subject.getPrincipals(SimpleGroup.class).stream()
                .filter(x -> x.getName().equals("Permissions"))
                .findFirst();
            if(simplePermissionGroup.isPresent()){
                this.permissions = Collections.list(simplePermissionGroup.get().members())
                        .stream()
                        .map(x -> x.getName())
                        .collect(Collectors.toList());
            } else {
                permissions = null;
            }

            Optional<SimpleGroup> simpleRoleGroup = subject.getPrincipals(SimpleGroup.class).stream()
                .filter(x -> x.getName().equals("Roles"))
                .findFirst();
            if(simpleRoleGroup.isPresent()){
                this.roles = Collections.list(simpleRoleGroup.get().members())
                    .stream()
                    .map(x -> x.getName())
                    .collect(Collectors.toList());
            } else {
                roles = null;
            }

            this.principal = subject.getPrincipals(CompanyPrincipal.class).iterator().next();
        } else {
            permissions=null;
            roles=null;
            principal=new CompanyPrincipal("noUser");
        }



    }

    @Override
    public List<String> getPermissions() {

        return permissions;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public List<String> getInternalRoles() {
        return internalRoles;
    }

    @Override
    public CompanyPrincipal getPrincipal() {
        return principal;
    }
}
