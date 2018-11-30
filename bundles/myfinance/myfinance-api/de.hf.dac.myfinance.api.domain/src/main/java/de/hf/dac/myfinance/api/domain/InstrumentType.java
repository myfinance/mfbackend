/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : InstrumentType.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 02.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.domain;

public enum InstrumentType {
    Giro(new Integer(1)),
    MoneyAtCall(new Integer(2)),
    TimeDeposit(new Integer(3)),
    BuildingsavingAccount(new Integer(4)),
    Budget(new Integer(5)),
    Tenant(new Integer(6)),
    BudgetGroupPortfolio(new Integer(7)),
    AccountPortfolio(new Integer(8)),
    ArtificialPortfolio(new Integer(9)),
    BudgetGroup(new Integer(10)),
    Depot(new Integer(10)),
    Buildingsaving(new Integer(12)),
    Currency(new Integer(13)),
    Equity(new Integer(14)),
    Fonds(new Integer(15)),
    ETF(new Integer(16)),
    Index(new Integer(17)),
    Bond(new Integer(18)),
    LifeInsurance(new Integer(19)),
    DepreciationObject(new Integer(20)),
    RealEstate(new Integer(21)),
    Loan(new Integer(22)),
    UNKNOWN(new Integer(99));

    private final Integer value;

    InstrumentType(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }

    public InstrumentTypeGroup getTypeGroup(){
        switch(value){
            case 6: return InstrumentTypeGroup.PORTFOLIO;
            case 7: return InstrumentTypeGroup.PORTFOLIO;
            case 8: return InstrumentTypeGroup.PORTFOLIO;
            case 9: return InstrumentTypeGroup.PORTFOLIO;
            case 10: return InstrumentTypeGroup.PORTFOLIO;
            case 11: return InstrumentTypeGroup.PORTFOLIO;
            case 12: return InstrumentTypeGroup.PORTFOLIO;
            case 13: return InstrumentTypeGroup.SECURITY;
            case 14: return InstrumentTypeGroup.SECURITY;
            case 15: return InstrumentTypeGroup.SECURITY;
            case 16: return InstrumentTypeGroup.SECURITY;
            case 17: return InstrumentTypeGroup.SECURITY;
            case 18: return InstrumentTypeGroup.SECURITY;
            case 19: return InstrumentTypeGroup.LIVEINSURANCE;
            case 20: return InstrumentTypeGroup.DEPRECATIONOBJECT;
            case 21: return InstrumentTypeGroup.REALESTATE;
            case 22: return InstrumentTypeGroup.LOAN;
            default: return InstrumentTypeGroup.CASHACCOUNT;
        }
    }

    public static InstrumentType getInstrumentTypeById(int instrumenttypeId){
        switch(instrumenttypeId){
            case 1: return InstrumentType.Giro;
            case 2: return InstrumentType.MoneyAtCall;
            case 3: return InstrumentType.TimeDeposit;
            case 4: return InstrumentType.BuildingsavingAccount;
            case 5: return InstrumentType.Budget;
            case 6: return InstrumentType.Tenant;
            case 7: return InstrumentType.BudgetGroupPortfolio;
            case 8: return InstrumentType.AccountPortfolio;
            case 9: return InstrumentType.ArtificialPortfolio;
            case 10: return InstrumentType.BudgetGroup;
            case 11: return InstrumentType.Depot;
            case 12: return InstrumentType.Buildingsaving;
            case 13: return InstrumentType.Currency;
            case 14: return InstrumentType.Equity;
            case 15: return InstrumentType.Fonds;
            case 16: return InstrumentType.ETF;
            case 17: return InstrumentType.Index;
            case 18: return InstrumentType.Bond;
            case 19: return InstrumentType.LifeInsurance;
            case 20: return InstrumentType.DepreciationObject;
            case 21: return InstrumentType.RealEstate;
            case 22: return InstrumentType.Loan;
            default: return InstrumentType.UNKNOWN;
        }
    }
}


