/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.security;


import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
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
 *
 * @author xn01598
 */
public class LdapLoginModule {
    protected Map<String, ?> options;
    private Principal identity = null;
    private String roleCtxDN;
    private String roleFilter;
    private String roleNameAttributeID;
    private String permissionAttributeID;
    private String roleAttributeID;
    private boolean roleAttributeIsDN;
    private Group roles = null;
    private Group permissions = null;
    private final String principalClassName = "de.hf.marketdataprovider.security.CompanyPrincipal";
    
    public boolean login(String username, String password) throws LoginException {
        
        if (identity == null) {
            try {
                identity = createIdentity(username);
            } catch (Exception e) {
                /*if (debug) {
                    LOG.info("Failed to create principal for user '{}'", username);
                }*/
                throw new LoginException("Failed to create principal: " + e.getMessage());
            }

            if (!validatePassword(password)) {
                throw new FailedLoginException("Password incorrect/Password required");
            }
            char[] credential = password.toCharArray();

            roles = new SimpleGroup("Roles");
            permissions = new SimpleGroup("Permissions");
            try {
                createLdapInitContext(identity.getName(), credential);
            } catch (Exception e) {
                //LOG.error("Failed LDAP login.", e);
                throw new FailedLoginException(e.getMessage());
            }

        }

        return true;
    }
    
    private boolean validatePassword(String password) throws LoginException {
        boolean valid = false;

        if (password != null) {
            // See if this is an empty password that should be disallowed
            if (password.length() == 0) {
                 return false;
            }

            try {
                // Validate the password by trying to create an initial context
                //das funktioniert leider nicht da keine zugriffs berechtigung existiert auf die pw. 
                //im lisa code wird dieser teil auch nie ausgeführt, da die prüfung bereits an anderer stelle passiert
                //valid = createLdapInitContext(identity.getName(), password);
            } catch (Exception e) {
                LoginException le = new LoginException("Could not authenticate user");
                le.initCause(e);
                throw le;
            }
        }
        return valid;
    }
    
