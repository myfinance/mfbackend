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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//todo ist es nicht besser immer das Subject weiterzugeben und im BaseSecurityProviderImpl welche sich im selben bundle wie das loginmodule befindet
// aufzulösen wie die internen Rollen aus dem Subject zu laden sind
/**
 * Defines how to get the internal Roles from the generic subject
 */
public class AuthorizationSubjectImpl implements AuthorizationSubject {
    private final Subject subject;

    private final List<String> permissions;
    private final List<String> internalRoles;
    private final List<String> roles;
    private final CompanyPrincipal principal;

    public AuthorizationSubjectImpl(Subject subject) {
        this.subject = subject;

        this.internalRoles = subject.getPrincipals(RolePrincipal.class).stream().map(x -> x.getName()).collect(Collectors.toList());
        this.permissions = Collections.list(subject.getPrincipals(SimpleGroup.class).stream()
            .filter(x -> x.getName().equals("Permissions"))
            .findFirst()
            .get().members())
            .stream()
            .map(x -> x.getName()).collect(Collectors.toList());

        this.roles = Collections.list(subject.getPrincipals(SimpleGroup.class).stream()
            .filter(x -> x.getName().equals("Roles"))
            .findFirst()
            .get().members())
            .stream()
            .map(x -> x.getName()).collect(Collectors.toList());


        this.principal = subject.getPrincipals(CompanyPrincipal.class).iterator().next();

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
