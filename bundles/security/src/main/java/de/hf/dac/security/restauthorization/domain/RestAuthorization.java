/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RestAuthorization.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 12.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.restauthorization.domain;

import de.hf.dac.api.security.AuthorizationEntry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//ToDo Logic von Domain-Klasse trennen und nach api statt AuthorizationEntry
@Entity
@Table(name="restauthorization" )
public class RestAuthorization  implements java.io.Serializable, AuthorizationEntry {

    @Id
    @Column(name = "restApp", nullable = false,length = 25)
    private String restApp;

    @Id
    @Column(name = "resource", nullable = false,length = 25)
    private String resource;

    @Id
    @Column(name = "restIdPattern", nullable = false,length = 50)
    private String restIdPattern;

    @Id
    @Column(name = "restOpType", nullable = false,length = 10)
    private String restOpType;

    @Column(name = "permissions", nullable = false,length = 200)
    private String permissions;

    @Column(name = "users", nullable = false,length = 200)
    private String users;

    @Column(name = "description", nullable = false,length = 100)
    private String description;

    @Id
    @Column(name = "operations", nullable = false,length = 200)
    private String operations;


    public RestAuthorization() {

    }

    public String getResource() {
        return resource;
    }

    @Override
    public String getRestIdPattern() {
        return restIdPattern;
    }

    public String getRestOpType() {
        return restOpType;
    }

    public String getPermissions() {
        return permissions;
    }
    public String getUsers() {
        return users;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getOperations() {
        return operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestAuthorization)) return false;
        RestAuthorization that = (RestAuthorization) o;
        return Objects.equals(getResource(), that.getResource()) &&
            Objects.equals(getRestIdPattern(), that.getRestIdPattern()) &&
            Objects.equals(getRestOpType(), that.getRestOpType()) &&
            Objects.equals(getPermissions(), that.getPermissions()) &&
            Objects.equals(getUsers(), that.getUsers()) &&
            Objects.equals(getDescription(), that.getDescription()) &&
            Objects.equals(getOperations(), that.getOperations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResource(), getRestIdPattern(), getRestOpType(), getPermissions(), getDescription(), getOperations());
    }

    @Override
    @Transient
    public List<String> listOperations() {
        // Delimiter:
        // zero or more whitespace, a literal comma, zero or more whitespace
        List<String> items = Arrays.asList(operations.split("\\s*,\\s*"));
        return items;
    }

    @Override
    @Transient
    public List<String> listPermissions() {
        // Delimiter:
        // zero or more whitespace, a literal comma, zero or more whitespace
        List<String> items = Arrays.asList(permissions.split("\\s*,\\s*"));
        return items;
    }

    @Override
    @Transient
    public List<String> listUsers() {
        // Delimiter:
        // zero or more whitespace, a literal comma, zero or more whitespace
        List<String> items = Arrays.asList(users.split("\\s*,\\s*"));
        return items;
    }
}
