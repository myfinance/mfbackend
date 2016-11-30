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

import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Instrument;

import java.util.List;

public interface InstrumentService {
    List<Instrument> listInstruments();
    List<EndOfDayPrice> listPrices(String isin);
    Instrument saveInstrument(Instrument instrument);
    EndOfDayPrice savePrice(EndOfDayPrice price);
}
