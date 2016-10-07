/** ----------------------------------------------------------------------------
 *
 * ---                              ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : InstrumentServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service;

import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
//import de.hf.marketdataprovider.persistence.repositories.EndOfDayPriceRepository;
//import de.hf.marketdataprovider.persistence.repositories.InstrumentRepository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InstrumentServiceImpl implements InstrumentService {

    public InstrumentServiceImpl(){

    }

    /*private InstrumentRepository instrumentRepository;
    private EndOfDayPriceRepository endOfDayPriceRepository;
*/
    @Override
    public List<Instrument> listInstruments() {
        List<Instrument> instruments = new ArrayList<>();
        //instrumentRepository.findAll().forEach(i->instruments.add(i));
        return instruments;
    }

    @Override
    public List<EndOfDayPrice> listPrices(String isin) {

        return null;//endOfDayPriceRepository.findByInstrumentIsin(isin);
    }

    @Override
    public Instrument saveInstrument(Instrument instrument) {
        return null;//instrumentRepository.save(instrument);
    }

    @Override
    public EndOfDayPrice savePrice(EndOfDayPrice price) {
        return null;//endOfDayPriceRepository.save(price);
    }
}
