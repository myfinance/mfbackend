package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public abstract class AbsInstrumentHandler extends BaseInstrumentHandler{
    protected AuditService auditService;
    protected LocalDateTime ts;

    protected AbsInstrumentHandler(InstrumentDao instrumentDao, AuditService auditService) {
        super(instrumentDao);
        this.auditService = auditService;
        ts = LocalDateTime.now();
    }

    protected void saveProperty(InstrumentPropertyType instrumentPropertyType, String value, LocalDate validFrom, LocalDate validUntil) {
        checkInitStatus();
         instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, value, instrumentPropertyType.getValueType(), validFrom, validUntil));
    }

    protected void saveProperty(InstrumentPropertyType instrumentPropertyType, ValuePerDate value) {
        checkInitStatus();
        instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, String.valueOf(value.getValue()), instrumentPropertyType.getValueType(), value.getDate(), null));
   }

   protected void saveProperty(InstrumentPropertyType instrumentPropertyType, int value) {
        checkInitStatus();
        instrumentDao.saveInstrumentProperty(new InstrumentProperties(instrumentPropertyType.name(), instrumentId, String.valueOf(value), instrumentPropertyType.getValueType(), null, null));
    }

    protected void savePropertyList(InstrumentPropertyType instrumentPropertyType, List<ValuePerDate> values) {
        for(var value : values) {
            saveProperty(instrumentPropertyType, value);
        } 
    }

    public void setTreeLastChanged(LocalDateTime ts){
        this.ts = ts;
    }

    public abstract void save();
}