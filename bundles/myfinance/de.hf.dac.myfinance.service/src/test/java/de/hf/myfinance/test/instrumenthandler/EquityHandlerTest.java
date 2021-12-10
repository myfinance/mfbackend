package de.hf.myfinance.test.instrumenthandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.instrumenthandler.securityhandler.EquityHandler;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;
import de.hf.myfinance.test.mock.RecurrentTransactionDaoMockImpl;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;

public class EquityHandlerTest  {
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
        String isin = "de12345667";
        String desc = "deutschbank";
        String symbol = "DB";
        String cur = "USD";

        var equityHandler = instrumentFactory.getInstrumentHandler(InstrumentType.EQUITY, desc, -1, isin);
        equityHandler.save();
        assertEquals(2, equityHandler.getInstrumentId());


        var newEquityHandler = (EquityHandler)instrumentFactory.getInstrumentHandler(InstrumentType.EQUITY, "", -1, isin);
        newEquityHandler.saveSymbol(symbol, cur);

        var equity =  newEquityHandler.getSavedDomainObject().get();
        assertEquals(isin.toUpperCase(), equity.getBusinesskey());
        assertEquals(InstrumentType.EQUITY, equity.getInstrumentType());
        assertEquals(desc, equity.getDescription());
        assertEquals(2, equity.getInstrumentid());
    }
}