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

    /**
     * returns a baseinstrumenthandler with type indifferent functions like getTenant().
     * use this if you do not know the type and it doesn't matter for your purpose
     * @param instrumentId the id of the instrument
     * @return the BaseAccountableInstrumentHandler
     */
    public BaseAccountableInstrumentHandler getBaseInstrumentHandler(int instrumentId) {
        return new BaseAccountableInstrumentHandlerImpl(instrumentDao, auditService, instrumentId);
    }

    /**
     * returns a BaseSecurityHandler with type and Tenant indifferent functions like getSecurities().
     * use this if you do not know the type and it doesn't matter for your purpose
     * @return the getBaseSecurityHandler
     */
    public BaseSecurityHandler getBaseSecurityHandler() {
        return new BaseSecurityHandler(instrumentDao, auditService);
    }

    /**
     * creates an Instrumenthandler for a new Instrument
     * @param instrumentType the type of the new instrument
     * @param description the description
     * @param parentId the id of the parent
     * @return Instrumenthandler for the instrumenttype of the new instrument
     */
    public InstrumentHandler getInstrumentHandler(InstrumentType instrumentType, String description, int parentId, String businesskey) {
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
                return new GiroHandler(instrumentDao, auditService, valueService, recurrentTransactionDao, description, parentId, businesskey);      
            case DEPOT: 
                return new DepotHandler(instrumentDao, auditService, description, parentId);  
            case REALESTATE: 
                return new RealEstateHandler(instrumentDao, auditService, this, description, parentId);        
            case CURRENCY: 
                return new CurrencyHandler(instrumentDao, auditService, this, description, businesskey);        
            case EQUITY: 
                return new EquityHandler(instrumentDao, auditService, this, description, businesskey);                                                    
            default:
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "can not create Instrumenthandler for instrumentType:"+instrumentType);
        }
    }

    /**
     * loads the instrument for the instrumentId and returns an InstrumentHandler for the type of the instrument.
     * use this if you do not know the type of the instrument and the InstrumentHandler Interface is suffitioned for your purpose (so you do not need type spezific functions)
     * @param instrumentId the id of the instrument
     * @return Instrumenthandler for the instrumenttype of the new instrument
     */
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

    /**
     * returns an TenantHandler. 
     * use this or the following InstrumentHandlerType-Spezific functions, if you know exactly what kind of instrumenthandler you want and if it matters. 
     * E.G. The TenantHandler has spezific public functions. You can only use them if you know that the instrumentId is a Tenant and you get the handler for this
     * @param instrumentId the instrument id
     * @param validate true if you want to validate that the type of the instrument and the expected type fits together (should be true in case you plan write operations with this instrument. Otherwise it is faster without validation)
     * @return TenantHandler
     */
    public TenantHandler getTenantHandler(int instrumentId, boolean validate) {
        var handler = new TenantHandler(instrumentDao, auditService, this, instrumentId); 
        if(validate) handler.validateInstrument();
        return handler;
    }

    public BudgetGroupHandler getBudgetGroupHandler(int instrumentId, boolean validate) {
        var handler = new BudgetGroupHandler(instrumentDao, auditService, this, instrumentId); 
        if(validate) handler.validateInstrument();
        return handler;
    }

    public DepotHandler getDepotHandler(int instrumentId, boolean validate) {
        var handler = new DepotHandler(instrumentDao, auditService, instrumentId); 
        if(validate) handler.validateInstrument();
        return handler;
    }

    public RealEstateHandler getRealEstateHandler(int instrumentId, boolean validate) {
        var handler = new RealEstateHandler(instrumentDao, auditService, this, instrumentId); 
        if(validate) handler.validateInstrument();
        return handler;
    }

    public List<Instrument> listInstruments() {
        return instrumentDao.listInstruments();
    }

    public List<Instrument> listTenants(){
        return listInstruments().stream().filter(i->i.getInstrumentType().equals(InstrumentType.TENANT)).collect(Collectors.toList());
    }
}