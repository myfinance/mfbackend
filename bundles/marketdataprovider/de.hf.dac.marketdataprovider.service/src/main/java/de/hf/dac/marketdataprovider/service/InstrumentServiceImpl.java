/** ----------------------------------------------------------------------------
 *
 * ---                              ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : InstrumentServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service;

import de.hf.dac.marketdataprovider.api.domain.Currency;
import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.SecuritySymbols;
import de.hf.dac.marketdataprovider.api.domain.SecurityType;
import de.hf.dac.marketdataprovider.api.persistence.dao.InstrumentDao;
import de.hf.dac.marketdataprovider.api.service.InstrumentService;
import lombok.Data;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
public class InstrumentServiceImpl implements InstrumentService {

    private InstrumentDao instrumentDao;

    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao){
        this.instrumentDao = instrumentDao;
    }

    @Override
    public List<Instrument> listInstruments() {

        List<Instrument> instruments = instrumentDao.listInstruments();
        return instruments;
    }

    @Override
    public List<EndOfDayPrice> listPrices(String isin) {

        return null;//endOfDayPriceRepository.findByInstrumentIsin(isin);
    }

    @Override
    public Optional<Currency> getCurrency(String currencyCode){
        return instrumentDao.getCurrency(currencyCode);
    }

    @Override
    public Optional<Security> getSecurity(String isin){
        return instrumentDao.getSecurity(isin);
    }

    @Override
    public String saveSecurity(String theisin, String description) {
        String isin = theisin.toUpperCase();
        Optional<Security> existingSec = getSecurity(isin);
        if(!existingSec.isPresent()) {
            Security security = new Security(description, true, LocalDate.now(), SecurityType.EQUITY, isin);
            instrumentDao.saveSecurity(security);
            return "new security saved sucessfully";
        } else {
            existingSec.get().setDescription(description);
            return "security updated sucessfully";
        }
    }

    @Override
    public String saveSymbol(String theisin, String thesymbol, String thecurrencyCode){

        String isin = theisin.toUpperCase();
        String symbol = thesymbol.toUpperCase();
        String currencyCode = thecurrencyCode.toUpperCase();

        Optional<Currency> currency = getCurrency(currencyCode);
        if(!currency.isPresent()) {
            return "Symbol not saved: unknown currency:"+currencyCode;
        }
        Optional<Security> existingSec = getSecurity(isin);
        if(!existingSec.isPresent()){
            return "Symbol not saved: unknown security:"+isin;
        }
        Set<SecuritySymbols> symbols = existingSec.get().getSecuritySymbols();
        SecuritySymbols newSymbol = new SecuritySymbols(currency.get(), existingSec.get(), symbol);
        if(symbols!=null && !symbols.isEmpty()){
            Optional<SecuritySymbols> existingSymbol = symbols.stream().filter(i->i.getSymbol().equals(symbol)).findFirst();
            if(existingSymbol.isPresent()) {
                newSymbol = existingSymbol.get();
                newSymbol.setCurrency(currency.get());
            }
        }
        instrumentDao.saveSymbol(newSymbol);
        return "Symbol saved";
    }

    @Override
    public String saveCurrency(String currencyCode, String description) {
        String curCode = currencyCode.toUpperCase();
        Optional<Currency> existingCur = getCurrency(curCode);
        if(!existingCur.isPresent()) {
            Currency currency = new Currency(description, true, LocalDate.now(), curCode);
            instrumentDao.saveCurrency(currency);
            return "new currency saved sucessfully";
        } else {
            existingCur.get().setDescription(description);
            return "security updated sucessfully";
        }
    }

    @Override
    public EndOfDayPrice savePrice(EndOfDayPrice price) {
        return null;//endOfDayPriceRepository.save(price);
    }
}
