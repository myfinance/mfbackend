/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseLoginModule.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth.modules;

import de.hf.dac.api.io.env.EnvironmentConfiguration;
import de.hf.dac.api.io.lookup.OSGIServiceLookup;
import de.hf.dac.security.auth.CompanyPrincipal;
import de.hf.dac.security.auth.SimpleGroup;
import de.hf.dac.security.auth.SimplePrincipal;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.karaf.jaas.boot.principal.RolePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * The <code>LdapLoginModule</code> is an implementation of the <code>LoginModule</code> interface to authenticate users against and extract roles
 * from an LDAP server via the JAAS framework.
 * </p>
 * <p>
 * This module supports the following options:
 * <ul>
 * <li><code>principalClass</code> - class to use for instantiating the principal, defaults to <code>de.dzbank.lisa.auth.SimplePrincipal</code>.</li>
 * <li><code>allowEmptyPasswords</code> - if <code>false</code>, authenticating with an empty password is not allowed and authentication will fail.
 * For some LDAP servers, empty passwords are treated as anonymous login. The default is <code>false</code>.</li>
 * <li><code>storePass</code> - if <code>true</code>, the LoginModule stores the user name and password obtained from the <code>CallbackHandler</code>
 * in the shared state, using <code>javax.security.auth.login.name</code> and <code>javax.security.auth.login.password</code> as the respective keys.
 * This is not performed if values already exist for username and password in the shared state, or if authentication fails.</li>
 * <li><code>useFirstPass</code> - if <code>true</code>, the first LoginModule in the stack saves the password entered, and subsequent LoginModules
 * also try to use it. LoginModules do not prompt for a new password if authentication fails (authentication simply fails).</li>
 * </ul>
 *
 */
//todo can we use AbstractKarafLoginModule?
//public abstract class BaseLoginModule extends AbstractKarafLoginModule implements LoginModule {
public abstract class BaseLoginModule implements LoginModule {
    public static final String WHAT = "$Id$";
    private static final Logger LOG = LoggerFactory.getLogger(BaseLoginModule.class);

    protected static final String CONTEXT_LOGIN_NAME = "javax.security.auth.login.name";
    protected static final String CONTEXT_LOGIN_PASSWORD = "javax.security.auth.login.password";
    private static final String PROVIDER_URL = "java.naming.provider.url";

    //the default user if the authorisation is deactivated
    protected static final String NO_AUTHENTIFICATION_USER = "admin";

    protected Subject subject;
    protected CallbackHandler callbackHandler;
    protected Map<String, ?> sharedState;
    protected Map<String, ?> options;
    protected boolean loginSuccess;
    protected boolean isAuthActive = true;
    protected String providerURL;

    private String principalClassName;
    private boolean useFirstPass;
    private boolean storePass;
    EnvironmentConfiguration serviceByInterface = null;
    private HashMap<String, String> roleMappings;


    @Override
    public void initialize(Subject theSubject, CallbackHandler theCB, Map<String, ?> theSharedState, Map<String, ?> theOptions) {
        //EnvironmentConfiguration is not injected via blueprint because it is not working. A timing issue?! it runs with declarative services maybe?
        serviceByInterface = OSGIServiceLookup.getServiceByInterface(EnvironmentConfiguration.class);
        this.providerURL = (String) theOptions.get(PROVIDER_URL);
        assert (serviceByInterface != null);
        isAuthActive=serviceByInterface.getString("LDAP", "auth.ldap.active").toLowerCase().equals("true");
        this.subject = theSubject;
        this.callbackHandler = theCB;
        this.sharedState = theSharedState;
        this.options = theOptions;


        useFirstPass = parseBooleanOption("useFirstPass");
        storePass = parseBooleanOption("storePass");

        // read roleMappings
        this.roleMappings = new HashMap<String, String>();
        String mappings = getOption("permissionRoleMapping");
        if (mappings != null) {
            String[] all = mappings.split(",");
            for (String s : all) {
                String[] kv = s.split("=");
                this.roleMappings.put(kv[0], kv[1]);
            }
        }

        Object principalClass = theOptions.get("principalClass");
        if (principalClass != null) {
            if (principalClass instanceof String) {
                principalClassName = (String) principalClass;
            } else {
                principalClassName = principalClass.getClass().getName();
                LOG.debug("No simple 'principalClass' found -> using {}", principalClassName);
            }
        } else {
            principalClassName = CompanyPrincipal.class.getName();
            LOG.debug("No 'principalClass' configured -> using {}", principalClassName);

        }
    }

