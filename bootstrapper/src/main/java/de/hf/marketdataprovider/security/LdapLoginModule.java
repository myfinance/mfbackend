/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.security;


import java.lang.reflect.Constructor;
import java.security.Principal;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

/**
 * Special Module to authorize via LDAP in the XXXX-Company
 * @author xn01598
 */
public class LdapLoginModule {
    private Principal identity = null;
    private final String principalClassName = "de.hf.marketdataprovider.security.CompanyPrincipal";
    private LdapProperties prop;

    public LdapLoginModule(LdapProperties prop) {
        this.prop = prop;
    }

    public boolean login(String username) throws LoginException {
        
        if (identity == null) {
            try {
                identity = createIdentity(username);
            } catch (Exception e) {
                throw new LoginException("Failed to create principal: " + e.getMessage());
            }

            try {
                createLdapInitContext(identity.getName());
            } catch (Exception e) {
                throw new FailedLoginException(e.getMessage());
            }

        }

        return true;
    }

    private boolean createLdapInitContext(String username) throws Exception {

        int recursion = 0;
        int searchTimeLimit = 10000;

        String scope = "ONELEVEL_SCOPE";
        int searchScope = 1;

        // Get the admin context for searching
        InitialLdapContext ctx = null;
        try {

            ctx = constructInitialLdapContext();



            String userDN = bindDNAuthentication(ctx, username, prop.getBaseCtxDN(), prop.getBaseFilter(), searchTimeLimit);

            // Query for roles matching the role filter
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(searchScope);
            constraints.setReturningAttributes(new String[0]);
            constraints.setTimeLimit(searchTimeLimit);
            /*if (debug) {
                LOG.info("Querying roles");
            }*/
            rolesSearch(ctx, constraints, username, userDN, recursion, 0);
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }
        return true;
    }    
    
    private  InitialLdapContext constructInitialLdapContext() throws NamingException {
        Properties env = new Properties();
        env.put("permissionAttributeID", prop.getPermissionAttributeID());
        env.put("debug", "true");
        env.put("searchScope", "ONELEVEL_SCOPE");
        env.put("principalClass", principalClassName);
        env.put("baseCtxDN", prop.getBaseCtxDN());
        env.put("roleAttributeID", prop.getRoleAttributeID());
        env.put("allowEmptyPasswords", "false");
        env.put("roleFilter", prop.getRoleFilter());
        env.put("permissionAttributeID", prop.getPermissionAttributeID());
        env.put("rolesCtxDN", prop.getRoleCtxDN());
        env.put("useFirstPass", prop.getPermissionAttributeID());
        env.put("permissionAttributeID", "true");
        env.put("baseFilter", prop.getBaseFilter());
        env.put("java.naming.provider.url", prop.getUrl());
        env.put("bindCredential", prop.getBindCredential());
        env.put("bindDN", prop.getBindDN());
        // Set defaults for key values if they are missing
        String factoryName = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if (factoryName == null) {
            factoryName = "com.sun.jndi.ldap.LdapCtxFactory";
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY, factoryName);
        }
        String authType = env.getProperty(Context.SECURITY_AUTHENTICATION);
        if (authType == null) {
            env.setProperty(Context.SECURITY_AUTHENTICATION, "simple");
        }

        env.setProperty(Context.PROVIDER_URL, prop.getUrl());

