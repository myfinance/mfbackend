package de.hf.dac.myfinance.api.domain;

public enum RecurrentFrequency {
    Monthly(Integer.valueOf(1)),
    Quaterly(Integer.valueOf(2)),
    Yearly(Integer.valueOf(3)),
    UNKNOWN(Integer.valueOf(99));

    public static final String MONTHLY_IDSTRING = "1";
    public static final String QUATERLY_IDSTRING = "2";
    public static final String YEARLY_IDSTRING = "3";

    private final Integer value;

    RecurrentFrequency(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }

    public static RecurrentFrequency getRecurrentFrequencyById(int recurrentfrequencyId){
        switch(recurrentfrequencyId){
            case 1: return RecurrentFrequency.Monthly;
            case 2: return RecurrentFrequency.Quaterly;
            case 3: return RecurrentFrequency.Yearly;
            default: return RecurrentFrequency.UNKNOWN;
        }
    }
}
