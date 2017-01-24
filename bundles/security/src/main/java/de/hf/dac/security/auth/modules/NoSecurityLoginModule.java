/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : NoSecurityLoginModule.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 24.01.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth.modules;

import org.apache.karaf.jaas.boot.principal.RolePrincipal;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NoSecurityLoginModule implements LoginModule {
    protected Subject subject;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
    }

    @Override
    public boolean login() throws LoginException {
        return true;
    }

    @Override
    public boolean commit() throws LoginException {

        Set<Principal> plainRoleRightPrincipals = new HashSet<>();
        plainRoleRightPrincipals.add(new RolePrincipal("admin"));
        subject.getPrincipals().addAll(plainRoleRightPrincipals);
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        return true;
    }
}
