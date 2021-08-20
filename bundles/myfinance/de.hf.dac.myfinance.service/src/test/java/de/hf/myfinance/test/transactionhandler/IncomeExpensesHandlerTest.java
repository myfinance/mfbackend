package de.hf.myfinance.test.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.service.transactionhandler.IncomeExpensesHandler;
import de.hf.myfinance.test.mock.AuditServiceMockImpl;
import de.hf.myfinance.test.mock.CashflowDaoMock;
import de.hf.myfinance.test.mock.InstrumentServiceTestImpl;
import de.hf.myfinance.test.mock.TransactionDaoMock;
import de.hf.myfinance.test.mock.ValueCurveServiceMock;


public class IncomeExpensesHandlerTest {
    @Test
    public void firsttest(){
        TransactionDaoMock transactionDaoMock = new TransactionDaoMock();
        IncomeExpensesHandler incomeExpensesHandler = new IncomeExpensesHandler(
            new InstrumentServiceTestImpl(),
            transactionDaoMock, 
            new AuditServiceMockImpl(), 
            new ValueCurveServiceMock(),
            new CashflowDaoMock());
        String yearStr = "2020";
        Year year = Year.parse(yearStr);
        LocalDate date = year.atMonth(Month.JANUARY).atDay(1);
        incomeExpensesHandler.init(1, 2, LocalDateTime.now(), "testincome", 1000, date);
        incomeExpensesHandler.save();
        assertEquals(2, transactionDaoMock.getTransaction().getCashflows().size());
        ArrayList<Cashflow> cashflows = new ArrayList<Cashflow>();
        cashflows.addAll(transactionDaoMock.getTransaction().getCashflows());
        assertEquals(true, cashflows.get(0).getValue()==(cashflows.get(1).getValue()));
        assertEquals(TransactionType.INCOMEEXPENSES, transactionDaoMock.getTransaction().getTransactionType());

    }
}