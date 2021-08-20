package de.hf.dac.myfinance.api.persistence.dao;

public interface TradeDao  {
    void updateTrade(int tradeid, double amount);
}
