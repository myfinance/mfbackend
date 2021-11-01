package de.hf.dac.myfinance.instrumenthandler;

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

public class RealEstateHandler extends AbsAccountableInstrumentHandler implements AccountableInstrumentHandler {
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

    public RealEstateHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, String description, int tenantId) {
        super(instrumentDao, auditService, description, tenantId, true);
        this.instrumentFactory = instrumentFactory;
    }

    @Override
    protected void createDomainObject(String description) {
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

    public void initAdditionalFields(int valueBudgetId, List<ValuePerDate> yieldgoals, List<ValuePerDate> realEstateProfits) {
        setValueBudgetId(valueBudgetId);
        this.yieldgoals = yieldgoals;
        this.realEstateProfits = realEstateProfits;
        isRealEstateInitialized = true;
    }

    private void setValueBudgetId(int valueBudgetId){
        var valueBudget = getInstrument(valueBudgetId, "Realestate not saved: Unknown ValueBudget:");
        validateInstrument(valueBudget, InstrumentType.BUDGET, "Realestate not saved: ValueBudget not valid:");
        this.valueBudgetId = valueBudgetId;
    }

    @Override
    public void save() {
        if(!isRealEstateInitialized) {
            throw new MFException(MFMsgKey.OBJECT_NOT_INITIALIZED_EXCEPTION, "AdditionalFields for RealEstate are not set:");
        }
        super.save();
        savePropertyList(InstrumentPropertyType.YIELDGOAL, yieldgoals);
        savePropertyList(InstrumentPropertyType.REALESTATEPROFITS, realEstateProfits);

        var budgetPortfolio = instrumentFactory.getTenantHandler(getTenant().get(), false).getBudgetPortfolio();

        var budgetGroupHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGETGROUP, DEFAULT_BUDGETGROUP_PREFIX+domainObject.getDescription(), budgetPortfolio.getInstrumentid());
        budgetGroupHandler.setTreeLastChanged(ts);
        budgetGroupHandler.save();

        saveProperty(InstrumentPropertyType.REALESTATEBUDGETGROUPID, budgetGroupHandler.getInstrumentId());
        instrumentGraphHandler.addInstrumentToGraph(instrumentId, valueBudgetId, EdgeType.VALUEBUDGET);
    }

    @Override
    public void updateInstrument(String description, boolean isActive) {
        super.updateInstrument(description, isActive);
        deleteInstrumentPropertyList();
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
        budgetGroupHandler.updateInstrument(false);
    }

}