    private boolean createLdapInitContext(String username, Object credential) throws Exception {
        String bindDN = "uid=eigenentwicklungen_lesend,ou=Eigenentwicklungen,ou=Special Users,dc=dzbank,dc=vrnet";
        String bindCredential = "eigen310511";

        String baseDN = "ou=People,dc=dzbank,dc=vrnet";
        String baseFilter = "(dzuid={0})";
        roleFilter = "(uniqueMember={1})";
        roleAttributeID = "cn";

        permissionAttributeID = "dzpermissions";
        // Is user's role attribute a DN or the role name?
        roleAttributeIsDN = false;
        roleNameAttributeID = "name";

        roleCtxDN = "ou=Rollen,ou=Test,ou=PoET,ou=Eigenentwicklungen,ou=Anwendungen,dc=dzbank,dc=vrnet";
        int recursion = 0;
        int searchTimeLimit = 10000;

        String scope = "ONELEVEL_SCOPE";
        int searchScope = 1;

        // Get the admin context for searching
        InitialLdapContext ctx = null;
        try {

            ctx = constructInitialLdapContext(bindDN, bindCredential);



            String userDN = bindDNAuthentication(ctx, username, credential, baseDN, baseFilter, searchTimeLimit);

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
    
    private  InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException {
        Properties env = new Properties();
        //jboss.security.security_domain=POETITEB, 
        env.put("permissionAttributeID", "dzpermissions");
        env.put("debug", "true");
        env.put("searchScope", "ONELEVEL_SCOPE");
        env.put("principalClass", principalClassName);
        env.put("baseCtxDN", "People,dc=dzbank,dc=vrnet");
        env.put("roleAttributeID", "cn");
        env.put("allowEmptyPasswords", "false");
        env.put("roleFilter", "dzpermissions");
        env.put("permissionAttributeID", "(uniqueMember={1})");
        env.put("rolesCtxDN", "ou=Rollen,ou=Test,ou=PoET,ou=Eigenentwicklungen,ou=Anwendungen,dc=dzbank,dc=vrnet");
        env.put("useFirstPass", "dzpermissions");
        env.put("permissionAttributeID", "true");
        env.put("baseFilter", "(dzuid={0})");
        env.put("java.naming.provider.url", "ldap://dfvvpldps1.dzbank.vrnet");
        env.put("bindCredential", "eigen310511");
        env.put("bindDN", "uid=eigenentwicklungen_lesend,ou=Eigenentwicklungen,ou=Special Users,dc=dzbank,dc=vrnet");
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
        String protocol = env.getProperty(Context.SECURITY_PROTOCOL);
        String providerURL = "ldap://dfvvpldps1.dzbank.vrnet";

        env.setProperty(Context.PROVIDER_URL, providerURL);

        if (dn != null) {
            env.setProperty(Context.SECURITY_PRINCIPAL, dn);
        }
        if (credential != null) {
            env.put(Context.SECURITY_CREDENTIALS, credential);
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
    
    protected String bindDNAuthentication(InitialLdapContext ctx, String user, Object credential, String baseDN, String filter, int limit)
        throws NamingException {
        SearchControls searchConstraints = new SearchControls();
        searchConstraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchConstraints.setTimeLimit(limit);

        boolean extendedUserAttrs = (identity instanceof CompanyPrincipal);
        String attrs[];

        // if we're using a CompanyPrincipal, gather additional info
        if (extendedUserAttrs) {
            String definedUserAttributes = null;
            if (definedUserAttributes == null) {
                // use default attributes
                attrs = new String[] { "sn", "givenName", "mail", "dzdepartment", "dzkst", "cn", "dzuid", "dzanrede", "dzarbeitsplatz", "dzani",
                    "dzbereich", "dzfaxNumber", "dztelNumber", "dzlokkurz", "dzraum", "dzetage", "dzgebaeude", "dzpnrleit", "l", "dzkstdn",
                    "dzksthierarchy", "dzdepartmentlong", "dzgeschlecht", "initials", "employeeNumber", "uid" };
            } else {
                attrs = definedUserAttributes.split(",");
            }
        } else {
            // return no attributes
            attrs = new String[0];
        }
        searchConstraints.setReturningAttributes(attrs);

        NamingEnumeration<SearchResult> results = null;

        Object[] filterArgs = { user };
        results = ctx.search(baseDN, filter, filterArgs, searchConstraints);
        if (results.hasMore() == false) {
            results.close();
            throw new NamingException("Search of baseDN(" + baseDN + ") found no matches");
        }

        SearchResult sr = results.next();
        String name = sr.getName();
        String userDN = null;
        if (sr.isRelative() == true) {
            userDN = name + "," + baseDN;
        } else {
            throw new NamingException("Can't follow referal for authentication: " + name);
        }

        if (extendedUserAttrs) {
            parseUser(sr, userDN, (CompanyPrincipal) identity);
        }

        results.close();
        results = null;

        return userDN;
    }
    
    private void parseUser(SearchResult result, String userDN, CompanyPrincipal principal) throws NamingException {
        principal.setDn(userDN);

        NamingEnumeration<? extends Attribute> ar = result.getAttributes().getAll();
        while (ar.hasMoreElements()) {
            Attribute att = ar.next();
            String id = att.getID();

            if ("sn".equals(id)) {
                principal.setLastName((String) att.get());
            } else if ("givenName".equals(id)) {
                principal.setFirstName((String) att.get());
            } else if ("mail".equals(id)) {
                principal.setEmail((String) att.get());
            } else if ("dzdepartment".equals(id)) {
                principal.setDepartment((String) att.get());
            } else if ("dzkst".equals(id)) {
                principal.setCostCenter((String) att.get());
            } else if ("cn".equals(id)) {
                principal.setCn((String) att.get());
            } else if ("dzuid".equals(id)) {
                principal.setDzuid((String) att.get());
            } else if ("dzanrede".equals(id)) {
                principal.setDzanrede((String) att.get());
            } else if ("dzarbeitsplatz".equals(id)) {
                principal.setDzarbeitsplatz((String) att.get());
            } else if ("dzani".equals(id)) {
                principal.setDzani((String) att.get());
            } else if ("dzbereich".equals(id)) {
                principal.setDzbereich((String) att.get());
            } else if ("dzfaxNumber".equals(id)) {
                principal.setDzfaxNumber((String) att.get());
            } else if ("dztelNumber".equals(id)) {
                principal.setDztelNumber((String) att.get());
            } else if ("dzlokkurz".equals(id)) {
                principal.setDzlokkurz((String) att.get());
            } else if ("dzraum".equals(id)) {
                principal.setDzraum((String) att.get());
            } else if ("dzetage".equals(id)) {
                principal.setDzetage((String) att.get());
            } else if ("dzgebaeude".equals(id)) {
                principal.setDzgebaeude((String) att.get());
            } else if ("dzpnrleit".equals(id)) {
                principal.setDzpnrleit((String) att.get());
            } else if ("l".equals(id)) {
                principal.setLocation((String) att.get());
            } else if ("dzkstdn".equals(id)) {
                principal.setDzkstdn((String) att.get());
            } else if ("dzksthierarchy".equals(id)) {
                principal.setDzksthierarchy((String) att.get());
            } else if ("dzdepartmentlong".equals(id)) {
                principal.setDzdepartmentlong((String) att.get());
            } else if ("dzgeschlecht".equals(id)) {
                principal.setDzgeschlecht((String) att.get());
            } else if ("initials".equals(id)) {
                principal.setInitials((String) att.get());
            } else if ("employeeNumber".equals(id)) {
                principal.setEmployeeNumber((String) att.get());
            } else if ("uid".equals(id)) {
                principal.setUid((String) att.get());
            }
        }
    }
    
    protected void rolesSearch(InitialLdapContext ctx, SearchControls constraints, String user, String userDN, int recursionMax, int nesting)
        throws NamingException {
        Object[] filterArgs = { user, userDN };
        NamingEnumeration<SearchResult> results = ctx.search(roleCtxDN, roleFilter, filterArgs, constraints);
        try {
            while (results.hasMore()) {
                SearchResult sr = results.next();
                String dn = canonicalize(sr.getName());
                if (nesting == 0 && roleAttributeIsDN && roleNameAttributeID != null && permissionAttributeID != null) {
                    // Check the top context for role names
                    String[] attrNames = { roleNameAttributeID, permissionAttributeID };
                    Attributes result2 = ctx.getAttributes(dn, attrNames);
                    Attribute roles2 = result2.get(roleNameAttributeID);

                    if (roles2 != null) {
                        for (int m = 0; m < roles2.size(); m++) {
                            String roleName = (String) roles2.get(m);
                            addRole(roleName);
                        }
                    }
                    addPermission(result2.get(permissionAttributeID));

                }

                // Query the context for the roleDN values
                String[] attrNames = { roleAttributeID, permissionAttributeID };
                Attributes result = ctx.getAttributes(dn, attrNames);
                if (result != null && result.size() > 0) {
                    Attribute tmpRoles = result.get(roleAttributeID);
                    for (int n = 0; n < tmpRoles.size(); n++) {
                        String roleName = (String) tmpRoles.get(n);
                        if (roleAttributeIsDN) {
                            // Query the roleDN location for the value of
                            // roleNameAttributeID
                            String roleDN = roleName;
                            String[] returnAttribute = { roleNameAttributeID };
                            try {
                                Attributes result2 = ctx.getAttributes(roleDN, returnAttribute);
                                Attribute roles2 = result2.get(roleNameAttributeID);
                                if (roles2 != null) {
                                    for (int m = 0; m < roles2.size(); m++) {
                                        roleName = (String) roles2.get(m);
                                        addRole(roleName);
                                    }
                                }
                                addPermission(result2.get(permissionAttributeID));

                            } catch (NamingException e) {
                                //LOG.info("", e);
                            }
                        } else {
                            // The role attribute value is the role name
                            addRole(roleName);
                            addPermission(result.get(permissionAttributeID));
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
        String result = searchResult;
        int len = searchResult.length();

        if (searchResult.endsWith("\"")) {
            result = searchResult.substring(0, len - 1) + "," + roleCtxDN + "\"";
        } else {
            result = searchResult + "," + roleCtxDN;
        }
        return result;
    }
    
    private void addRole(String theRoleName) {
        if (theRoleName != null) {
            /*if (debug) {
                LOG.info("Adding role: {}", theRoleName);
            }*/

            try {
                roles.addMember(new SimplePrincipal(theRoleName));
            } catch (Exception e) {
                //LOG.info("", e);
            }
        }
    }
    
    private void addPermission(Attribute thePermissions) {
        if (thePermissions != null) {
            for (int m = 0; m < thePermissions.size(); m++) {
                try {
                    String permission = (String) thePermissions.get(m);
                    /*if (debug) {
                        LOG.info("Adding permission: {}", permission);
                    }*/

                    permissions.addMember(new SimplePrincipal(permission));
                } catch (Exception e) {
                    //LOG.info("", e);
                }
            }
        }
    }
    
    protected Principal createIdentity(String name) throws Exception {
        Principal p = null;
        if (principalClassName == null) {
            p = new SimplePrincipal(name);
        } else {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = loader.loadClass(principalClassName);
            Class<?>[] ctorSig = { String.class };
            Constructor<?> ctor = clazz.getConstructor(ctorSig);
            Object[] ctorArgs = { name };
            p = (Principal) ctor.newInstance(ctorArgs);
        }
        return p;
    }
}
