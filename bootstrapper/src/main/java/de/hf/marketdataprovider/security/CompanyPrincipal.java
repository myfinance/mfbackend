/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.security;

/**
 *
 * @author xn01598
 */
public class CompanyPrincipal extends SimplePrincipal {
    public static final String WHAT = "$Id$";

    private static final long serialVersionUID = -2093165888366191998L;

    private String firstName;
    private String lastName;
    private String department;
    private String mail;
    private String costCenter;
    private String dzanrede;
    private String dzarbeitsplatz;
    private String dzani;
    private String dzbereich;
    private String dzfaxNumber;
    private String dztelNumber;
    private String dzlokkurz;
    private String dzraum;
    private String dzetage;
    private String dzgebaeude;
    private String dzpnrleit;
    private String dzkstdn;
    private String dzksthierarchy;
    private String dzdepartmentlong;
    private String location;
    private String dzgeschlecht;
    private String initials;
    private String employeeNumber;
    private String uid;
    private String cn;
    private String dzuid;
    private String dn;

    /**
     * Create a new principal.
     *
     * @param name
     *        name of the user to create the principal for
     */
    public CompanyPrincipal(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CompanyPrincipal other = (CompanyPrincipal) obj;
        if (this.dzuid == null) {
            if (other.dzuid != null) {
                return false;
            }
        } else if (!this.dzuid.equals(other.dzuid)) {
            return false;
        }
        return true;
    }

    public String getCn() {
        return this.cn;
    }

    /**
     * @return the cost center
     */
    public String getCostCenter() {
        return costCenter;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    public String getDn() {
        return this.dn;
    }

    public String getDzani() {
        return this.dzani;
    }

    public String getDzanrede() {
        return this.dzanrede;
    }

    public String getDzarbeitsplatz() {
        return this.dzarbeitsplatz;
    }

    public String getDzbereich() {
        return this.dzbereich;
    }

    public String getDzdepartmentlong() {
        return this.dzdepartmentlong;
    }

    public String getDzetage() {
        return this.dzetage;
    }

    public String getDzfaxNumber() {
        return this.dzfaxNumber;
    }

    public String getDzgebaeude() {
        return this.dzgebaeude;
    }

    public String getDzgeschlecht() {
        return this.dzgeschlecht;
    }

    public String getDzkstdn() {
        return this.dzkstdn;
    }

    public String getDzksthierarchy() {
        return this.dzksthierarchy;
    }

    public String getDzlokkurz() {
        return this.dzlokkurz;
    }

    public String getDzpnrleit() {
        return this.dzpnrleit;
    }

    public String getDzraum() {
        return this.dzraum;
    }

    public String getDztelNumber() {
        return this.dztelNumber;
    }

    public String getDzuid() {
        return this.dzuid;
    }

    /**
     * @return the email address
     */
    public String getEmail() {
        return mail;
    }

    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    /**
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    public String getInitials() {
        return this.initials;
    }

    /**
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    public String getLocation() {
        return this.location;
    }

    public String getUid() {
        return this.uid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.dzuid == null) ? 0 : this.dzuid.hashCode());
        return result;
    }

    public void setCn(String theCN) {
        this.cn = theCN;
    }

    /**
     * @param cc
     *        the cost center to set
     */
    public void setCostCenter(String cc) {
        this.costCenter = cc;
    }

    /**
     * @param department
     *        the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDn(String theDN) {
        this.dn = theDN;
    }

    public void setDzani(String theDZani) {
        this.dzani = theDZani;
    }

    public void setDzanrede(String dzanrede) {
        this.dzanrede = dzanrede;
    }

    public void setDzarbeitsplatz(String dzarbeitsplatz) {
        this.dzarbeitsplatz = dzarbeitsplatz;
    }

    public void setDzbereich(String theDZbereich) {
        this.dzbereich = theDZbereich;
    }

    public void setDzdepartmentlong(String theDZDepartmentlong) {
        this.dzdepartmentlong = theDZDepartmentlong;
    }

    public void setDzetage(String theDZEtage) {
        this.dzetage = theDZEtage;
    }

    public void setDzfaxNumber(String theDZFaxNumber) {
        this.dzfaxNumber = theDZFaxNumber;
    }

    public void setDzgebaeude(String theDZGebaeude) {
        this.dzgebaeude = theDZGebaeude;
    }

    public void setDzgeschlecht(String theDZGeschlecht) {
        this.dzgeschlecht = theDZGeschlecht;
    }

    public void setDzkstdn(String theDZKstdn) {
        this.dzkstdn = theDZKstdn;
    }

    public void setDzksthierarchy(String theDZKsthierarchy) {
        this.dzksthierarchy = theDZKsthierarchy;
    }

    public void setDzlokkurz(String theDZLokkurz) {
        this.dzlokkurz = theDZLokkurz;
    }

    public void setDzpnrleit(String theDZpnrleit) {
        this.dzpnrleit = theDZpnrleit;
    }

    public void setDzraum(String theDZRaum) {
        this.dzraum = theDZRaum;
    }

    public void setDztelNumber(String theDZTelNumber) {
        this.dztelNumber = theDZTelNumber;
    }

    public void setDzuid(String theDZuid) {
        this.dzuid = theDZuid;
    }

    /**
     * @param email
     *        the email address to set
     */
    public void setEmail(String email) {
        this.mail = email;
    }

    public void setEmployeeNumber(String theEmployeeNumber) {
        this.employeeNumber = theEmployeeNumber;
    }

    /**
     * @param firstName
     *        the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setInitials(String theInitials) {
        this.initials = theInitials;
    }

    /**
     * @param lastName
     *        the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLocation(String theLocation) {
        this.location = theLocation;
    }

    public void setUid(String theUID) {
        this.uid = theUID;
    }

    @Override
    public String toString() {
        return super.getName() + " (" + lastName + ", " + firstName + ')';
    }

}
