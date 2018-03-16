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

import de.hf.dac.myfinance.api.domain.Currency;
import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.Security;
import de.hf.dac.myfinance.api.domain.Source;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InstrumentService {
    List<Instrument> listInstruments();
    Optional<Currency> getCurrency(String currencyCode);
    Optional<Security> getSecurity(String isin);
    List<Security> getSecurities();
    Optional<EndOfDayPrice> getEndOfDayPrice(String isin, LocalDate date);
    List<EndOfDayPrice> listEodPrices(int instrumentId);
    Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentId, LocalDate date);
    Optional<Source> getSource(int sourceId);
    Map<LocalDate, Double> getValueCurve(int instrumentId);
    Map<LocalDate, Double> getValueCurve(int instrumentId, LocalDate startDate, LocalDate endDate);
    double getValue(int instrumentId, LocalDate date);

    String saveSecurity(String isin, String description);
    String saveSymbol(String isin, String symbol, String currencyCode);
    String saveCurrency(String currencyCode, String description);
    String saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDateTime lastchanged);
    String importPrices(LocalDateTime ts);
    String fillPriceHistory(int sourceId, String isin, LocalDateTime ts);
}
