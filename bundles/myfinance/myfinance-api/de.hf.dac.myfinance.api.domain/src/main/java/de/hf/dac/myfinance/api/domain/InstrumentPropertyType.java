package de.hf.dac.myfinance.api.domain;

public enum InstrumentPropertyType {
    YIELDGOAL(Integer.valueOf(1)),
    REALESTATEPROFITS(Integer.valueOf(2));

    
    private final Integer value;

    InstrumentPropertyType(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }

    public String getValueType(){
        switch(value){
            case 1: return "double";
            case 2: return "double";
            default: return "int";
        }
    }
}
