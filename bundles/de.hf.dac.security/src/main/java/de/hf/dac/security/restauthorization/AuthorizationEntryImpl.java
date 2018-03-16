/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AuthorizationEntryImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.08.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.restauthorization;

import de.hf.dac.api.io.domain.DacRestauthorization;
import de.hf.dac.api.security.AuthorizationEntry;

import java.util.Arrays;
import java.util.List;

public class AuthorizationEntryImpl implements AuthorizationEntry{

    private final DacRestauthorization dacRestauthorization;

    public AuthorizationEntryImpl(DacRestauthorization dacRestauthorization){
        this.dacRestauthorization=dacRestauthorization;
    }

    @Override
    public List<String> listUsers() {
        // Delimiter:
        // zero or more whitespace, a literal comma, zero or more whitespace
        List<String> items = Arrays.asList(dacRestauthorization.getUsers().split("\\s*,\\s*"));
        return items;
    }

    @Override
    public List<String> listOperations() {
        // Delimiter:
        // zero or more whitespace, a literal comma, zero or more whitespace
        List<String> items = Arrays.asList(dacRestauthorization.getOperations().split("\\s*,\\s*"));
        return items;
    }

    @Override
    public List<String> listPermissions() {
        // Delimiter:
        // zero or more whitespace, a literal comma, zero or more whitespace
        List<String> items = Arrays.asList(dacRestauthorization.getPermissions().split("\\s*,\\s*"));
        return items;
    }

    @Override
    public String getRestIdPattern() {
        return dacRestauthorization.getRestidpattern();
    }

    @Override
    public String getDescription() {
        return dacRestauthorization.getDescription();
    }
}