    @Override
    public boolean login() throws LoginException {
        loginSuccess = false;
        if (useFirstPass) {
            Object identity = sharedState.get(CONTEXT_LOGIN_NAME);
            Object credential = sharedState.get(CONTEXT_LOGIN_PASSWORD);
            if (identity != null & credential != null) {
                loginSuccess = true;
            } else {
                throw new FailedLoginException("No principal or credentials available");
            }
        }

        return loginSuccess;
    }

    @Override
    public boolean commit() throws LoginException {
        if (!loginSuccess) {
            return false;
        }

        Set<Principal> principals = subject.getPrincipals();

        Set<Principal> plainRoleRightPrincipals = new HashSet<>();

        Principal identity = getIdentity();
        if (identity != null) {
            principals.add(identity);
        }

        Group[] roleSets = getRoleSets();

        for (int i = 0; i < roleSets.length; ++i) {
            Group group = roleSets[i];
            if (group != null) {
                Group subjectGroup = createGroup(group.getName(), principals);

                // copy the roles to the group
                Enumeration<? extends Principal> members = group.members();
                while (members.hasMoreElements()) {
                    Principal role = members.nextElement();
                    subjectGroup.addMember(role);
                    if(this.roleMappings!=null) {
                        if (i == 1) { // Roles are Groups
                            //plainRoleRightPrincipals.add(new GroupPrincipal(role.getName()));
                            if (this.roleMappings.containsKey(role.getName())) {
                                plainRoleRightPrincipals.add(new RolePrincipal(this.roleMappings.get(role.getName())));
                            }
                        } else { // Permissions are Roles
                            //plainRoleRightPrincipals.add(new RolePrincipal(role.getName()));
                            if (this.roleMappings.containsKey(role.getName())) {
                                plainRoleRightPrincipals.add(new RolePrincipal(this.roleMappings.get(role.getName())));
                            }
                        }
                    }
                    else {
                        plainRoleRightPrincipals.add(new RolePrincipal(role.getName()));
                    }
                }
            }
        }
        subject.getPrincipals().addAll(plainRoleRightPrincipals);
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        // remove the user identity
        Principal identity = getIdentity();
        subject.getPrincipals().remove(identity);
        return true;
    }

    /**
     * Find a group with the given name.
     *
     * @param name
     *        name of the group
     * @param principals
     *        set of principals
     * @return group or <code>null</code> if not found
     */
    protected Group findGroup(String name, Set<Principal> principals) {
        Iterator<Principal> iter = principals.iterator();
        while (iter.hasNext()) {
            Principal next = iter.next();
            if (next instanceof Group) {
                Group g = (Group) next;
                if (g.getName().equals(name)) {
                    return g;
                }
            }
        }
        return null;
    }

    /**
     * Find or create a group with the given name.
     *
     * @param name
     *        name of the group
     * @param principals
     *        set of principals to look for the group in
     * @return the requested group
     */
    protected Group createGroup(String name, Set<Principal> principals) {
        Group group = findGroup(name, principals);

        // if we didn't find the group create it
        if (group == null) {
            group = new SimpleGroup(name);
            principals.add(group);
        }
        return group;
    }

