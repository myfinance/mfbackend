package de.hf.dac.myfinance.transactionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.Transaction;
import de.hf.dac.myfinance.api.persistence.dao.CashflowDao;
import de.hf.dac.myfinance.api.persistence.dao.TransactionDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;
import de.hf.dac.myfinance.api.domain.Cashflow;

public abstract class AbsTransferHandler extends AbsTransactionHandler {
   
    protected Instrument srcInstrument;
    protected Instrument trgInstrument;
    
    protected AbsTransferHandler(InstrumentService instrumentService, 
            TransactionDao transactionDao, 
            AuditService auditService,
            ValueCurveHandler valueCurveService,
            CashflowDao cashflowDao) {
        super(instrumentService, transactionDao, auditService, valueCurveService, cashflowDao);
    }

    public void init(Instrument srcInstrument, 
            Instrument trgInstrument,
            LocalDateTime ts, 
            String description, 
            double value,
            LocalDate transactionDate) {
        super.init(ts, description, value, transactionDate);    
        this.srcInstrument = srcInstrument;
        this.trgInstrument = trgInstrument;       
        validateInstruments();
        validateTenant(this.srcInstrument, this.trgInstrument);
    }
    

    abstract void validateInstruments();

    @Override
    void updateCache() {
        valueCurveService.invalidateCache(srcInstrument.getInstrumentid());
        valueCurveService.invalidateCache(trgInstrument.getInstrumentid());
    }

    @Override
    protected Set<Cashflow> buildCashflows(double value, Transaction transaction) {
        Cashflow srcCashflow = new Cashflow(srcInstrument, value * -1);
        srcCashflow.setTransaction(transaction);
        Cashflow trgCashflow = new Cashflow(trgInstrument, value);
        trgCashflow.setTransaction(transaction);
        Set<Cashflow> cashflows = new HashSet<>();
        cashflows.add(srcCashflow);
        cashflows.add(trgCashflow);
        return cashflows;
    }     
    
    @Override
    protected void updateCashflows() {
        transaction.getCashflows().forEach(i-> {
            if( (i.getValue() < 0 && value < 0) || (i.getValue() > 0 && value > 0)) {
                cashflowDao.updateCashflow(i.getCashflowid(), value);
            } else {
                cashflowDao.updateCashflow(i.getCashflowid(), -1 * value);
            }
            valueCurveService.invalidateCache(i.getInstrument().getInstrumentid());
        });
    }
}