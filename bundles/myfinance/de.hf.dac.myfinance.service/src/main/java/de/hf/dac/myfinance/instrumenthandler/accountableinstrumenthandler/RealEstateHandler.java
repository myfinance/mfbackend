package de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler;

import java.util.List;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.RealEstate;
import de.hf.dac.myfinance.api.domain.ValuePerDate;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;

public class RealEstateHandler extends AbsAccountableInstrumentHandler{
    int valueBudgetId;
    List<ValuePerDate> yieldgoals;
    List<ValuePerDate> realEstateProfits;
    Boolean isRealEstateInitialized = false;
    InstrumentFactory instrumentFactory;
    private static final String DEFAULT_BUDGETGROUP_PREFIX = "RealEstateBG_";


    public RealEstateHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, Instrument realEstate) {
        super(instrumentDao, auditService, realEstate);
        this.instrumentFactory = instrumentFactory;
    }

    public RealEstateHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
        this.instrumentFactory = instrumentFactory;
    }

    public RealEstateHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, String description, int tenantId, String businesskey) {
        super(instrumentDao, auditService, description, tenantId, true, businesskey);
        this.instrumentFactory = instrumentFactory;
    }

    @Override
    protected void createDomainObject() {
        domainObject = new RealEstate(description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "Realestate";
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.REALESTATE;
    }

    public void initAdditionalFields(List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits) {
        this.yieldgoals = yieldgoals;
        this.realEstateProfits = realEstateProfits;
        isRealEstateInitialized = true;
    }

    public void initAdditionalFields(int valueBudgetId, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits) {
        setValueBudgetId(valueBudgetId);
        initAdditionalFields(yieldgoals, realEstateProfits);
    }

    private void setValueBudgetId(int valueBudgetId){
        var valueBudget = getInstrument(valueBudgetId, "Realestate not saved: Unknown ValueBudget:");
        validateInstrument(valueBudget, InstrumentType.BUDGET, "Realestate not saved: ValueBudget not valid:");
        this.valueBudgetId = valueBudgetId;
    }

    @Override
    protected void saveNewInstrument() {
        if(!isRealEstateInitialized) {
            throw new MFException(MFMsgKey.OBJECT_NOT_INITIALIZED_EXCEPTION, "AdditionalFields for RealEstate are not set:");
        }
        super.saveNewInstrument();
        savePropertyList(InstrumentPropertyType.YIELDGOAL, yieldgoals);
        savePropertyList(InstrumentPropertyType.REALESTATEPROFITS, realEstateProfits);

        var budgetPortfolio = instrumentFactory.getTenantHandler(getTenant().get(), false).getBudgetPortfolio();

        var budgetGroupHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGETGROUP, DEFAULT_BUDGETGROUP_PREFIX+domainObject.getDescription(), budgetPortfolio.getInstrumentid(), DEFAULT_BUDGETGROUP_PREFIX+domainObject.getDescription());
        budgetGroupHandler.setTreeLastChanged(ts);
        budgetGroupHandler.save();

        saveProperty(InstrumentPropertyType.REALESTATEBUDGETGROUPID, budgetGroupHandler.getInstrumentId());
        instrumentGraphHandler.addInstrumentToGraph(instrumentId, valueBudgetId, EdgeType.VALUEBUDGET);
    }

    @Override
    protected void updateInstrument() {
        super.updateInstrument();
        deleteInstrumentPropertyList(InstrumentPropertyType.YIELDGOAL);
        deleteInstrumentPropertyList(InstrumentPropertyType.REALESTATEPROFITS);
        savePropertyList(InstrumentPropertyType.YIELDGOAL, yieldgoals);
        savePropertyList(InstrumentPropertyType.REALESTATEPROFITS, realEstateProfits);
    }

    public int getBudgetGroupId() {
        var properties = getInstrumentProperties(InstrumentPropertyType.REALESTATEBUDGETGROUPID);
        if(properties == null || properties.size()==0) {
            throw new MFException(MFMsgKey.NO_VALID_INSTRUMENT, "Realestate has no REALESTATEBUDGETGROUPID-property:"+instrumentId);
        }
        return Integer.parseInt(properties.get(0).getValue());
    }

    @Override
    protected void validateInstrument4Inactivation() {
        var budgetGroupHandler = instrumentFactory.getInstrumentHandler(getBudgetGroupId());
        budgetGroupHandler.setActive(false);
        budgetGroupHandler.save();
    }

    @Override
    protected InstrumentType getParentType() {
        return InstrumentType.ACCOUNTPORTFOLIO;
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
        super.setBusinesskey(description);
    }
}