        if (prop.getBindDN() != null) {
            env.setProperty(Context.SECURITY_PRINCIPAL, prop.getBindDN());
        }
        if (prop.getBindCredential() != null) {
            env.put(Context.SECURITY_CREDENTIALS, prop.getBindCredential());
        }
        InitialLdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(env, null);
        } catch (Exception e) {
            //LOG.error("Failed creating initial LDAP context.", e);
            if (e instanceof NamingException) {
                throw (NamingException) e;
            }
        }

        //LOG.info("Initial LDAP Context constructed.");

        return ctx;

    }
    
    protected String bindDNAuthentication(InitialLdapContext ctx, String user, String baseDN, String filter, int limit)
        throws NamingException {
        SearchControls searchConstraints = new SearchControls();
        searchConstraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchConstraints.setTimeLimit(limit);

        boolean extendedUserAttrs = (identity instanceof CompanyPrincipal);
        String attrs[];

        // if we're using a CompanyPrincipal, gather additional info
        if (extendedUserAttrs) {
            attrs = prop.getUserAttr().stream().toArray(String[]::new);
        } else {
            // return no attributes
            attrs = new String[0];
        }
        searchConstraints.setReturningAttributes(attrs);

        Object[] filterArgs = { user };
        NamingEnumeration<SearchResult> results = ctx.search(baseDN, filter, filterArgs, searchConstraints);
        if (!results.hasMore()) {
            results.close();
            throw new NamingException("Search of baseDN(" + baseDN + ") found no matches");
        }

        SearchResult sr = results.next();
        String name = sr.getName();
        String userDN;
        if (sr.isRelative()) {
            userDN = name + "," + baseDN;
        } else {
            throw new NamingException("Can't follow referal for authentication: " + name);
        }

        if (extendedUserAttrs) {
            parseUser(sr, userDN, (CompanyPrincipal) identity);
        }

        results.close();

        return userDN;
    }
    
    private void parseUser(SearchResult result, String userDN, CompanyPrincipal principal) throws NamingException {
        principal.setDn(userDN);

        NamingEnumeration<? extends Attribute> ar = result.getAttributes().getAll();
        while (ar.hasMoreElements()) {
            Attribute att = ar.next();
            String id = att.getID();
            principal.addProperty(id,(String) att.get());
        }
    }
    
    protected void rolesSearch(InitialLdapContext ctx, SearchControls constraints, String user, String userDN, int recursionMax, int nesting)
        throws NamingException {
        Object[] filterArgs = { user, userDN };
        NamingEnumeration<SearchResult> results = ctx.search(prop.getRoleCtxDN(), prop.getRoleFilter(), filterArgs, constraints);
        try {
            while (results.hasMore()) {
                SearchResult sr = results.next();
                String dn = canonicalize(sr.getName());
                if (nesting == 0 && prop.isRoleAttributeIsDN() && prop.getRoleAttributeID() != null && prop.getPermissionAttributeID() != null) {
                    // Check the top context for role names
                    String[] attrNames = { prop.getRoleAttributeID(), prop.getPermissionAttributeID() };
                    Attributes result2 = ctx.getAttributes(dn, attrNames);
                    Attribute roles2 = result2.get(prop.getRoleAttributeID());

                    if (roles2 != null) {
                        for (int m = 0; m < roles2.size(); m++) {
                            String roleName = (String) roles2.get(m);
                            ((SimplePrincipal)identity).addRole(roleName);
                        }
                    }
                    ((SimplePrincipal)identity).addPermission(result2.get(prop.getPermissionAttributeID()));

                }

                // Query the context for the roleDN values
                String[] attrNames = { prop.getRoleAttributeID(), prop.getPermissionAttributeID() };
                Attributes result = ctx.getAttributes(dn, attrNames);
                if (result != null && result.size() > 0) {
                    Attribute tmpRoles = result.get(prop.getRoleAttributeID());
                    for (int n = 0; n < tmpRoles.size(); n++) {
                        String roleName = (String) tmpRoles.get(n);
                        if (prop.isRoleAttributeIsDN()) {
                            // Query the roleDN location for the value of
                            // roleNameAttributeID
                            String roleDN = roleName;
                            String[] returnAttribute = { prop.getRoleAttributeID() };
                            try {
                                Attributes result2 = ctx.getAttributes(roleDN, returnAttribute);
                                Attribute roles2 = result2.get(prop.getRoleAttributeID());
                                if (roles2 != null) {
                                    for (int m = 0; m < roles2.size(); m++) {
                                        roleName = (String) roles2.get(m);
                                        ((SimplePrincipal)identity).addRole(roleName);
                                    }
                                }
                                ((SimplePrincipal)identity).addPermission(result2.get(prop.getPermissionAttributeID()));

                            } catch (NamingException e) {
                                //LOG.info("", e);
                            }
                        } else {
                            // The role attribute value is the role name
                            ((SimplePrincipal)identity).addRole(roleName);
                            ((SimplePrincipal)identity).addPermission(result.get(prop.getPermissionAttributeID()));
                        }
                    }
                }

                if (nesting < recursionMax) {
                    rolesSearch(ctx, constraints, user, dn, recursionMax, nesting + 1);
                }
            }
        } finally {
            if (results != null) {
                results.close();
            }
        }

    }
    
        // JBAS-3438 : Handle "/" correctly
    private String canonicalize(String searchResult) {
        String result;
        int len = searchResult.length();

        if (searchResult.endsWith("\"")) {
            result = searchResult.substring(0, len - 1) + "," + prop.getRoleCtxDN() + "\"";
        } else {
            result = searchResult + "," + prop.getRoleCtxDN();
        }
        return result;
    }
    

    protected Principal createIdentity(String name) throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?> clazz = loader.loadClass(principalClassName);
        Class<?>[] ctorSig = { String.class };
        Constructor<?> ctor = clazz.getConstructor(ctorSig);
        Object[] ctorArgs = { name };
        return (Principal) ctor.newInstance(ctorArgs);
    }

    public Principal getIdentity() {
        return identity;
    }
}
