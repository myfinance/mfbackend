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
import java.util.List;
import java.util.Optional;

public interface InstrumentDao {
    List<Instrument> listInstruments();
    Optional<Instrument> getEquity(String isin);
    Optional<Instrument> getSecurity(String businesskey);
    List<Instrument> getSecurities();
    Optional<Instrument> getCurrency(String currencyCode);
    Optional<Instrument> getInstrument(int instrumentId);
    void saveInstrument(Instrument instrument);
    void updateInstrument(int instrumentId, String description, boolean isActive);
    void saveSymbol(SecuritySymbols symbol);
    String deleteSymbols(int symbolId);
    List<InstrumentGraphEntry> getAncestorGraphEntries(int instrumentId, EdgeType edgeType);
    Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType);
    void saveGraphEntry(InstrumentGraphEntry instrumentGraphEntry);
    List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType);
    List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength);
    Optional<Instrument> getAccountPortfolio(int tenantId);
    Optional<Instrument> getBudgetPortfolio(int tenantId);
    void saveInstrumentProperty(InstrumentProperties instrumentProperty);
    List<InstrumentProperties> getInstrumentProperties(int instrumentId);
    String deleteInstrumentProperty(int instrumentPropertyId);
}
