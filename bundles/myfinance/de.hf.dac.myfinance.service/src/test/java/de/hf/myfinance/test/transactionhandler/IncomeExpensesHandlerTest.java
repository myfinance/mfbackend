package de.hf.myfinance.test.transactionhandler;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.BudgetHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.GiroHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.dac.myfinance.transactionhandler.IncomeExpensesHandler;
import de.hf.myfinance.test.AbsTest;


public class IncomeExpensesHandlerTest extends AbsTest {
    @Test
    public void firsttest(){
        initTest();
        IncomeExpensesHandler incomeExpensesHandler = new IncomeExpensesHandler(
            instrumentService,
            transactionDaoMock, 
            auditService, 
            valueCurveHandler,
            cashflowDao);
        String yearStr = "2020";
        Year year = Year.parse(yearStr);
        LocalDate date = year.atMonth(Month.JANUARY).atDay(1);
        
        var tenantHandler = new TenantHandler(instrumentDao, auditService, instrumentFactory, "tenant");
        tenantHandler.save();
        var tenantId = tenantHandler.getInstrumentId();
        //Tenant, AccountPF, BudgetPF, BudgetGroup and IncomeBudget should be created
        assertEquals(5, instrumentDao.listInstruments().size());
        int budgetGroupId = 0;
        var instruments = instrumentDao.listInstruments();
        for (Instrument instrument : instruments) {
            if(instrument.getInstrumentType().equals(InstrumentType.BUDGETGROUP)){
                budgetGroupId = instrument.getInstrumentid();
            }
        }
        var giroHandler = new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, "testgiro", tenantId, "testgiro");
        giroHandler.save();
        var budgetHandler = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, "testbudget", budgetGroupId, "testbudget");
        budgetHandler.save();
        incomeExpensesHandler.init(giroHandler.getInstrumentId(), budgetHandler.getInstrumentId(), ts, "testincome", 1000, date);
        incomeExpensesHandler.save();
        int transactionId = 1;
        assertEquals(2, transactionDaoMock.getTransaction(transactionId).get().getCashflows().size());
        ArrayList<Cashflow> cashflows = new ArrayList<Cashflow>();
        cashflows.addAll(transactionDaoMock.getTransaction(transactionId).get().getCashflows());
        assertEquals(true, cashflows.get(0).getValue()==(cashflows.get(1).getValue()));
        assertEquals(TransactionType.INCOMEEXPENSES, transactionDaoMock.getTransaction(transactionId).get().getTransactionType());

    }
}