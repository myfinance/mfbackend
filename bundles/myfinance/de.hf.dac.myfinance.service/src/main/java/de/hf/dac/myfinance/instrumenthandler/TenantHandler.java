package de.hf.dac.myfinance.instrumenthandler;

import java.util.List;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class TenantHandler extends AbsInstrumentHandler {

    public TenantHandler(InstrumentDao instrumentDao, int tenantId) {
        super(instrumentDao, tenantId);
    }

    public TenantHandler(InstrumentDao instrumentDao, Instrument tenant) {
        super(instrumentDao, tenant);
    }

    public List<Instrument> listInstruments() {
        return instrumentGraphHandler.getAllInstrumentChilds(instrumentId);
    }

    public List<Instrument> listInstruments(boolean onlyActive) {
        return instrumentGraphHandler.getAllInstrumentChilds(instrumentId, onlyActive);
    }

    public List<Instrument> listInstruments(InstrumentType instrumentType, boolean onlyActive) {
        return instrumentGraphHandler.getAllInstrumentChilds(instrumentId, instrumentType, onlyActive);
    }

}