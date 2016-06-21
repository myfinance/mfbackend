/** ----------------------------------------------------------------------------
 *
 * ---                                ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : AbsSecurityConfig.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 31.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.config.security;

import de.hf.marketdataprovider.security.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class AbsSecurityConfig extends GlobalAuthenticationConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(AbsSecurityConfig.class);

    private Map<String,List<String>> internalRoleMap = null;

    @Value("${internalRoleMapping}")
    private String internalRoleMapping;

    @Value("${useRoleMapping}")
    private Boolean useRoleMapping;

    protected Map<String,List<String>> getInternalRoleMapping() {
        if(internalRoleMap!=null) {
            return internalRoleMap;
        }
        internalRoleMap = new HashMap<>();
        if(internalRoleMapping!=null && !internalRoleMapping.equals("")) {
            StringTokenizer tokenizer = new StringTokenizer(internalRoleMapping, ";");
            while (tokenizer.hasMoreTokens()) {
                StringTokenizer keyValueTokenizer = new StringTokenizer(tokenizer.nextToken(), ",");
                try {
                    String key = keyValueTokenizer.nextToken();
                    String value = keyValueTokenizer.nextToken();
                    List<String> values;
                    if(!internalRoleMap.containsKey(key)) {
                        values = new ArrayList<>();
                    } else {
                        values = internalRoleMap.get(key);
                    }
                    values.add(value);
                    internalRoleMap.put(key, values);
                }
                catch (Exception e) {
                    log.error("malformed role mapping in property file");
                    throw e;
                }
            }
        }
        return internalRoleMap;
    }

    protected SimplePrincipal mapRoles(SimplePrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        if(!useRoleMapping) {
            authorities.stream().forEach(s->principal.addRole(s.getAuthority()));
            return principal;
        }
        Map<String,List<String>> roleMapping = getInternalRoleMapping();
        principal.clearRoles();
        authorities.stream().filter(s -> roleMapping.containsKey(s.getAuthority())).map(s -> roleMapping.get(s.getAuthority())).forEach(s -> s.stream().forEach(
            principal::addRole));

        return principal;
    }
}
