package de.hf.myfinance.test.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.transactionhandler.AbsTransferHandler;
import de.hf.dac.myfinance.transactionhandler.TransactionHandlerFactory;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.CashflowDaoMock;
import de.hf.myfinance.test.mock.TradeDaoMock;
import de.hf.myfinance.test.mock.InstrumentServiceTestImpl;
import de.hf.myfinance.test.mock.TransactionDaoMock;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;

public class BudgetTransferHandlerTest {
    @Test
    public void firstbudgettransfertest(){
        TransactionDaoMock transactionDaoMock = new TransactionDaoMock();
        InstrumentService instrumentService = new InstrumentServiceTestImpl();
        var srcInstrument = instrumentService.getInstrument(2, "Can not add transfer: Unknown instrument:");
        var trgInstrument = instrumentService.getInstrument(4, "Can not add transfer: Unknown instrument:");

        TransactionHandlerFactory transactionHandlerFactory = new TransactionHandlerFactory(instrumentService, transactionDaoMock, new AuditServiceMockImpl(), new ValueCurveServiceMock(), new CashflowDaoMock(), new TradeDaoMock());

        AbsTransferHandler transactionHandler = transactionHandlerFactory.createTransferOrBudgetTransferHandler(srcInstrument);
        transactionHandler.init(srcInstrument, trgInstrument, LocalDateTime.now(), "testbudgettransfer", 100, LocalDate.now());
        transactionHandler.save();
        assertEquals(2, transactionDaoMock.getTransaction().getCashflows().size());
        ArrayList<Cashflow> cashflows = new ArrayList<Cashflow>();
        cashflows.addAll(transactionDaoMock.getTransaction().getCashflows());
        assertEquals(true, cashflows.get(0).getValue()==(cashflows.get(1).getValue()*(-1)));
        assertEquals(TransactionType.BUDGETTRANSFER, transactionDaoMock.getTransaction().getTransactionType());

    }
}