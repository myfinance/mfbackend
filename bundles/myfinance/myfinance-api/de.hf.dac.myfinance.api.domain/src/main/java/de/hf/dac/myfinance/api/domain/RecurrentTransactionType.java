package de.hf.dac.myfinance.api.domain;

public enum RecurrentTransactionType {
    Income(Integer.valueOf(1)),
    Expenses(Integer.valueOf(2)),
    Transfer(Integer.valueOf(3)),
    BudgetTransfer(Integer.valueOf(4)),
    UNKNOWN(Integer.valueOf(99));

    public static final String INCOME_IDSTRING = "1";
    public static final String EXPENSES_IDSTRING = "2";
    public static final String TRANSFER_IDSTRING = "3";
    public static final String BUDGETTRANSFER_IDSTRING = "4";

    private final Integer value;

    RecurrentTransactionType(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }

    public static RecurrentTransactionType getRecurrentTransactionTypeById(int recurrentTransactionTypeId){
        switch(recurrentTransactionTypeId){
            case 1: return RecurrentTransactionType.Income;
            case 2: return RecurrentTransactionType.Expenses;
            case 3: return RecurrentTransactionType.Transfer;
            case 4: return RecurrentTransactionType.BudgetTransfer;
            default: return RecurrentTransactionType.UNKNOWN;
        }
    }
}
