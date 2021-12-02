package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public abstract class AbsInstrumentHandlerWithProperty extends AbsInstrumentHandler {

    protected AbsInstrumentHandlerWithProperty(InstrumentDao instrumentDao, AuditService auditService, String description, String businesskey) {
        super(instrumentDao, auditService, description, businesskey);
    }

    protected AbsInstrumentHandlerWithProperty(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
    }

    protected AbsInstrumentHandlerWithProperty(InstrumentDao instrumentDao, AuditService auditService, Instrument instrument) {
        super(instrumentDao, auditService, instrument);
    }

    private List<InstrumentProperties> properties;

    public List<InstrumentProperties> getInstrumentProperties() {
        checkInitStatus();
        if(!isPropertyInit) {
            properties = instrumentDao.getInstrumentProperties(instrumentId);
        }
        return properties;
    }

    public List<InstrumentProperties> getInstrumentProperties(InstrumentPropertyType instrumentPropertyType) {
        return getInstrumentProperties().stream().filter(i->i.getPropertyname().equals(instrumentPropertyType.name())).collect(Collectors.toList());
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

    protected void deleteInstrumentPropertyList() {
        var instrumentProperties = instrumentDao.getInstrumentProperties(instrumentId);
        for (InstrumentProperties instrumentProperty : instrumentProperties) {
            auditService.saveMessage(instrumentDao.deleteInstrumentProperty(instrumentProperty.getPropertyid()),
                Severity.INFO, AUDIT_MSG_TYPE);
        }    
    }

}