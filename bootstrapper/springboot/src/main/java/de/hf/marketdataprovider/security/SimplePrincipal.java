/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import de.hf.marketDataProvider.common.security.BasePrincipal;

import javax.naming.directory.Attribute;
import java.util.Collection;

@Slf4j
public class SimplePrincipal extends BasePrincipal implements UserDetails {

    private static final long serialVersionUID = -277583078436504038L;

    private AuthorityGroup roles;
    private AuthorityGroup permissions;

    /**
     * The default constructor.
     * @param theName the name
     */
    public SimplePrincipal(String theName) {
        super(theName);
        permissions = new AuthorityGroup("Permissions");
        roles = new AuthorityGroup("Roles");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.memberCollection();
    }

    public void addRole(String theRoleName) {
        if (theRoleName != null) {
            try {
                getRoles().addMember(new GrantedAuthorityImpl(theRoleName));
            } catch (Exception e) {
                log.warn("", e);
            }
        }
    }

    public void clearRoles() {
        getRoles().clearMembers();
    }

    public void addPermission(Attribute thePermissions) {
        if (thePermissions != null) {
            for (int m = 0; m < thePermissions.size(); m++) {
                try {
                    String permission = (String) thePermissions.get(m);

                    permissions.addMember(new GrantedAuthorityImpl(permission));
                } catch (Exception e) {
                    log.warn("", e);
                }
            }
        }
    }

    public AuthorityGroup getRoles() {
        return roles;
    }

    public AuthorityGroup getPermissions() {
        return permissions;
    }

    @Override
    public String getPassword() {
        return null;
    }

    /**
     * Get the name of this principal.
     *
     * @return principal name
     */
    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

