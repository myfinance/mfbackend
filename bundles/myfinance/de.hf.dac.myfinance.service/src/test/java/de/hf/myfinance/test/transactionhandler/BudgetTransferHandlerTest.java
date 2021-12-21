package de.hf.myfinance.test.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hf.dac.myfinance.api.domain.Cashflow;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.TransactionType;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.BudgetHandler;
import de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler.TenantHandler;
import de.hf.dac.myfinance.transactionhandler.AbsTransferHandler;
import de.hf.dac.myfinance.transactionhandler.TransactionHandlerFactory;
import de.hf.myfinance.test.AbsTest;

public class BudgetTransferHandlerTest extends AbsTest{
    @Test
    public void firstbudgettransfertest(){
        initTest();

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
        var budgetHandler = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, "testbudget", budgetGroupId, "testbudget");
        budgetHandler.save();
        var budgetHandler2 = new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, "testbudget2", budgetGroupId, "testbudget2");
        budgetHandler2.save();

        TransactionHandlerFactory transactionHandlerFactory = new TransactionHandlerFactory(instrumentService, transactionDao, auditService, cashflowDao, tradeDao);

        AbsTransferHandler transactionHandler = transactionHandlerFactory.createTransferOrBudgetTransferHandler(budgetHandler.getInstrument());
        transactionHandler.init(budgetHandler.getInstrument(), budgetHandler2.getInstrument(), ts, "testbudgettransfer", 100, LocalDate.now());
        transactionHandler.save();
        int transactionId = 1;
        assertEquals(2, transactionDao.getTransaction(transactionId).get().getCashflows().size());
        ArrayList<Cashflow> cashflows = new ArrayList<Cashflow>();
        cashflows.addAll(transactionDao.getTransaction(transactionId).get().getCashflows());
        assertEquals(true, cashflows.get(0).getValue()==(cashflows.get(1).getValue()*(-1)));
        assertEquals(TransactionType.BUDGETTRANSFER, transactionDao.getTransaction(transactionId).get().getTransactionType());

    }
}