package de.hf.dac.myfinance.instrumenthandler.securityhandler;

import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Currency;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;

public class CurrencyHandler extends SecurityHandler {

    public CurrencyHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
    }

    public CurrencyHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, String currencyCode, String description) {
        super(instrumentDao, auditService, currencyCode, description);
    }

    @Override
    protected Optional<Instrument> getSecurity(){
        return instrumentDao.getSecurity(businesskey);
    }


    @Override
    protected void createDomainObject() {
        domainObject = new Currency(businesskey, description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "Currency";
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.CURRENCY;
    }

}