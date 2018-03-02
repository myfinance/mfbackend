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

import de.hf.dac.marketdataprovider.ValueHandler.ValueCurveService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Data
public class InstrumentServiceImpl implements InstrumentService {

    private InstrumentDao instrumentDao;
    private ValueCurveService service;

    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao){
        this.instrumentDao = instrumentDao;
        service = new ValueCurveService(instrumentDao);
    }

    @Override
    public List<Instrument> listInstruments() {

        List<Instrument> instruments = instrumentDao.listInstruments();
        return instruments;
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
    public List<EndOfDayPrice> listEodPrices(int instrumentId) {

        return instrumentDao.listEndOfDayPrices(instrumentId);
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
    public Optional<Source> getSource(int sourceId){
        return instrumentDao.getSource(sourceId);
    }

    @Override
    public Map<LocalDate, Double> getValueCurve(int instrumentId){
        return service.getValueCurve(instrumentId);
    }

    @Override
    public Map<LocalDate, Double> getValueCurve(int instrumentId, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Double> adjValueCurve = new HashMap();
        if(startDate.isAfter(endDate) || startDate.getYear()<1970) return adjValueCurve;
        Map<LocalDate, Double> valueCurve = getValueCurve(instrumentId);
        Set<LocalDate> dates = valueCurve.keySet();
        for (LocalDate date:valueCurve.keySet()) {
            if(!date.isBefore(startDate) && !date.isAfter(endDate)){
                adjValueCurve.put(date, valueCurve.get(date));
            }
        }
        return valueCurve;
    }

    @Override
    public double getValue(int instrumentId, LocalDate date){
        return service.getValue(instrumentId, date);
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
    public String saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDateTime lastchanged) {
        return saveEndOfDayPrice(currencyCode, isin, SourceName.MAN.getValue(), dayofprice, value, lastchanged);
    }

    protected String saveEndOfDayPrice(String currencyCode, String isin, int sourceId, LocalDate dayofprice, Double value, LocalDateTime lastchanged) {
        Optional<Currency> currency = getCurrency(currencyCode);
        if(!currency.isPresent()){
            return "Currency with code "+currencyCode+" is not available";
        }
        Optional<Security> security = getSecurity(isin);
        if(!security.isPresent()){
            return "Security with isin "+isin+" is not available";
        }
        Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            return "Source with id "+sourceId+" is not available";
        }
        EndOfDayPrice price = new EndOfDayPrice(currency.get(), security.get(), source.get(), dayofprice, value, lastchanged);
        instrumentDao.saveEndOfDayPrice(price);
        return("Saved");
    }

    @Override
    public String importPrices(LocalDateTime ts){
        List<Source> sources = instrumentDao.getActiveSources();
        List<Security> secuirities = getSecurities();
        Optional<Currency> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            return "Currency EUR not available";
        }

        ImportHandler handler = new ImportHandler(sources, true,"proxy.dzbank.vrnet",8080,"xn01598","XN01598", eur.get());
        for(Security security : secuirities){
            //all prices are in EUR so we do not need prices for this currency
            if(security.getSecurityType()==SecurityType.CURRENCY && ((Currency)security).getCurrencycode().equals("EUR")) continue;
            LocalDate lastPricedDay = instrumentDao.getLastPricedDay(security.getInstrumentid());
            Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
            prices.putAll(handler.importSource(security, lastPricedDay, ts));
            for (EndOfDayPrice price : prices.values()) {
                instrumentDao.saveEndOfDayPrice(price);
            }
        }

        return "sucessful";
    }

    @Override
    public String fillPriceHistory(int sourceId, String isin, LocalDateTime ts){
        Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            return "Source with id "+sourceId+" is not available";
        }
        Optional<Security> security = getSecurity(isin);
        if(!security.isPresent()){
            return "Security with isin "+isin+" is not available";
        }
        List<Source> sources = new ArrayList<>();
        sources.add(source.get());

        //all prices are in EUR so we do not need prices for this currency
        if(security.get().getSecurityType()==SecurityType.CURRENCY && ((Currency)security.get()).getCurrencycode().equals("EUR")) {
            return "Prices for currency EUR are not necessary";
        }

        Optional<Currency> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            return "Currency EUR not available";
        }

        ImportHandler handler = new ImportHandler(sources, true,"proxy.dzbank.vrnet",8080,"xn01598","XN01598", eur.get());

        Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
        prices.putAll(handler.importSource(security.get(), LocalDate.MIN, ts));

        List<EndOfDayPrice> existingPrices = listEodPrices(security.get().getInstrumentid());
        List<LocalDate> pricedDates = new ArrayList<>();
        if (existingPrices != null) {
            for (EndOfDayPrice price:existingPrices) {
                pricedDates.add(price.getDayofprice());
            }
        }

        for (EndOfDayPrice price : prices.values()) {
            if(!pricedDates.contains(price.getDayofprice())){
                instrumentDao.saveEndOfDayPrice(price);
                pricedDates.add(price.getDayofprice());
            }

        }

        return "sucessful";
    }
}
