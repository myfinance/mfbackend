package de.hf.dac.myfinance.instrumenthandler;

import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;

public interface BaseAccountableInstrumentHandler {
    List<Integer> getAncestorIds();
    List<Instrument> getInstrumentChilds(EdgeType edgeType, int pathlength);
    Optional<Integer> getTenant();
    Instrument getInstrument();
    Instrument getInstrument(String errMsg);
    List<InstrumentProperties> getInstrumentProperties();
}