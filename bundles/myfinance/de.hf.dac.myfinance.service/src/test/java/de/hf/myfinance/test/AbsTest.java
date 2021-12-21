package de.hf.myfinance.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.persistence.dao.TradeDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.PriceService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveCache;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;
import de.hf.dac.myfinance.api.service.ValueService;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;
import de.hf.dac.myfinance.service.InstrumentServiceImpl;
import de.hf.dac.myfinance.service.PriceServiceImpl;
import de.hf.dac.myfinance.service.SimpleCurveCache;
import de.hf.dac.myfinance.service.TransactionServiceImpl;
import de.hf.dac.myfinance.valuehandler.ValueCurveHandlerImpl;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.CashflowDaoMock;
import de.hf.myfinance.test.mock.EndOfDayPriceDaoTestImpl;
import de.hf.myfinance.test.mock.InstrumentDaoMock;
import de.hf.myfinance.test.mock.RecurrentTransactionDaoMockImpl;
import de.hf.myfinance.test.mock.TradeDaoMock;
import de.hf.myfinance.test.mock.TransactionDaoMock;
import de.hf.myfinance.test.mock.TransactionServiceMock;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;

public abstract class AbsTest {
    protected DateTimeFormatter formatter;
    protected InstrumentDao instrumentDao;
    protected AuditService auditService;
    protected InstrumentFactory instrumentFactory;
    protected ValueCurveHandler valueCurveHandler;
    protected ValueService valueService;
    protected RecurrentTransactionDao recurrentTransactionDao;
    protected InstrumentService instrumentService;
    protected TransactionDao transactionDao;
    protected CashflowDao cashflowDao;
    protected TransactionService transactionService;
    protected TradeDao tradeDao;
    protected ValueCurveCache cache;
    protected EndOfDayPriceDao endOfDayPriceDao;
    protected LocalDateTime ts;
    protected PriceService priceService;

    public void initTest() {
        ts = LocalDateTime.now();
        transactionDao = new TransactionDaoMock();
        cashflowDao = new CashflowDaoMock();
        tradeDao = new TradeDaoMock();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        instrumentDao = new InstrumentDaoMock();
        auditService = new AuditServiceMockImpl();
        recurrentTransactionDao = new RecurrentTransactionDaoMockImpl();
        valueService = new ValueCurveServiceMock();
        instrumentService = new InstrumentServiceImpl(instrumentDao, recurrentTransactionDao, auditService, valueService);
        instrumentFactory = new InstrumentFactory(instrumentDao, auditService, valueService, recurrentTransactionDao);
        endOfDayPriceDao = new EndOfDayPriceDaoTestImpl();
        cache = new SimpleCurveCache();
        cache.flush();
        priceService = new PriceServiceImpl(endOfDayPriceDao, instrumentService, null, auditService);
        transactionService = new TransactionServiceImpl(instrumentService, transactionDao, recurrentTransactionDao, cashflowDao, tradeDao, auditService);
        valueCurveHandler = new ValueCurveHandlerImpl(instrumentService, priceService, cache, transactionService);
        
    }

}