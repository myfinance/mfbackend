package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

/**
 * Handles all the functionality which is not type specific.  
 * E.G. if you have an Id and don't care which instrumenttype it is because you just want to know the parent, then you do not have to load the instrument and use the the simpleInstrumentHandler
 */
public class SimpleInstrumentHandler extends AbsInstrumentHandler{

    public SimpleInstrumentHandler(InstrumentDao instrumentDao, int instrumentId) {
        super(instrumentDao, instrumentId);
    }
}