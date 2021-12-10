/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : EndOfDayPriceDao.java
 * Author(s)   : xn01598
 * Created     : 05.04.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.persistence.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Source;

public interface EndOfDayPriceDao {
    Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentid, LocalDate dayofprice);
    List<EndOfDayPrice> listEndOfDayPrices(int instrumentid);
    LocalDate getLastPricedDay(int instrumentid);
    void saveEndOfDayPrice(EndOfDayPrice price);
    Optional<Source> getSource(int sourceId);
    List<Source> getActiveSources();
}
