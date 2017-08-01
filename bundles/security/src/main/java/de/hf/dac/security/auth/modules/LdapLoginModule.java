/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : LdapLoginModule.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth.modules;

import de.hf.dac.security.auth.CompanyPrincipal;
import de.hf.dac.security.auth.SimplePrincipal;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * The <code>LdapLoginModule</code> is an implementation of the <code>LoginModule</code> interface to authenticate users against and extract roles
 * from an LDAP server via the JAAS framework.
 * </p>
 *
 */
public class LdapLoginModule extends BaseLoginModule {
    private static final Logger LOG = LoggerFactory.getLogger(LdapLoginModule.class);

    /* module options */
    private final static String OPT_ALLOW_EMPTY_PWS = "allowEmptyPasswords";
    private final static String OPT_ROLE_GROUP = "roleGroup";
    private final static String OPT_PERMISSION_GROUP = "permissionGroup";

    private static final String OPT_ROLES_CTX_DN = "rolesCtxDN";
    private static final String OPT_ROLE_FILTER = "roleFilter";
    private static final String OPT_ROLE_RECURSION = "roleRecursion";
    private static final String OPT_ROLE_ATTRIBUTE_ID = "roleAttributeID";
    private static final String OPT_PERMISSION_ATTRIBUTE_ID = "permissionAttributeID";
    private static final String OPT_ROLE_ATTRIBUTE_IS_DN = "roleAttributeIsDN";
    private static final String OPT_ROLE_NAME_ATTRIBUTE_ID = "roleNameAttributeID";
    private static final String OPT_SEARCH_TIME_LIMIT = "searchTimeLimit";
    private static final String OPT_SEARCH_SCOPE = "searchScope";

    private static final String OPT_BIND_DN = "bindDN";
    private static final String OPT_BIND_CREDENTIAL = "bindCredential";
    private static final String OPT_BASE_CTX_DN = "baseCtxDN";
    private static final String OPT_BASE_FILTER = "baseFilter";
    private static final String OPT_DEFAULT_ROLE = "defaultRole";
    private static final String OPT_CHECK_ROLES_ASSIGNED = "checkRolesAssigned";

    private static final String OPT_USER_ATTRIBUTES = "userAttributes";

    private static final String OPT_DEBUG = "debug";

    private String roleGroupName = "Roles";
    private String permissionsGroupName = "Permissions";
    private String roleCtxDN = null;
    private String roleFilter = null;
    private String roleAttributeID = null;
    private String permissionAttributeID = null;
    private String roleNameAttributeID = null;
    private boolean roleAttributeIsDN = false;
    private boolean checkRolesAssigned = true;

    private Principal identity = null;
    private Group roles = null;
    private Group permissions = null;

    private Group[] roleSets;

    @Override
    public void initialize(Subject theSubject, CallbackHandler theCB, Map<String, ?> theSharedState, Map<String, ?> theOptions) {

        super.initialize(theSubject, theCB, theSharedState, theOptions);


        String roleGroup = (String) theOptions.get(OPT_ROLE_GROUP);
        if (roleGroup != null) {
            roleGroupName = roleGroup;
        }

        String permissionGroup = (String) theOptions.get(OPT_PERMISSION_GROUP);
        if (permissionGroup != null) {
            permissionsGroupName = permissionGroup;
        }


        debug = parseBooleanOption(OPT_DEBUG);
        if (debug) {
            LOG.info("Initialized LdapLoginModule");
        }
    }

