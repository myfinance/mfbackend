package de.hf.myfinance.test.instrumenthandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;
import de.hf.myfinance.test.mock.RecurrentTransactionDaoMockImpl;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;

public class CurrencyHandlerTest {
    @Test
    public void saveInstrumentTest(){

        var instrumentDao = new InstrumentDaoMock();
        var auditService = new AuditServiceMockImpl();
        var valueService = new ValueCurveServiceMock();
        var recurrentTransactionDao = new RecurrentTransactionDaoMockImpl();

        var instrumentFactory = new InstrumentFactory(instrumentDao, auditService, valueService, recurrentTransactionDao);

        var currencyHandler = instrumentFactory.getInstrumentHandler(InstrumentType.CURRENCY, "dollar", -1, "usd");
        currencyHandler.save();

        assertEquals(1, currencyHandler.getInstrumentId());

        var newCurrencyHandler = instrumentFactory.getInstrumentHandler(InstrumentType.CURRENCY, "", -1, "USD");
        var currency =  newCurrencyHandler.getSavedDomainObject().get();
        assertEquals("USD", currency.getBusinesskey());
        assertEquals(InstrumentType.CURRENCY, currency.getInstrumentType());
        assertEquals("dollar", currency.getDescription());
        assertEquals(1, currency.getInstrumentid());
    }
}