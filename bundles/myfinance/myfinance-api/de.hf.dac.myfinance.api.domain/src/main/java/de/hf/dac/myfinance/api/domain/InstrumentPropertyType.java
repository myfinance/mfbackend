package de.hf.dac.myfinance.api.domain;

public enum InstrumentPropertyType {
    YIELDGOAL(Integer.valueOf(1)),
    REALESTATEPROFITS(Integer.valueOf(2)),
    DEFAULTGIROID(Integer.valueOf(3));

    
    private final Integer value;

    InstrumentPropertyType(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }

    public String getValueType(){
        switch(value){
            case 1: return "double";
            case 2: return "double";
            case 3: return "int";
            default: return "int";
        }
    }

    public String getStringValue(){
        switch(value){
            case 1: return "YIELDGOAL";
            case 2: return "REALESTATEPROFITS";
            case 3: return "DEFAULTGIROID";
            default: return "NA";
        }
    }
}
