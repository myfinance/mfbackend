package de.hf.dac.myfinance.service.transactionhandler;

import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.service.InstrumentService;

public abstract class AbsHandler {
    protected InstrumentService instrumentService;
    protected static String ERROR_MSG = "Transaction not saved"; 

    protected AbsHandler(InstrumentService instrumentService){
        this.instrumentService = instrumentService;  
    }

    protected boolean isAccountTransferAllowed(Instrument instrument){
        switch(instrument.getInstrumentType()){
            case GIRO:
            case MONEYATCALL:
            case TIMEDEPOSIT:
            case BUILDINGSAVINGACCOUNT:
            case LIFEINSURANCE: return true;
            default: return false;
        }
    }

    abstract void save();
    
    protected void validateTenant(Instrument firstInstrument, Instrument secondInstrument) {
        Optional<Integer> tenantOfFirstInstrument = instrumentService.getRootInstrument(firstInstrument.getInstrumentid(), EdgeType.TENANTGRAPH);
        Optional<Integer> tenantOfSecInstrument = instrumentService.getRootInstrument(secondInstrument.getInstrumentid(), EdgeType.TENANTGRAPH);

        if(!tenantOfFirstInstrument.isPresent()
            || !tenantOfSecInstrument.isPresent()
            || !tenantOfFirstInstrument.get().equals(tenantOfSecInstrument.get())){
            throw new MFException(MFMsgKey.WRONG_TENENT_EXCEPTION, ERROR_MSG + ": instruments have not the same tenant");
        }
    }    
}