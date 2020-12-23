/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : LDAPCache.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.security.auth.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.acl.Group;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LDAPCache {

    public static final Logger LOG = LoggerFactory.getLogger(LDAPCache.class);

    public static final long MAX_CACHE_LIFETIME = (long)600000;

    /** One Cache per unique LDAP URL*/
    private static final ConcurrentMap<String, LDAPCache> CACHES = new ConcurrentHashMap();

    // Simple LDAP Caching
    private final Map<String, CachedInstance> cachedInstances = new ConcurrentHashMap<>();

    private final String ldapNamingUrl;
    private final long maxCacheLifetime;

    public LDAPCache(String url, long maxCacheLifetime
    ) {
        this.ldapNamingUrl = url;
        this.maxCacheLifetime = maxCacheLifetime;
    }

    public CachedInstance getCachedInstance(String user) {
        if (!cachedInstances.containsKey(user)) {
            createCachedInstance(user);
        }

        CachedInstance temp = cachedInstances.get(user);
        if (!temp.isValid()) {
            createCachedInstance(user);
            LOG.debug("User Roles expired in cache. Renew from LDAP. {}", user);
        }
        return temp;
    }

    protected void createCachedInstance(String user) {
        cachedInstances.put(user, new CachedInstance(getMaxLifeTime()));
    }

    public static final void clear() {
        while (!CACHES.isEmpty()) {
            String options = (String) CACHES.keySet().iterator().next();
            LDAPCache cache = (LDAPCache) CACHES.remove(options);
            if (cache != null) {
                cache.clearCache();
            }
        }
    }

    public static LDAPCache getCache(String options) {
        LDAPCache cache = (LDAPCache) CACHES.get(options);
        if (cache == null) {
            CACHES.putIfAbsent(options, new LDAPCache(options, MAX_CACHE_LIFETIME));
            cache = (LDAPCache) CACHES.get(options);
        }

        return cache;
    }

    protected void clearCache() {
        cachedInstances.clear();
    }

    public String getUserDn(String username) {
        return getCachedInstance(username).getUserDn();
    }

    public void setUserDn(String username, String userDN) {
        getCachedInstance(username).setUserDn(userDN);
    }

    public Group[] getUserRolesPermission(String user) {
        return getCachedInstance(user).getUserRolesPermission();
    }

    public void setUserRolesPermission(String user, Group[] groups) {
        getCachedInstance(user).setUserRolesPermission(groups);
    }

    public long getMaxLifeTime() {
        return maxCacheLifetime;
    }

}

class CachedInstance {

    long created = 	ZonedDateTime.now().getNano();
    long maxLifetime = LDAPCache.MAX_CACHE_LIFETIME;      // defaults to 10 Min

    private String userDn = null;
    private Group[] userRolesPermission = null;

    public CachedInstance(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public String getUserDn() {
        return userDn;
    }

    public Group[] getUserRolesPermission() {
        return userRolesPermission;
    }

    public boolean isValid() {
        return (ZonedDateTime.now().getNano() - created) <= maxLifetime;
    }

    public void setUserDn(String userDn) {
        this.userDn = userDn;
    }

    public void setUserRolesPermission(Group[] userRolesPermission) {
        this.userRolesPermission = userRolesPermission;
    }
}