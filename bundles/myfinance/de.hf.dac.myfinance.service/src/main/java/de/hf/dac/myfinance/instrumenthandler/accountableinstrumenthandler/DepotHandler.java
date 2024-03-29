package de.hf.dac.myfinance.instrumenthandler.accountableinstrumenthandler;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Depot;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentPropertyType;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class DepotHandler extends AbsAccountableInstrumentHandler {
    private int defaultGiroId;
    private boolean isDepotInitialized = false;
    private int valueBudgetId;

    public DepotHandler(InstrumentDao instrumentDao, AuditService auditService, Instrument depot) {
        super(instrumentDao, auditService, depot);
    }

    public DepotHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
    }

    public DepotHandler(InstrumentDao instrumentDao, AuditService auditService, String description, int tenantId, String businesskey) {
        super(instrumentDao, auditService, description, tenantId, true, businesskey);
    }

    @Override
    protected void createDomainObject() {
        domainObject = new Depot(description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "Depot";
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.DEPOT;
    }

    public void initAdditionalFields(int defaultGiroId, int valueBudgetId) {
        setDefaultGiroId(defaultGiroId);
        setValueBudgetId(valueBudgetId);
        isDepotInitialized = true;
    }

    public void setDefaultGiroId(int defaultGiroId){
        var giro = getInstrument(defaultGiroId, "Depot not saved: Unknown default giro account:");
        validateInstrument(giro, InstrumentType.GIRO, "Depot not saved: default giro account not valid:");
        this.defaultGiroId = giro.getInstrumentid();
    }

    private void setValueBudgetId(int valueBudgetId){
        var valueBudget = getInstrument(valueBudgetId, "Depot not saved: Unknown ValueBudget:");
        validateInstrument(valueBudget, InstrumentType.BUDGET, "Depot not saved: ValueBudget not valid:");
        this.valueBudgetId = valueBudgetId;
    }

    @Override
    protected void saveNewInstrument() {
        if(!isDepotInitialized) {
            throw new MFException(MFMsgKey.OBJECT_NOT_INITIALIZED_EXCEPTION, "AdditionalFields for depot are not set:");
        }
        super.saveNewInstrument();
        saveProperty(InstrumentPropertyType.DEFAULTGIROID, defaultGiroId);
        instrumentGraphHandler.addInstrumentToGraph(instrumentId, valueBudgetId, EdgeType.VALUEBUDGET);
    }

    @Override
    protected void updateInstrument() {
        super.updateInstrument();
        deleteInstrumentPropertyList();
        saveProperty(InstrumentPropertyType.DEFAULTGIROID, defaultGiroId);
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