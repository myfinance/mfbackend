package de.hf.dac.myfinance.instrumenthandler;

import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;

public interface AccountableInstrumentHandler extends InstrumentHandler {
    Optional<Integer> getTenant();
    List<Instrument> getInstrumentChilds(EdgeType edgeType, int pathlength);
    List<Integer> getAncestorIds();
}