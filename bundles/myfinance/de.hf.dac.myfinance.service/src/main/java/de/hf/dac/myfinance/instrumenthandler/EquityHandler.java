package de.hf.dac.myfinance.instrumenthandler;

import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;

public class EquityHandler extends AbsInstrumentHandler {

    public EquityHandler(InstrumentDao instrumentDao, int instrumentId) {
        super(instrumentDao, instrumentId);
    }

    protected EquityHandler(InstrumentDao instrumentDao) {
        super(instrumentDao);
    }

    public void saveEquity(String theisin, String description) {
        String isin = theisin.toUpperCase();
        Optional<Equity> existingSec = getEquity(isin);
        if(!existingSec.isPresent()) {
            Equity equity = new Equity(isin, description, true, LocalDateTime.now());
            auditService.saveMessage("Equity inserted:" + theisin, Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.saveInstrument(equity);
        } else {
            existingSec.get().setDescription(description);
            auditService.saveMessage("Equity " + theisin +" updated with new description: " + description, Severity.INFO, AUDIT_MSG_TYPE);
            instrumentDao.updateInstrument(existingSec.get().getInstrumentid(), description, true);
        }
    }

    public void saveFullEquity(String theisin, String description, List<String[]> symbols) {
        saveEquity(theisin, description);
        deleteSymbols(theisin);
        for (String[] symbol : symbols) {
            saveSymbol(theisin, symbol[0], symbol[1]);
        }
    }
}