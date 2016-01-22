/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.domain;

import javax.persistence.Entity;

/**
 *
 * @author xn01598
 */
@Entity
public class Loan extends Account{
    private static final long serialVersionUID = 1L;
    
    private double annuityValue;
    
    public double getAnnuityValue() {
        return annuityValue;
    }
    
    public void setAnnuityValue(double annuityValue) {
        this.annuityValue=annuityValue;
    }

}
