package de.hf.dac.myfinance.instrumenthandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.Tenant;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class TenantHandler extends AbsInstrumentHandler {
    protected Tenant tenant;

    public TenantHandler(InstrumentDao instrumentDao, int tenantId) {
        super(instrumentDao, tenantId);
    }

    public TenantHandler(InstrumentDao instrumentDao, Instrument tenant) {
        super(instrumentDao, tenant.getInstrumentid());
        if(!tenant.getInstrumentType().equals(InstrumentType.TENANT)) {
            throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, "can not create TenantHandler for instrumentType:"+tenant.getInstrumentType());
        }
        this.tenant = (Tenant)tenant;
    }

    public TenantHandler(String description, LocalDateTime ts) {
        Tenant tenant = new Tenant(description, true, ts);
        instrumentDao.saveInstrument(tenant);
        auditService.saveMessage("Tenant inserted:" + description, Severity.INFO, AUDIT_MSG_TYPE);
        instrumentGraphHandler.addInstrumentToGraph(tenant.getInstrumentid(),tenant.getInstrumentid());

        int budgetPfId = newBudgetPortfolio(DEFAULT_BUDGETPF_PREFIX+description, ts);
        instrumentGraphHandler.addInstrumentToGraph(budgetPfId, tenant.getInstrumentid());
        newBudgetGroup(description, budgetPfId, ts);
        int accPfId = newAccountPortfolio(DEFAULT_ACCPF_PREFIX+description, ts);
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