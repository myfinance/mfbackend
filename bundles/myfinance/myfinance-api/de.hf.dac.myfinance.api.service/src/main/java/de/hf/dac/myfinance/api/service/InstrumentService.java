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
import java.util.List;
import java.util.Optional;

public interface InstrumentService {
    List<Instrument> listInstruments();
    List<Instrument> listInstruments(int tenantId);
    List<Instrument> listInstruments(int tenantId, boolean onlyActive);
    List<Instrument> listInstruments(int tenantId, InstrumentType instrumentType, boolean onlyActive);
    List<Instrument> listAccounts(int tenantId);
    List<Instrument> listTenants();
    Instrument getInstrument(int instrumentId);
    Instrument getInstrument(int instrumentId, String errMsg);
    Optional<Integer> getTenant(int instrumentId);
    List<Integer> getParentIds(int instrumentId);
    List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength);
    Instrument getIncomeBudget(int tenantId);
    List<InstrumentProperties> getInstrumentProperties(int instrumentId);
    void newTenant(String description);
    void updateInstrument(int instrumentId, String description, boolean isActive);
    void newBudget(String description, int budgetGroupId);
    void newGiroAccount(String description, int tenantId);
    void newDepotAccount(String description, int tenantId, int defaultGiroId, int valueBudgetId);
    void updateDepotAccount(int instrumentId, String description, boolean isActive, int defaultGiroId);
    void newRealEstate(String description, int tenantId, int valueBudgetId, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits);
    void updateRealEstate(int instrumentId, String description, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits, boolean isActive);
    Optional<Instrument> getCurrency(String currencyCode);
    Optional<Instrument> getEquity(String isin);
    Instrument getSecurity(String isin);
    Instrument getSecurity(String isin, String errMsg);
    List<Instrument> getSecurities();
    void saveEquity(String isin, String description);
    void saveFullEquity(String theisin, String description, List<String[]> symbols);
    void saveSymbol(String isin, String symbol, String currencyCode);
    void deleteSymbols(String theisin);
    void saveCurrency(String currencyCode, String description);
}
