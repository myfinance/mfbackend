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

package de.hf.dac.marketdataprovider.api.service;

import de.hf.dac.marketdataprovider.api.domain.Currency;
import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.Source;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InstrumentService {
    List<Instrument> listInstruments();
    List<EndOfDayPrice> listPrices(String isin);
    Optional<Currency> getCurrency(String currencyCode);
    Optional<Security> getSecurity(String isin);
    List<Security> getSecurities();
    Optional<EndOfDayPrice> getEndOfDayPrice(String isin, LocalDate date);
    Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentId, LocalDate date);
    Optional<Source> getSource(String description);

    String saveSecurity(String isin, String description);
    String saveSymbol(String isin, String symbol, String currencyCode);
    String saveCurrency(String currencyCode, String description);
    String saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDate lastchanged);
    void importPrices();
}
