/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SecurityServiceImpl.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 09.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth;

import de.hf.dac.api.security.AuthorizationSubject;
import de.hf.dac.api.security.SecurityService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Singleton;
import javax.security.auth.Subject;
import java.security.AccessController;

@OsgiServiceProvider(classes = { SecurityService.class })
@Singleton
public class SecurityServiceImpl implements SecurityService{

    @Override
    public AuthorizationSubject getAuthorizationSubject(Subject subject) {
        return new AuthorizationSubjectImpl(Subject.getSubject(AccessController.getContext()));
    }
}
