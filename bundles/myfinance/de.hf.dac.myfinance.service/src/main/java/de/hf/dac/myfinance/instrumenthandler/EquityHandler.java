package de.hf.dac.myfinance.instrumenthandler;

import java.util.List;
import java.util.Optional;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class EquityHandler extends AbsInstrumentHandler implements InstrumentHandler {
    List<String[]> symbols;
    String isin;

    public EquityHandler(InstrumentDao instrumentDao, AuditService auditService, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
    }

    public EquityHandler(InstrumentDao instrumentDao, AuditService auditService, String isin, String description) {
        super(instrumentDao, auditService);
        this.isin = isin.toUpperCase();;
        createDomainObject(description);
    }

    public void setSymbols(List<String[]> symbols) {
        this.symbols = symbols;
    }

   /* @Override
    public void save() {
        Optional<Equity> existingSec = getEquity(isin);
        if (!existingSec.isPresent()) {
            super.save();
        } else {
            existingSec.get().setDescription(description);
            auditService.saveMessage("Equity " + theisin + " updated with new description: " + description,
                    Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.updateInstrument(existingSec.get().getInstrumentid(), description, true);
        }
    }

    public void saveFullEquity(String theisin, String description, List<String[]> symbols) {
        saveEquity(theisin, description);
        deleteSymbols(theisin);
        for (String[] symbol : symbols) {
            saveSymbol(theisin, symbol[0], symbol[1]);
        }
    }*/

    @Override
    protected void createDomainObject(String description) {
        domainObject = new Equity(isin, description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "Equity";

    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.EQUITY;
    }
}