    @Override
    public boolean login() throws LoginException {
        LOG.debug("login()");
        ///
        if (super.login()) {
            Object loginName = sharedState.get(CONTEXT_LOGIN_NAME);
            if (loginName == null) {
                throw new FailedLoginException("Found undefined '" + CONTEXT_LOGIN_NAME + "' in shared state.");
            }

            String name = null;
            if (loginName instanceof Principal) {
                name = ((Principal) loginName).getName();//JBoss EAP 6 sends in SimplePricipal!
            } else {
                name = loginName.toString();
            }
            try {
                identity = createIdentity(name);
            } catch (Exception e) {
                throw new FailedLoginException("Failed to create principal: " + e.getMessage());
            }

            Object password = sharedState.get(CONTEXT_LOGIN_PW);
            char[] credential = null;
            if (password instanceof char[]) {
                credential = (char[]) password;
            } else if (password != null) {
                credential = password.toString().toCharArray();
            }

            if (debug) {
                LOG.info("Found shared state, creating group '{}'", roleGroupName);
            }

            // get roles from LDAP
            roles = createGroup(roleGroupName, subject.getPrincipals());
            permissions = createGroup(permissionsGroupName, subject.getPrincipals());
            try {
                createLdapInitContext(identity.getName(), credential);

                if (hasOption(OPT_CHECK_ROLES_ASSIGNED)) {
                    checkRolesAssigned = parseBooleanOption(OPT_CHECK_ROLES_ASSIGNED);
                }
                if (checkRolesAssigned) {
                    if (isEmpty(roles)) {
                        throw new FailedLoginException("Failed LDAP login. No roles assigned to the user " + identity.getName());
                    }
                }
                addDefaultRole();
            } catch (Exception e) {
                LOG.error("Failed LDAP login.", e);
                throw new FailedLoginException(e.getMessage());
            }
            LOG.info("LDAP login successfully.");
            return true;
        }

        super.loginSuccess = false;

        String[] info = getUsernameAndPassword();
        String username = info[0];
        String password = info[1];

        if (identity == null) {
            try {
                identity = createIdentity(username);
            } catch (Exception e) {
                if (debug) {
                    LOG.info("Failed to create principal for user '{}'", username);
                }
                throw new LoginException("Failed to create principal: " + e.getMessage());
            }

            if (!validatePassword(password)) {
                throw new FailedLoginException("Password incorrect/Password required");
            }

            if (getStorePass()) {
                // add username and password to the shared state map
                ((Map) sharedState).put(CONTEXT_LOGIN_NAME, username);
                ((Map) sharedState).put(CONTEXT_LOGIN_PW, password.toCharArray());
            }
            super.loginSuccess = true;
        }
        LOG.info("LDAP login successfully finished.");
        return true;
    }

    /**
     * Checks if these group has members.
     *
     * @param group
     *        the group
     * @return true, if is empty
     */
    protected boolean isEmpty(Group group) {
        return !group.members().hasMoreElements();
    }

    @Override
    protected Principal getIdentity() {
        return identity;
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        return new Group[] { roles, permissions };
    }

    public void setRoleSets(Group[] roleSets) {
        this.roles = roleSets[0];
        this.permissions = roleSets[1];
    }

    private void addRole(String theRoleName) {
        if (theRoleName != null) {
            if (debug) {
                LOG.info("Adding role: {}", theRoleName);
            }

            try {
                roles.addMember(new SimplePrincipal(theRoleName));
            } catch (Exception e) {
                LOG.info("", e);
            }
        }
    }

    private void addPermission(Attribute thePermissions) {
        if (thePermissions != null) {
            for (int m = 0; m < thePermissions.size(); m++) {
                try {
                    String permission = (String) thePermissions.get(m);
                    if (debug) {
                        LOG.info("Adding permission: {}", permission);
                    }

                    permissions.addMember(new SimplePrincipal(permission));
                } catch (Exception e) {
                    LOG.info("", e);
                }
            }
        }
    }

    private void addDefaultRole() {
        try {
            String defaultRole = (String) getOption(OPT_DEFAULT_ROLE);
            if (defaultRole == null || defaultRole.equals("")) {
                return;
            }
            if (debug) {
                LOG.info("Adding default role");
            }

            addRole(defaultRole);
        } catch (Exception e) {
            LOG.info("", e);
        }
    }

