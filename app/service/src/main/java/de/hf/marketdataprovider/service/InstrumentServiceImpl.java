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
 *  Author(s)   : xn01598
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.service;

import de.hf.marketdataprovider.domain.EndOfDayPrice;
import de.hf.marketdataprovider.domain.Instrument;
import de.hf.marketdataprovider.persistence.repositories.EndOfDayPriceRepository;
import de.hf.marketdataprovider.persistence.repositories.InstrumentRepository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//@Service
@Data
public class InstrumentServiceImpl implements InstrumentService {


    //@Autowired
    private InstrumentRepository instrumentRepository;
    //@Autowired
    private EndOfDayPriceRepository endOfDayPriceRepository;

    @Override
    public List<Instrument> listInstruments() {
        List<Instrument> instruments = new ArrayList<>();
        instrumentRepository.findAll().forEach(i->instruments.add(i));
        return instruments;
    }

    @Override
    public List<EndOfDayPrice> listPrices(String isin) {
        return endOfDayPriceRepository.findByInstrumentIsin(isin);
    }

    @Override
    public Instrument saveInstrument(Instrument instrument) {
        return instrumentRepository.save(instrument);
    }

    @Override
    public EndOfDayPrice savePrice(EndOfDayPrice price) {
        return endOfDayPriceRepository.save(price);
    }
}