    /**
     * Create a <code>Principal</code> for a user. If the <code>principalClass</code> option was provided to the module,
     * this will create an instance of that class using a constructor
     * with a <code>String</code> argument. By default, it will create
     * a <code>SimplePrincipal</code>.
     *
     * @param name
     *        the name of the principal
     * @return the principal instance
     * @throws java.lang.Exception
     *         if the principal cannot be created
     */
    protected Principal createIdentity(String name) throws Exception {
        Principal p = null;
        if (principalClassName == null) {
            p = new SimplePrincipal(name);
        } else {
            //            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = this.getClass().getClassLoader().loadClass(principalClassName);
            Class<?>[] ctorSig = { String.class };
            Constructor<?> ctor = clazz.getConstructor(ctorSig);
            Object[] ctorArgs = { name };
            p = (Principal) ctor.newInstance(ctorArgs);
        }
        return p;
    }

    /**
     * Parse an option for a boolean value. If the option contains an
     * invalid value, it will default to false.
     *
     * @param option
     *        name of the option
     * @return option value
     */
    protected boolean parseBooleanOption(String option) {
        String s = (String) getOption(option);
        return s != null && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equals("1") || s.equalsIgnoreCase("on"));
    }

    /**
     * Checks for option set.
     *
     * @param option
     *        the option
     * @return true, if option is set
     */
    protected boolean hasOption(String option) {
        return options.containsKey(option);
    }

    /**
     * Parse an option for an integer value.
     *
     * @param option
     *        name of the option
     * @param defVal
     *        default value if option is unset or invalid
     * @return option value
     */
    protected int parseIntOption(String option, int defVal) {
        String s = (String) getOption(option);
        if (s != null) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                LOG.debug("", e);
            }
        }
        return defVal;
    }

    /**
     * Check for replacement parameters. Look up within ReSFile LDAP Section
     * @param name
     * @return
     */
    protected String getOption(String name) {
        String result = (String) options.get(name);
        // result may be of ${...} style
        if (result != null && result.startsWith("${") && result.endsWith("}")) {
            result = serviceByInterface.getString("LDAP", result.substring(2, result.length() - 1));
        }
        return result;
    }
    /**
     * Should be called by <code>login()</code> to acquire the username
     * and password strings for authentication. This method does no
     * validation of either.
     *
     * @return String[], [0] = username, [1] = password
     * @exception LoginException
     *            if CallbackHandler is not set or fails
     */
    protected String[] getUsernameAndPassword() throws LoginException {
        String[] info = { null, null };
        // prompt for a username and password
        if (callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available " + "to collect authentication information");
        }

        NameCallback nc = new NameCallback("User name: ", "guest");
        PasswordCallback pc = new PasswordCallback("Password: ", false);
        Callback[] callbacks = { nc, pc };
        String username = null;
        String password = null;
        try {
            callbackHandler.handle(callbacks);
            username = nc.getName();
            char[] tmpPassword = pc.getPassword();
            if (tmpPassword != null) {
                char[] credential = new char[tmpPassword.length];
                System.arraycopy(tmpPassword, 0, credential, 0, tmpPassword.length);
                pc.clearPassword();
                password = new String(credential);
            }
        } catch (IOException e) {
            LoginException le = new LoginException("Failed to get username/password");
            le.initCause(e);
            throw le;
        } catch (UnsupportedCallbackException e) {
            LoginException le = new LoginException("CallbackHandler does not support " + e.getCallback());
            le.initCause(e);
            throw le;
        }
        info[0] = username;
        info[1] = password;
        return info;
    }

    /**
     * @return whether to use the identity and credentials from the
     *         shared context
     */
    protected boolean getUseFirstPass() {
        return useFirstPass;
    }

    /**
     * @return whether to store the identity and credentials in the
     *         shared context
     */
    protected boolean getStorePass() {
        return storePass;
    }

    /**
     * Subclasses need to override this to provide the user identity.
     * After successfully login, the principal returned by {@link #getIdentity()} will be added to
     * {@link Subject} principals.
     *
     * @return the user's primary identity
     */
    abstract protected Principal getIdentity();

    /**
     * Subclasses need to override this to provide the groups that
     * correspond to the roles assigned to the user.
     *
     * @return the user's role sets
     * @throws LoginException
     *         the login exception
     */
    abstract protected Group[] getRoleSets() throws LoginException;
}