    /**
     * Validate the password by creating an LDAP context with the
     * SECURITY_CREDENTIALS set to the password.
     *
     * @param password
     *        the password to validate
     * @throws LoginException
     *         on error
     */
    private boolean validatePassword(String password) throws LoginException {
        boolean valid = false;

        if (password != null) {
            // See if this is an empty password that should be disallowed
            if (password.length() == 0) {
                // Check for an allowEmptyPasswords option
                boolean allowEmptyPasswords = parseBooleanOption(OPT_ALLOW_EMPTY_PWS);

                if (allowEmptyPasswords == false) {
                    if (debug) {
                        LOG.info("Rejecting empty password");
                    }

                    return false;
                }
            }

            try {
                // Validate the password by trying to create an initial context
                valid = createLdapInitContext(identity.getName(), password);
            } catch (Exception e) {
                LoginException le = new LoginException("Could not authenticate user");
                le.initCause(e);
                throw le;
            }
        }
        return valid;
    }


    /**
     * Bind to the LDAP server for authentication.
     *
     * @param username
     *        user name
     * @param credential
     *        user password
     * @return true if the bind for authentication succeeded
     * @throws NamingException
     *         on error
     */
    private boolean createLdapInitContext(String username, Object credential) throws Exception {
        String bindDN = (String) getOption(OPT_BIND_DN);
        String bindCredential = (String) getOption(OPT_BIND_CREDENTIAL);

        String baseDN = (String) getOption(OPT_BASE_CTX_DN);
        String baseFilter = (String) getOption(OPT_BASE_FILTER);
        roleFilter = (String) getOption(OPT_ROLE_FILTER);
        roleAttributeID = (String) getOption(OPT_ROLE_ATTRIBUTE_ID);
        if (roleAttributeID == null) {
            roleAttributeID = "cn";
        }
        permissionAttributeID = (String) getOption(OPT_PERMISSION_ATTRIBUTE_ID);
        if (permissionAttributeID == null) {
            permissionAttributeID = "permissions";
        }
        // Is user's role attribute a DN or the role name?
        roleAttributeIsDN = parseBooleanOption(OPT_ROLE_ATTRIBUTE_IS_DN);
        roleNameAttributeID = (String) getOption(OPT_ROLE_NAME_ATTRIBUTE_ID);
        if (roleNameAttributeID == null) {
            roleNameAttributeID = "name";
        }

        roleCtxDN = (String) getOption(OPT_ROLES_CTX_DN);
        int recursion = parseIntOption(OPT_ROLE_RECURSION, 0);
        int searchTimeLimit = parseIntOption(OPT_SEARCH_TIME_LIMIT, 10000);

        String scope = (String) getOption(OPT_SEARCH_SCOPE);
        int searchScope = SearchControls.OBJECT_SCOPE;
        if ("OBJECT_SCOPE".equalsIgnoreCase(scope)) {
            searchScope = SearchControls.OBJECT_SCOPE;
        } else if ("ONELEVEL_SCOPE".equalsIgnoreCase(scope)) {
            searchScope = SearchControls.ONELEVEL_SCOPE;
        } else if ("SUBTREE_SCOPE".equalsIgnoreCase(scope)) {
            searchScope = SearchControls.SUBTREE_SCOPE;
        }

        // Get the admin context for searching
        InitialLdapContext ctx = null;
        try {
            if (debug) {
                LOG.info("Creating initial context with user {}", bindDN);
            }

            ctx = constructInitialLdapContext(bindDN, bindCredential);

            // Validate the user by binding against the userDN
            if (debug) {
                LOG.info("Authenticating user");
            }

            // avoid userDN lookup by using cache
            String userDN = LDAPCache.getCache(this.providerURL).getUserDn(username);
            if (userDN == null) {
                userDN = bindDNAuthentication(ctx, username, credential, baseDN, baseFilter, searchTimeLimit);
                LDAPCache.getCache(this.providerURL).setUserDn(username, userDN);
            }

            // avoid role search ldap query
            if (LDAPCache.getCache(this.providerURL).getUserRolesPermission(username) != null) {
                setRoleSets(LDAPCache.getCache(this.providerURL).getUserRolesPermission(username));
            } else {
                // Query for roles matching the role filter
                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(searchScope);
                constraints.setReturningAttributes(new String[0]);
                constraints.setTimeLimit(searchTimeLimit);
                if (debug) {
                    LOG.info("Querying roles");
                }
                rolesSearch(ctx, constraints, username, userDN, recursion, 0);
                // cache results
                if (getRoleSets()[0] != null && getRoleSets()[1] != null) {
                    LDAPCache.getCache(this.providerURL).setUserRolesPermission(username, getRoleSets());
                }
            }
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException {
        LOG.info("Constructing initial LDAP Context...");
        Properties env = new Properties();
        Iterator<?> iter = options.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            env.put(entry.getKey(), entry.getValue());
        }

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
        String providerURL = (String) getOption(Context.PROVIDER_URL);
        if (providerURL == null) {
            providerURL = "ldap://localhost:" + ("ssl".equals(protocol) ? "636" : "389");
        }

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
            LOG.error("Failed creating initial LDAP context.", e);
            if (e instanceof NamingException) {
                throw (NamingException) e;
            }
        }

        LOG.info("Initial LDAP Context constructed.");

        return ctx;

    }

