package de.hf.dac.myfinance.api.domain;

public class InstrumentValuesTuple {
    double value;
    LiquidityType liquidityType;
    double valueChange;

    public InstrumentValuesTuple(double value, LiquidityType liquidityType, double valueChange) {
        this.value = value;
        this.liquidityType = liquidityType;
        this.valueChange = valueChange;
    }

    public LiquidityType getLiquidityType() {
        return liquidityType;
    }

    public double getValue() {
        return value;
    }

    public double getValueChange() {
        return valueChange;
    }
}