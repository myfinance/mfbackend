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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InstrumentService {
    List<Instrument> listInstruments();
    List<Instrument> listInstruments(int tenantId);
    List<Instrument> listInstruments(int tenantId, boolean onlyActive);
    List<Instrument> listInstruments(int tenantId, InstrumentType instrumentType, boolean onlyActive);
    List<Instrument> listAccounts(int tenantId);
    List<Instrument> listTenants();
    Optional<Instrument> getInstrument(int instrumentId);
    Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType);
    Instrument getIncomeBudget(int tenantId);
    void newTenant(String description, LocalDateTime ts);
    void updateInstrument(int instrumentId, String description, boolean isActive);
    void newBudget(String description, int budgetGroupId, LocalDateTime ts);
    void newGiroAccount(String description, int tenantId, LocalDateTime ts);
    Optional<Instrument> getCurrency(String currencyCode);
    Optional<Equity> getEquity(String isin);
    List<Instrument> getSecurities();
    void saveEquity(String isin, String description);
    void saveSymbol(String isin, String symbol, String currencyCode);
    void saveCurrency(String currencyCode, String description);
}
