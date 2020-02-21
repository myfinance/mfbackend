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


import de.hf.dac.myfinance.api.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InstrumentDao {
    List<Instrument> listInstruments();
    Optional<Equity> getEquity(String isin);
    Optional<Instrument> getSecurity(String businesskey);
    List<Instrument> getSecurities();
    Optional<Instrument> getCurrency(String currencyCode);
    Optional<Instrument> getInstrument(int instrumentId);
    Optional<Source> getSource(int sourceId);
    List<Source> getActiveSources();
    Map<LocalDate, Cashflow> getInstrumentCashflowMap(int instrumentId);
    List<Cashflow> listInstrumentCashflows(int instrumentId);
    void saveInstrument(Instrument instrument);
    void updateInstrument(int instrumentId, String description, boolean isActive);
    void saveSymbol(SecuritySymbols symbol);
    List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType);
    Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType);
    void saveGraphEntry(InstrumentGraphEntry instrumentGraphEntry);
    List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType);
    Optional<Instrument> getAccountPortfolio(int tenantId);
}
