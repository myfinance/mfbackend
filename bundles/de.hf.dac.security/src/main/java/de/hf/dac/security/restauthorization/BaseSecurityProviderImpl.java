/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseSecurityProviderImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 12.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.restauthorization;


import de.hf.dac.api.base.exceptions.DACException;
import de.hf.dac.api.base.exceptions.DACMsgKey;
import de.hf.dac.api.io.domain.DacRestauthorization;
import de.hf.dac.api.security.AuthorizationEntry;
import de.hf.dac.api.security.AuthorizationSubject;
import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.security.auth.AuthorizationSubjectImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.security.auth.Subject;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseSecurityProviderImpl<
    ACCESS_TYPE extends Enum<ACCESS_TYPE>,
    RESOURCE_LEVEL extends Enum<RESOURCE_LEVEL>>
    implements RootSecurityProvider<ACCESS_TYPE,RESOURCE_LEVEL> {

    // Pair replacement
    private class EntryPair extends AbstractMap.SimpleEntry<ACCESS_TYPE,RESOURCE_LEVEL> {

        public EntryPair(ACCESS_TYPE access_type, RESOURCE_LEVEL resource_level) {
            super(access_type, resource_level);
        }

        public EntryPair(Map.Entry<? extends ACCESS_TYPE, ? extends RESOURCE_LEVEL> entry) {
            super(entry);
        }
    }


    private Map<RESOURCE_LEVEL,List<AuthMatcherImpl>> passThroughAuthMatchers = new HashMap<>();
    private Map<EntryPair,List<AuthMatcherImpl>> rootAuthMatchers = new HashMap<>();
    protected AuthorizationSubject authSubject;

    public abstract EntityManagerFactory getEMFactory();


    @Override
    public void updateAuth() {
        Map<RESOURCE_LEVEL,List<AuthMatcherImpl>> allAuthMatchers = new HashMap<>();

        AbstractMap.SimpleEntry ss = null;

        Map<EntryPair, List<AuthMatcherImpl>> typeSpecificAuthMatchers  =
            new HashMap<>();

        List<RESOURCE_LEVEL> opLevels;
        try {
            opLevels = getDistinctOpLevels(getResourceLevel(), getSystem());
            if(opLevels==null || opLevels.isEmpty()){
                throw new DACException(DACMsgKey.WRONG_SECURITY_CONFIG, "No security Resources found for System:"+getSystem());
            }
        } finally {
            rootAuthMatchers.clear();
            passThroughAuthMatchers.clear();
        }

        try {
            for(ACCESS_TYPE opType : getAccessType().getEnumConstants()) {
                for(RESOURCE_LEVEL level : opLevels) {
                    if(!allAuthMatchers.containsKey(level))
                        allAuthMatchers.put(level, new ArrayList<>());
                    allAuthMatchers.get(level).addAll(getAuthMatchers(getSystem(), level, opType));
                    typeSpecificAuthMatchers.put(new EntryPair(opType,level),getAuthMatchers(getSystem(), level, opType));
                }
            }

        } catch(Exception ex) {
            throw new SecurityException("Can't initialize Authorization matchers",ex);
        } finally {
            passThroughAuthMatchers.clear();
            rootAuthMatchers.clear();
        }

        passThroughAuthMatchers.putAll(allAuthMatchers);
        rootAuthMatchers.putAll(typeSpecificAuthMatchers);
    }

    protected abstract String getSystem();

    protected abstract Class<ACCESS_TYPE> getAccessType();

    protected abstract Class<RESOURCE_LEVEL> getResourceLevel();

    private <T extends Enum<T>> List<T> getDistinctOpLevels(Class<T> clazz, String system) {
        List<T> ret = new ArrayList<>();

        EntityManager em = getEMFactory().createEntityManager();

        Query query = em.createQuery("SELECT DISTINCT(c.resource) FROM DacRestauthorization c WHERE c.restapp = :restApp ");
        query.setParameter("restApp", system);

        List<String> ol = query.getResultList();
        for(String o : ol) {
            ret.add(Enum.valueOf(clazz,o));
        }
        return ret;
    }

    private void setUserPermissions(Subject subject){
        authSubject = new AuthorizationSubjectImpl(subject);
    }

    protected List<String> getPermissions(){
        return authSubject.getInternalRoles();
    }

    protected String getUser(){
        return authSubject.getPrincipal().getName();
    }

    @Override
    public boolean isOperationAllowed(ACCESS_TYPE opType, RESOURCE_LEVEL opLevel, String resourceId, Subject subject, String operationId) {
        try {
            setUserPermissions(subject);
            List<AuthMatcherImpl> impls = rootAuthMatchers.get(new EntryPair(opType,opLevel));

            Set<String> allowedPermissions = new HashSet<>();
            Set<String> allowedUsers = new HashSet<>();
            for(AuthMatcherImpl impl : impls) {
                allowedPermissions.addAll(impl.getPossiblePermissions(resourceId,operationId));
                allowedUsers.addAll(impl.getPossibleUsers(resourceId,operationId));
            }

            Set<String> availablePermissions = new HashSet<>(getPermissions());

            availablePermissions.retainAll(allowedPermissions);

            return !(availablePermissions.isEmpty() && DoesNotContain(allowedUsers,  getUser()));
        } catch(Exception ex) {
            throw new SecurityException("Can't check authorization",ex);

        }
    }

    private static boolean DoesNotContain(Set<String> ss, String s) {
        return !(ss.contains(s.toLowerCase()) || ss.contains(s.toUpperCase()));
    }

    @Override
    public boolean isPassthroughAllowed(RESOURCE_LEVEL opLevel, String resourceId, Subject subject) {
        try {
            List<AuthMatcherImpl> impls = passThroughAuthMatchers.get(opLevel);

            Set<String> allowedPermissions = new HashSet<>();
            Set<String> allowedUsers = new HashSet<>();

            for(AuthMatcherImpl impl : impls) {
                allowedPermissions.addAll(impl.getAllPermissions(resourceId));
                allowedUsers.addAll(impl.getAllUsers(resourceId));
            }

            Set<String> availablePermissions = new HashSet<>(getPermissions());

            availablePermissions.retainAll(allowedPermissions);

            return !(availablePermissions.isEmpty() && DoesNotContain(allowedUsers,  getUser()));
        } catch(Exception ex) {
            throw new SecurityException("Can't check authorization",ex);
        }
    }

    private List<AuthMatcherImpl> getAuthMatchers(String system, final RESOURCE_LEVEL opLevel, final ACCESS_TYPE opType) {

        List<AuthMatcherImpl> ret = new ArrayList<>();

        EntityManager em = getEMFactory().createEntityManager();
        Query query = em.createQuery("SELECT c FROM DacRestauthorization c WHERE " +
            "c.resource = :resource and " +
            "c.restapp = :restApp and " +
            "c.restoptype = :restOpType ");
        query.setParameter("restOpType", opType.toString());
        query.setParameter("resource", opLevel.toString());
        query.setParameter("restApp", system);
        List<DacRestauthorization> ol = query.getResultList();
        for(DacRestauthorization o : ol) {
            AuthorizationEntry entry = new AuthorizationEntryImpl(o);
            ret.add(new AuthMatcherImpl(entry.getRestIdPattern(),
                entry.listOperations(),
                entry.listPermissions(),
                entry.listUsers() ,
                entry.getDescription()));
        }
        em.close();
        return ret;
    }

    private class AuthMatcherImpl {

        protected static final String ALL = "all";
        protected static final String NONE = "none";
        final private String idPattern;
        final private List<String> operations = new ArrayList<>();
        final private List<String> forbiddenOperations = new ArrayList<>();
        final private List<String> permissions;
        final private List<String> users;
        final private String description;

        final private Pattern pattern;

        public AuthMatcherImpl(String idPattern, List<String> ops, List<String> permissions, List<String> users, String description) {
            this.idPattern = idPattern;
            for(String op : ops) {
                if(op.startsWith("-")) {
                    forbiddenOperations.add(op.substring(1));
                } else {
                    this.operations.add(op);
                }
            }

            this.permissions = permissions;
            this.description = description;
            this.users = users;

            pattern = Pattern.compile(idPattern);
        }

        public List<String> getPossiblePermissions(String resourceId, String operation) {
            return IsOperationAllowedOnResource(resourceId,operation) ? permissions : new ArrayList<>();
        }

        private boolean IsOperationAllowedOnResource(String resourceId, String operation) {
            Matcher m = pattern.matcher(resourceId);
            return (m.matches() && !forbiddenOperations.contains(operation))
                && ((operations.size() == 1 && operations.get(0).matches(ALL))
                || operations.contains(operation));
        }

        public List<String> getPossibleUsers(String resourceId, String operation) {
            return IsOperationAllowedOnResource(resourceId,operation) ? users : new ArrayList<>();
        }

        public List<String> getAllPermissions(String resourceId) {
            return pattern.matcher(resourceId).matches() ?
                permissions : new ArrayList<>();
        }

        public List<String> getAllUsers(String resourceId) {
            return pattern.matcher(resourceId).matches() ?
                users : new ArrayList<>();
        }
    }


}

