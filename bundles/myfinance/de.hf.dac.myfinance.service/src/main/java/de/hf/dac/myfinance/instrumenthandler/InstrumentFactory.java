package de.hf.dac.myfinance.instrumenthandler;

import java.util.List;
import java.util.stream.Collectors;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.persistence.dao.RecurrentTransactionDao;
import de.hf.dac.myfinance.api.service.ValueCurveService;

public class InstrumentFactory {

    private final InstrumentDao instrumentDao;
    private final AuditService auditService;
    private final ValueCurveService valueService;
    private final RecurrentTransactionDao recurrentTransactionDao;

    public InstrumentFactory(InstrumentDao instrumentDao, AuditService auditService, ValueCurveService valueService, RecurrentTransactionDao recurrentTransactionDao) {
        this.instrumentDao = instrumentDao;
        this.auditService = auditService;
        this.valueService = valueService;
        this.recurrentTransactionDao = recurrentTransactionDao;
    }

    public BaseAccountableInstrumentHandler getBaseInstrumentHandler(int instrumentId) {
        return new BaseAccountableInstrumentHandlerImpl(instrumentDao, auditService, instrumentId);
    }

    public InstrumentHandler getInstrumentHandler(int instrumentId) {
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
                return new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, instrument);      
            case GIRO: 
                return new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, instrument);      
            case DEPOT: 
                return new DepotHandler(instrumentDao, auditService, instrument);     
            case REALESTATE: 
                return new RealEstateHandler(instrumentDao, auditService, this, instrument);                                                               
            default:
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "can not create Instrumenthandler for instrumentType:"+instrument.getInstrumentType());
        }
    }

    public InstrumentHandler getInstrumentHandler(InstrumentType instrumentType, String description, int parentId) {
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
                return new BudgetHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, description, parentId);          
            case GIRO: 
                return new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, description, parentId);      
            case DEPOT: 
                return new DepotHandler(instrumentDao, auditService, description, parentId);  
            case REALESTATE: 
                return new RealEstateHandler(instrumentDao, auditService, this, description, parentId);                                                                 
            default:
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "can not create Instrumenthandler for instrumentType:"+instrumentType);
        }
    }

    public TenantHandler getTenantHandler(int instrumentId, boolean validate) {
        var tenantHandler = new TenantHandler(instrumentDao, auditService, this, instrumentId); 
        if(validate) {
            tenantHandler.validateInstrument();  
        } 
        return tenantHandler;   
    }

    public BudgetGroupHandler getBudgetGroupHandler(int instrumentId, boolean validate) {
        var budgetGroupHandler = new BudgetGroupHandler(instrumentDao, auditService, this, instrumentId); 
        if(validate) {
            budgetGroupHandler.validateInstrument();  
        } 
        return budgetGroupHandler;   
    }

    public List<Instrument> listInstruments() {
        return instrumentDao.listInstruments();
    }

    public List<Instrument> listTenants(){
        return listInstruments().stream().filter(i->i.getInstrumentType().equals(InstrumentType.TENANT)).collect(Collectors.toList());
    }
}