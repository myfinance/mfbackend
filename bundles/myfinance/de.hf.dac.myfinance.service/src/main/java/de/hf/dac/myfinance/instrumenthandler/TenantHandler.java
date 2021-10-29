package de.hf.dac.myfinance.instrumenthandler;

import java.util.ArrayList;
import java.util.List;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Tenant;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class TenantHandler extends AbsInstrumentHandler {
    private  InstrumentFactory instrumentFactory;

    private static final String DEFAULT_ACCPF_PREFIX = "accountPf_";
    private static final String DEFAULT_BUDGETPF_PREFIX = "budgetPf_";
    private static final String DEFAULT_BUDGETGROUP_PREFIX = "budgetGroup_";
    

    public TenantHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, int tenantId) {
        super(instrumentDao, auditService, tenantId);
        this.instrumentFactory = instrumentFactory;
    }

    public TenantHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, Instrument tenant) {
        super(instrumentDao, auditService, tenant);
        this.instrumentFactory = instrumentFactory;
    }

    public TenantHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, String description) {
        super(instrumentDao, auditService, description, -1);
        this.instrumentFactory = instrumentFactory;
    }

    protected void updateParent() {
        setParent(instrumentId);
    } 

    @Override
    public void save() {
        super.save();

        var budgetPortfolioHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGETPORTFOLIO, DEFAULT_BUDGETPF_PREFIX+domainObject.getDescription(), instrumentId);
        budgetPortfolioHandler.setTreeLastChanged(ts);
        budgetPortfolioHandler.save();
        var budgetGroupHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGETGROUP, DEFAULT_BUDGETGROUP_PREFIX+domainObject.getDescription(), budgetPortfolioHandler.getInstrumentId());
        budgetGroupHandler.setTreeLastChanged(ts);
        budgetGroupHandler.save();

        var accPortfolioHandler = instrumentFactory.getInstrumentHandler(InstrumentType.ACCOUNTPORTFOLIO, DEFAULT_ACCPF_PREFIX+domainObject.getDescription(), instrumentId);
        accPortfolioHandler.setTreeLastChanged(ts);
        accPortfolioHandler.save();
    }

    public List<Instrument> listInstruments() { 
        return instrumentGraphHandler.getAllInstrumentChilds(instrumentId);
    }

    public List<Instrument> listInstruments(boolean onlyActive) {
        return instrumentGraphHandler.getAllInstrumentChilds(instrumentId, onlyActive);
    }

    public List<Instrument> listInstruments(InstrumentType instrumentType, boolean onlyActive) {
        return instrumentGraphHandler.getAllInstrumentChilds(instrumentId, instrumentType, onlyActive);
    }

    public Instrument getAccountPortfolio() {
        return instrumentGraphHandler.getFirstLevelChildsPerTypeFirstmatch(instrumentId, InstrumentType.ACCOUNTPORTFOLIO);
    }

    public Instrument getBudgetPortfolio() {
        return instrumentGraphHandler.getFirstLevelChildsPerTypeFirstmatch(instrumentId, InstrumentType.BUDGETPORTFOLIO);
    }

    public List<Instrument> getAccounts() {
        Instrument accPF = getAccountPortfolio();
        if(accPF==null) {
            return new ArrayList<>();
        }
        return instrumentGraphHandler.getInstrumentFirstLevelChilds(accPF.getInstrumentid());
    }

    @Override
    protected void createDomainObject(String description) {
        domainObject = new Tenant(description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "Tenant";
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.TENANT;
    }

    @Override
    public void updateInstrument(String description, boolean isActive) {
        super.updateInstrument(description, isActive);
        List<Instrument> instruments = instrumentGraphHandler.getAllInstrumentChilds(instrumentId);
        renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_BUDGETPF_PREFIX, instruments);
        renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_BUDGETGROUP_PREFIX, instruments);
        renameDefaultTenantChild(instrumentId, description, oldDesc, DEFAULT_ACCPF_PREFIX, instruments); 
    }

    private void renameDefaultTenantChild(int instrumentId, String newDesc, String oldDesc, String defaultDescPrefix, List<Instrument> instruments) {
        //look by description for default instruments of the tenant to rename
        instruments.stream().filter(i->i.getDescription().equals(defaultDescPrefix+oldDesc)).forEach(i->{
            var handler = instrumentFactory.getInstrumentHandler(i.getInstrumentid());
            handler.updateInstrument(newDesc, true);
        });
    }
}