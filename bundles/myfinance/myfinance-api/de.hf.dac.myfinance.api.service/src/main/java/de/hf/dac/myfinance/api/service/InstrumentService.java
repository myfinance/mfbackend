/** ----------------------------------------------------------------------------
 *
 * ---                              ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : InstrumentService.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.service;

import de.hf.dac.myfinance.api.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InstrumentService {
    List<Instrument> listInstruments();
    Optional<Instrument> getCurrency(String currencyCode);
    Optional<Equity> getEquity(String isin);
    List<Instrument> getSecurities();
    List<Instrument> listTenants();
    Optional<EndOfDayPrice> getEndOfDayPrice(String isin, LocalDate date);
    List<EndOfDayPrice> listEodPrices(int instrumentId);
    Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentId, LocalDate date);
    Optional<Source> getSource(int sourceId);
    Map<LocalDate, Double> getValueCurve(int instrumentId);
    Map<LocalDate, Double> getValueCurve(int instrumentId, LocalDate startDate, LocalDate endDate);
    double getValue(int instrumentId, LocalDate date);

    void saveEquity(String isin, String description);
    void saveSymbol(String isin, String symbol, String currencyCode);
    void saveCurrency(String currencyCode, String description);
    void saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDateTime lastchanged);


    void newTenant(String description, LocalDateTime ts);
    void updateInstrument(int instrumentId, String description, boolean isActive);
    void newBudget(String description, int budgetGroupId, LocalDateTime ts);
    void newGiroAccount(String description, int tenantId, LocalDateTime ts);

    void newIncomeExpense(String description, int accId, int budgetId, double value, LocalDate transactionDate, LocalDateTime ts);
    void newTransfer(String description, int srcInstrumentId, int trgInstrumentId, double value, LocalDate transactionDate, LocalDateTime ts);
    List<Transaction> listTransactions(LocalDate startDate, LocalDate endDate);
    List<Cashflow> listInstrumentCashflows(int instrumentId);
    void deleteTransaction(int transactionId);

    void importPrices(LocalDateTime ts);
    void fillPriceHistory(int sourceId, String isin, LocalDateTime ts);

}