    /**
     * @param ctx
     *        the context to search from
     * @param user
     *        the input username
     * @param credential
     *        the bind credential
     * @param baseDN
     *        base DN to search the ctx from
     * @param filter
     *        the search filter string
     * @param limit
     *        search time limit
     * @return the userDN string for the successful authentication
     * @throws NamingException
     *         if a naming exception is encountered
     */
    protected String bindDNAuthentication(InitialLdapContext ctx, String user, Object credential, String baseDN, String filter, int limit)
        throws NamingException {
        SearchControls searchConstraints = new SearchControls();
        searchConstraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchConstraints.setTimeLimit(limit);

        boolean extendedUserAttrs = (identity instanceof CompanyPrincipal);
        String attrs[];

        // if we're using a CompanyPrincipal, gather additional info
        if (extendedUserAttrs) {
            String definedUserAttributes = (String) getOption(OPT_USER_ATTRIBUTES);
            if (definedUserAttributes == null) {
                // use default attributes
                attrs = new String[] { "sn", "givenName", "mail", "cn", "initials", "employeeNumber", "uid" };
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

        // we don't want to reauthenticate when using the previous credentials
        if (!super.getUseFirstPass()) {
            // Bind as the user dn to authenticate the user
            InitialLdapContext userCtx = constructInitialLdapContext(userDN, credential);
            userCtx.close();
        }

        if (debug) {
            LOG.info("Authenticated as: {}", userDN);
        }

        return userDN;
    }

    /**
     * Roles search.
     *
     * @param ctx
     *        LDAP context to use
     * @param constraints
     *        search controls
     * @param user
     *        name of user to get roles for
     * @param userDN
     *        DN of the user
     * @param recursionMax
     *        the maximum recursion level
     * @param nesting
     *        the current recursion level
     * @throws NamingException
     *         the naming exception
     */
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
                                LOG.info("", e);
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

    private void parseUser(SearchResult result, String userDN, CompanyPrincipal principal) throws NamingException {
        principal.setDn(userDN);

        NamingEnumeration<? extends Attribute> ar = result.getAttributes().getAll();
        while (ar.hasMoreElements()) {
            Attribute att = ar.next();
            String id = att.getID();
            principal.addProperty(id,(String) att.get());

            if (debug) {
                LOG.info("Got attribute: {} ({})", new Object[] { id, att.get() });
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
}

