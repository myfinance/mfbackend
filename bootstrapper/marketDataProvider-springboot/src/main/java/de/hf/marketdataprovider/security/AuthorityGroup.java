/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.security;


import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class AuthorityGroup extends BasePrincipal implements Cloneable, Serializable {

    private static final long serialVersionUID = 8399135191756840977L;

    private HashMap<GrantedAuthorityImpl, GrantedAuthorityImpl> members;
    /**
     * The default constructor.
     * @param groupName the group name
     */
    public AuthorityGroup(String groupName) {
        super(groupName);
        members = new HashMap<>(3);
    }

    /**
     * Add a member to the group.
     *
     * @param theMember
     *        the principal to add to this group
     * @return <code>true</code> if the principal was successfully added, <code>false</code> if it already was a member
     */
    public boolean addMember(GrantedAuthorityImpl theMember) {
        boolean isMember = members.containsKey(theMember);
        if (!isMember) {
            members.put(theMember, theMember);
        }
        return !isMember;
    }

    /**
     * Check whether a principal is already a member of the group. This function
     * recursively iterates through all members to search for the principal.
     *
     * @param theMember
     *        the principal to check for
     * @return <code>true</code> if the principal is a member of the group, <code>false</code> otherwise.
     */
    public boolean isMember(Principal theMember) {
        // check for direct members
        boolean isMember = members.containsKey(theMember);

        if (!isMember) {
            // check for group membership recursively
            Collection<GrantedAuthorityImpl> values = members.values();
            Iterator<GrantedAuthorityImpl> iter = values.iterator();

            while (!isMember && iter.hasNext()) {
                Object next = iter.next();
                if (next instanceof Group) {
                    Group group = (Group) next;
                    isMember = group.isMember(theMember);
                }
            }
        }
        return isMember;
    }

    /**
     * Return an enumeration of the group members.
     *
     * @return an enumeration of the members
     */
    public Enumeration<? extends GrantedAuthorityImpl> members() {
        return Collections.enumeration(members.values());
    }

    public Collection<? extends GrantedAuthority> memberCollection() {
        return members.values();
    }

    /**
     * Remove a member from the group.
     *
     * @param member
     *        the principal to remove from this group
     * @return <code>true</code> if the member was removed, <code>false</code> if it was not a member
     */
    public boolean removeMember(Principal member) {
        Principal p = members.remove(member);
        return p != null;
    }

    public void clearMembers() {
        members.clear();
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(getName());
        Iterator<GrantedAuthorityImpl> iter = members.keySet().iterator();

        buf.append('[');
        while (iter.hasNext()) {
            buf.append(iter.next());
            buf.append(',');
        }
        buf.setCharAt(buf.length() - 1, ']');
        return buf.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Object clone() throws CloneNotSupportedException {
        AuthorityGroup clone = (AuthorityGroup) super.clone();
        if (clone != null) {
            clone.members = (HashMap<GrantedAuthorityImpl, GrantedAuthorityImpl>) this.members.clone();
        }

        return clone;
    }
}

