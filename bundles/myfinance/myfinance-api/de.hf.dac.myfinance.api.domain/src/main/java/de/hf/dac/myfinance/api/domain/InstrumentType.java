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
    Giro(Integer.valueOf(1)),
    MoneyAtCall(Integer.valueOf(2)),
    TimeDeposit(Integer.valueOf(3)),
    BuildingsavingAccount(Integer.valueOf(4)),
    Budget(Integer.valueOf(5)),
    Tenant(Integer.valueOf(6)),
    //BudgetGroupPortfolio(new Integer(7)),
    AccountPortfolio(Integer.valueOf(8)),
    ArtificialPortfolio(Integer.valueOf(9)),
    BudgetGroup(Integer.valueOf(10)),
    Depot(Integer.valueOf(10)),
    Buildingsaving(Integer.valueOf(12)),
    Currency(Integer.valueOf(13)),
    Equity(Integer.valueOf(14)),
    Fonds(Integer.valueOf(15)),
    ETF(Integer.valueOf(16)),
    Index(Integer.valueOf(17)),
    Bond(Integer.valueOf(18)),
    LifeInsurance(Integer.valueOf(19)),
    DepreciationObject(Integer.valueOf(20)),
    RealEstate(Integer.valueOf(21)),
    Loan(Integer.valueOf(22)),
    UNKNOWN(Integer.valueOf(99));

    public static final String GIRO_IDSTRING = "1";
    public static final String MONEYATCALL_IDSTRING = "2";
    public static final String TIMEDEPOSIT_IDSTRING = "3";
    public static final String BUILDINGSAVINGACCOUNT_IDSTRING = "4";
    public static final String BUDGET_IDSTRING = "5";
    public static final String TENANT_IDSTRING = "6";
    //public static final String BUDGETGROUPPORTFOLIO_IDSTRING = "7";
    public static final String ACCOUNTPORTFOLIO_IDSTRING = "8";
    public static final String ARTIFICIALPORTFOLIO_IDSTRING = "9";
    public static final String BUDGETGROUP_IDSTRING = "10";
    public static final String DEPOT_IDSTRING = "11";
    public static final String BUILDINGSAVING_IDSTRING = "12";
    public static final String CURRENCY_IDSTRING = "13";
    public static final String EQUITY_IDSTRING = "14";
    public static final String FONDS_IDSTRING = "15";
    public static final String ETF_IDSTRING = "16";
    public static final String INDEX_IDSTRING = "17";
    public static final String BOND_IDSTRING = "18";
    public static final String LIFEINSURANCE_IDSTRING = "19";
    public static final String DEPRECATIONOBJECT_IDSTRING = "20";
    public static final String REALESTATE_IDSTRING = "21";
    public static final String LOAN_IDSTRING = "22";

    private final Integer value;

    InstrumentType(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }

    public InstrumentTypeGroup getTypeGroup(){
        switch(value){
            case 6: return InstrumentTypeGroup.TENANT;
            //case 7: return InstrumentTypeGroup.PORTFOLIO;
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
            //case 7: return InstrumentType.BudgetGroupPortfolio;
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


