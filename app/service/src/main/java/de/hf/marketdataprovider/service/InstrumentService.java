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

package de.hf.marketdataprovider.service;

import de.hf.marketdataprovider.domain.EndOfDayPrice;
import de.hf.marketdataprovider.domain.Instrument;

import java.util.List;

public interface InstrumentService {
    List<Instrument> listInstruments();
    List<EndOfDayPrice> listPrices(String isin);
    Instrument saveInstrument(Instrument instrument);
    EndOfDayPrice savePrice(EndOfDayPrice price);
}
