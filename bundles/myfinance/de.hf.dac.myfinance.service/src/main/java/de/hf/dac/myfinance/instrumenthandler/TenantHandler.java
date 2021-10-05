package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Tenant;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class TenantHandler extends AbsInstrumentHandler {
    protected Tenant tenant;
    private  final InstrumentFactory instrumentFactory;

    private static final String DEFAULT_ACCPF_PREFIX = "accountPf_";
    private static final String DEFAULT_BUDGETPF_PREFIX = "budgetPf_";
    private static final String DEFAULT_BUDGETGROUP_PREFIX = "budgetGroup_";
    private static final String DEFAULT_INCOMEBUDGET_PREFIX = "incomeBudget_";
    private static final String AUDIT_MSG_TYPE="TenantHandler_User_Event";
    

    public TenantHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, int tenantId) {
        super(instrumentDao, auditService);
        this.instrumentFactory = instrumentFactory;
        setInstrumentId(tenantId);
    }

    public TenantHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, Instrument tenant) {
        super(instrumentDao, auditService);
        this.instrumentFactory = instrumentFactory;
        if(!tenant.getInstrumentType().equals(InstrumentType.TENANT)) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create TenantHandler for instrumentType:"+tenant.getInstrumentType());
        }
        this.instrumentId = tenant.getInstrumentid();
        this.tenant = (Tenant)tenant;
    }

    public TenantHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, String description) {
        super(instrumentDao, auditService);
        this.instrumentFactory = instrumentFactory;
        tenant = new Tenant(description, true, ts);
    }

    @Override
    public void save() {
        instrumentDao.saveInstrument(tenant);
        auditService.saveMessage("Tenant inserted:" + tenant.getDescription(), Severity.INFO, AUDIT_MSG_TYPE);
        instrumentId = tenant.getInstrumentid();
        instrumentGraphHandler.addInstrumentToGraph(tenant.getInstrumentid(),instrumentId);

        var budgetPortfolioHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGETPORTFOLIO, DEFAULT_BUDGETPF_PREFIX+tenant.getDescription(), instrumentId);
        var budgetGroupHandler = instrumentFactory.getInstrumentHandler(InstrumentType.BUDGETGROUP, DEFAULT_BUDGETGROUP_PREFIX+tenant.getDescription(), budgetPortfolioHandler.getInstrumentId());




        int accPfId = newAccountPortfolio(DEFAULT_ACCPF_PREFIX+tenant.getDescription(), ts);
        instrumentGraphHandler.addInstrumentToGraph(accPfId, tenant.getInstrumentid());
    }

    public void load() {
          
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

    public List<Instrument> getAccounts() {
        Instrument accPF = getAccountPortfolio();
        if(accPF==null) {
            return new ArrayList<>();
        }
        return instrumentGraphHandler.getInstrumentFirstLevelChilds(accPF.getInstrumentid());
    }

}