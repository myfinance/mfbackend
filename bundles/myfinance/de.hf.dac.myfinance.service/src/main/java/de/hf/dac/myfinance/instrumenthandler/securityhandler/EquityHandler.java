package de.hf.dac.myfinance.instrumenthandler.securityhandler;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.SecuritySymbols;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.instrumenthandler.InstrumentFactory;

public class EquityHandler extends SecurityHandler {
    List<String[]> symbols;
    private InstrumentFactory instrumentFactory;

    public EquityHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, int instrumentId) {
        super(instrumentDao, auditService, instrumentId);
        this.instrumentFactory = instrumentFactory;
    }

    public EquityHandler(InstrumentDao instrumentDao, AuditService auditService, InstrumentFactory instrumentFactory, String isin, String description) {
        super(instrumentDao, auditService, isin, description);
        this.instrumentFactory = instrumentFactory;
    }

    public void setSymbols(List<String[]> symbols) {
        this.symbols = symbols;
    }

    @Override
    protected Optional<Instrument> getSecurity(){
        return instrumentDao.getEquity(businesskey);
    }


    public void saveFullEquity(List<String[]> symbols) {
        save();
        deleteSymbols();
        for (String[] symbol : symbols) {
            saveSymbol(symbol[0], symbol[1]);
        }
    }

    @Override
    protected void createDomainObject() {
        domainObject = new Equity(businesskey, description, true, ts);
    }

    @Override
    protected void setDomainObjectName() {
        domainObjectName = "Equity";
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.EQUITY;
    }

    public void deleteSymbols(){
        if(!exists) return;
        Set<SecuritySymbols> symbols = ((Equity)domainObject).getSymbols();
        for (SecuritySymbols securitySymbol : symbols) {
            auditService.saveMessage(instrumentDao.deleteSymbols(securitySymbol.getSymbolid()), Severity.INFO, AUDIT_MSG_TYPE);
        }
    }

    public void saveSymbol(String thesymbol, String thecurrencyCode){
        String symbol = thesymbol.toUpperCase();
        String currencyCode = thecurrencyCode.toUpperCase().trim();
        var currencyHandler = instrumentFactory.getInstrumentHandler(InstrumentType.CURRENCY, "", -1, currencyCode);
        Optional<Instrument> currency = currencyHandler.getSavedDomainObject();
        if(!currency.isPresent()) {
            throw new MFException(MFMsgKey.UNKNOWN_CURRENCY_EXCEPTION, "Symbol not saved: unknown currency:"+currencyCode);
        }
        loadInstrument();
        if(!exists){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Symbol not saved: unknown security:"+businesskey);
        }
        Set<SecuritySymbols> symbols = ((Equity)domainObject).getSymbols();
        SecuritySymbols newSymbol = new SecuritySymbols(currency.get(), domainObject.getInstrumentid(), symbol);
        if(symbols!=null && !symbols.isEmpty()){
            Optional<SecuritySymbols> existingSymbol = symbols.stream().filter(i->i.getSymbol().equals(symbol)).findFirst();
            if(existingSymbol.isPresent()) {
                newSymbol = existingSymbol.get();
                newSymbol.setCurrency(currency.get());
                auditService.saveMessage("Symbol " + thesymbol +" updated with new currency: " + thecurrencyCode, Severity.INFO, AUDIT_MSG_TYPE);
            }
            else {
                auditService.saveMessage("Symbol with currency "+thecurrencyCode+" inserted:" + thesymbol, Severity.INFO, AUDIT_MSG_TYPE);
            }
        }
        instrumentDao.saveSymbol(newSymbol);
    }
}