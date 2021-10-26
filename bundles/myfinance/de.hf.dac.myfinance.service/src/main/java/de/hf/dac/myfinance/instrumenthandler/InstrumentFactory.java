package de.hf.dac.myfinance.instrumenthandler;

import java.util.List;
import java.util.stream.Collectors;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class InstrumentFactory {

    private final InstrumentDao instrumentDao;
    private final AuditService auditService;

    public InstrumentFactory(InstrumentDao instrumentDao, AuditService auditService) {
        this.instrumentDao = instrumentDao;
        this.auditService = auditService;
    }

    public BaseInstrumentHandler getBaseInstrumentHandler(int instrumentId) {
        return new BaseInstrumentHandler(instrumentDao, instrumentId);
    }

    public AbsInstrumentHandler getInstrumentHandler(int instrumentId) {
        var instrument =  getBaseInstrumentHandler(instrumentId).getInstrument();
        switch(instrument.getInstrumentType()){
            case TENANT: 
                return new TenantHandler(instrumentDao, auditService, this, instrument); 
            case BUDGETPORTFOLIO: 
                return new BudgetPortfolioHandler(instrumentDao, auditService, instrument);    
            case ACCOUNTPORTFOLIO: 
                return new AccountPortfolioHandler(instrumentDao, auditService, instrument);                      
            case BUDGETGROUP: 
                return new BudgetGroupHandler(instrumentDao, auditService, this, instrument);                             
            case BUDGET: 
                return new BudgetHandler(instrumentDao, auditService, instrument);      
            case GIRO: 
                return new GiroHandler(instrumentDao, auditService, instrument);      
            case DEPOT: 
                return new DepotHandler(instrumentDao, auditService, instrument);                                                 
            default:
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "can not create Instrumenthandler for instrumentType:"+instrument.getInstrumentType());
        }
    }

    public AbsInstrumentHandler getInstrumentHandler(InstrumentType instrumentType, String description, int parentId) {
        switch(instrumentType){
            case TENANT: 
                return new TenantHandler(instrumentDao, auditService, this, description);     
            case BUDGETPORTFOLIO: 
                return new BudgetPortfolioHandler(instrumentDao, auditService, description, parentId);    
            case ACCOUNTPORTFOLIO: 
                return new AccountPortfolioHandler(instrumentDao, auditService, description, parentId);                                   
            case BUDGETGROUP: 
                return new BudgetGroupHandler(instrumentDao, auditService, this, description, parentId);  
            case BUDGET: 
                return new BudgetHandler(instrumentDao, auditService, description, parentId);          
            case GIRO: 
                return new GiroHandler(instrumentDao, auditService, description, parentId);      
            case DEPOT: 
                return new DepotHandler(instrumentDao, auditService, description, parentId);                                              
            default:
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "can not create Instrumenthandler for instrumentType:"+instrumentType);
        }
    }

    public TenantHandler getTenantHandler(int instrumentId, boolean validate) {
        if(validate) {
            var instrument =  getInstrument(instrumentId, "");
            if(!instrument.getInstrumentType().equals(InstrumentType.TENANT)) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create tenantthandler for instrumentid:"+instrumentId);
            }
            return new TenantHandler(instrumentDao, auditService, this, instrument);   
        } 
        return new TenantHandler(instrumentDao, auditService, this, instrumentId);   
    }

    public BudgetGroupHandler getBudgetGroupHandler(int instrumentId, boolean validate) {
        if(validate) {
            var instrument =  getInstrument(instrumentId, "");
            if(!instrument.getInstrumentType().equals(InstrumentType.BUDGETGROUP)) {
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create BudgetGroupHandler for instrumentid:"+instrumentId);
            }
            return new BudgetGroupHandler(instrumentDao, auditService, this, instrument);   
        } 
        return new BudgetGroupHandler(instrumentDao, auditService, this, instrumentId);   
    }

    public List<Instrument> listInstruments() {
        return instrumentDao.listInstruments();
    }

    public List<Instrument> listTenants(){
        return listInstruments().stream().filter(i->i.getInstrumentType().equals(InstrumentType.TENANT)).collect(Collectors.toList());
    }
}