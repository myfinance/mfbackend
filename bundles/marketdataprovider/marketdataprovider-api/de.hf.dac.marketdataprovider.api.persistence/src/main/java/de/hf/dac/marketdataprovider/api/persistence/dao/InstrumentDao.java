/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : InstrumentDao.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 28.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.persistence.dao;


import de.hf.dac.marketdataprovider.api.domain.Currency;
import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.SecuritySymbols;
import de.hf.dac.marketdataprovider.api.domain.Source;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InstrumentDao {
    List<Instrument> listInstruments();
    Optional<Security> getSecurity(String isin);
    List<Security> getSecurities();
    Optional<Currency> getCurrency(String currencyCode);
    Optional<Instrument> getInstrument(int instrumentId);
    Optional<EndOfDayPrice>getEndOfDayPrice(int instrumentid, LocalDate dayofprice);
    List<EndOfDayPrice> listEndOfDayPrices(int instrumentid);
    LocalDate getLastPricedDay(int instrumentid);
    Optional<Source> getSource(int sourceId);
    List<Source> getActiveSources();
    void saveSecurity(Security instrument);
    void saveCurrency(Currency currency);
    void saveSymbol(SecuritySymbols symbol);
    void saveEndOfDayPrice(EndOfDayPrice price);
}
