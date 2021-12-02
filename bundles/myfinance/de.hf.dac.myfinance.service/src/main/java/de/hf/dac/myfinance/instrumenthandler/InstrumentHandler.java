package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;

public interface InstrumentHandler {
    Instrument getInstrument();
    Instrument getInstrument(String errMsg);
    int getInstrumentId();
    List<InstrumentProperties> getInstrumentProperties();
    List<InstrumentProperties> getInstrumentProperties(InstrumentPropertyType instrumentPropertyType);
    void validateInstrument();
    void setTreeLastChanged(LocalDateTime ts);
    void save();
    void setActive(boolean isActive);
    void setDescription(String description);
    Optional<Instrument> getSavedDomainObject() ;
}