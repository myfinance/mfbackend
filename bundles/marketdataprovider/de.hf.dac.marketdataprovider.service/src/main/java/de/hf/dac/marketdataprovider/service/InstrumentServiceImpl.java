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
import de.hf.dac.marketdataprovider.api.domain.Source;
import de.hf.dac.marketdataprovider.api.domain.SourceName;
import de.hf.dac.marketdataprovider.api.persistence.dao.InstrumentDao;
import de.hf.dac.marketdataprovider.api.service.InstrumentService;
import de.hf.dac.marketdataprovider.importhandler.ImportHandler;
import lombok.Data;

import javax.inject.Inject;
import java.time.LocalDate;
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
    public List<Security> getSecurities(){
        return instrumentDao.getSecurities();
    }


    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(String isin, LocalDate date){
        Optional<Security> security = getSecurity(isin);
        if(!security.isPresent()) {
            return Optional.empty();
        }
        return getEndOfDayPrice(getSecurity(isin).get().getInstrumentid(), date);
    }

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentId, LocalDate date){
        return instrumentDao.getEndOfDayPrice(instrumentId, date);
    }

    @Override
    public Optional<Source> getSource(String description){
        return instrumentDao.getSource(description);
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
    public String saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDate lastchanged) {
        return saveEndOfDayPrice(currencyCode, isin, SourceName.MAN.name(), dayofprice, value, lastchanged);
    }

    protected String saveEndOfDayPrice(String currencyCode, String isin, String sourceDescription, LocalDate dayofprice, Double value, LocalDate lastchanged) {
        Optional<Currency> currency = getCurrency(currencyCode);
        if(!currency.isPresent()){
            return "Currency with code "+currencyCode+" is not available";
        }
        Optional<Security> security = getSecurity(isin);
        if(!security.isPresent()){
            return "Security with isin "+isin+" is not available";
        }
        Optional<Source> source = getSource(sourceDescription);
        if(!source.isPresent()){
            return "Source with description "+sourceDescription+" is not available";
        }
        EndOfDayPrice price = new EndOfDayPrice(currency.get(), security.get(), source.get(), dayofprice, value, lastchanged);
        instrumentDao.saveEndOfDayPrice(price);
        return("Saved");
    }

    @Override
    public void importPrices(){
        List<Source> sources = instrumentDao.getActiveSources();
        List<Security> secuirities = getSecurities();
        ImportHandler handler = new ImportHandler(true,"proxy.dzbank.vrnet",8080,"xn01598","XN01598");
        for(Security security : secuirities){
            //all prices are in EUR so we do not need prices for this currency
            if(security.getDescription().equals("EUR")) continue;
            LocalDate lastPricedDay = instrumentDao.getLastPricedDay(security.getInstrumentid());
            for(Source source : sources){
                handler.importSource(source, lastPricedDay, security);
            }

        }



    }
}
