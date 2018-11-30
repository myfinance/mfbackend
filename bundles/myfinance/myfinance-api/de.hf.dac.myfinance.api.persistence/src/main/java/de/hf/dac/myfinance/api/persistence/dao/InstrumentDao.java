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

package de.hf.dac.myfinance.api.persistence.dao;


import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.SecuritySymbols;
import de.hf.dac.myfinance.api.domain.Source;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InstrumentDao {
    List<Instrument> listInstruments();
    Optional<Instrument> getSecurity(String isin);
    List<Instrument> getSecurities();
    Optional<Instrument> getCurrency(String currencyCode);
    Optional<Instrument> getInstrument(int instrumentId);
    Optional<EndOfDayPrice>getEndOfDayPrice(int instrumentid, LocalDate dayofprice);
    List<EndOfDayPrice> listEndOfDayPrices(int instrumentid);
    LocalDate getLastPricedDay(int instrumentid);
    Optional<Source> getSource(int sourceId);
    List<Source> getActiveSources();
    void saveInstrument(Instrument instrument);
    void saveSymbol(SecuritySymbols symbol);
    void saveEndOfDayPrice(EndOfDayPrice price);
}
