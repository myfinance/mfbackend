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

package de.hf.dac.myfinance.service;

import de.hf.dac.api.io.web.WebRequestService;
import de.hf.dac.myfinance.ValueHandler.ValueCurveService;
import de.hf.dac.myfinance.api.domain.*;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.importhandler.ImportHandler;
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
    private WebRequestService webRequestService;

    @Inject
    public InstrumentServiceImpl(InstrumentDao instrumentDao, WebRequestService webRequestService){
        this.instrumentDao = instrumentDao;
        this.webRequestService = webRequestService;
        service = new ValueCurveService(instrumentDao);
    }

    @Override
    public List<Instrument> listInstruments() {

        List<Instrument> instruments = instrumentDao.listInstruments();
        return instruments;
    }



    @Override
    public Optional<Instrument> getCurrency(String currencyCode){
        return instrumentDao.getCurrency(currencyCode);
    }

    @Override
    public Optional<Instrument> getSecurity(String isin){
        return instrumentDao.getSecurity(isin);
    }

    @Override
    public List<Instrument> getSecurities(){
        return instrumentDao.getSecurities();
    }

    @Override
    public List<EndOfDayPrice> listEodPrices(int instrumentId) {

        return instrumentDao.listEndOfDayPrices(instrumentId);
    }

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(String isin, LocalDate date){
        Optional<Instrument> security = getSecurity(isin);
        if(!security.isPresent()) {
            return Optional.empty();
        }
        return getEndOfDayPrice(security.get().getInstrumentid(), date);
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
        Optional<Instrument> existingSec = getSecurity(isin);
        if(!existingSec.isPresent()) {
            /*Security security = new Security(description, true, LocalDateTime.now(), SecurityType.EQUITY, isin);
            instrumentDao.saveSecurity(security);*/
            return "new security saved sucessfully";
        } else {
            existingSec.get().setDescription(description);
            return "security updated sucessfully";
        }
    }

    @Override
    public String saveSymbol(String theisin, String thesymbol, String thecurrencyCode){

        /*String isin = theisin.toUpperCase();
        String symbol = thesymbol.toUpperCase();
        String currencyCode = thecurrencyCode.toUpperCase();

        Optional<Instrument> currency = getCurrency(currencyCode);
        if(!currency.isPresent()) {
            return "Symbol not saved: unknown currency:"+currencyCode;
        }
        Optional<Instrument> existingSec = getSecurity(isin);
        if(!existingSec.isPresent()){
            return "Symbol not saved: unknown security:"+isin;
        }
        Set<SecuritySymbols> symbols = existingSec.get().getSecuritySymbols();
        SecuritySymbols newSymbol = new SecuritySymbols(currency.get(), existingSec.get().getInstrumentid(), symbol);
        if(symbols!=null && !symbols.isEmpty()){
            Optional<SecuritySymbols> existingSymbol = symbols.stream().filter(i->i.getSymbol().equals(symbol)).findFirst();
            if(existingSymbol.isPresent()) {
                newSymbol = existingSymbol.get();
                newSymbol.setCurrency(currency.get());
            }
        }
        instrumentDao.saveSymbol(newSymbol);*/
        return "Symbol saved";
    }

    @Override
    public String saveCurrency(String currencyCode, String description) {
        String curCode = currencyCode.toUpperCase();
        Optional<Instrument> existingCur = getCurrency(curCode);
        if(!existingCur.isPresent()) {
            Instrument currency = new Currency(currencyCode, description, true, LocalDateTime.now());
            instrumentDao.saveInstrument(currency);
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
        /*Optional<Instrument> currency = getCurrency(currencyCode);
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
        instrumentDao.saveEndOfDayPrice(price);*/
        return("Saved");
    }

    @Override
    public String importPrices(LocalDateTime ts){
        /*List<Source> sources = instrumentDao.getActiveSources();
        List<Instrument> secuirities = getSecurities();
        Optional<Instrument> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            return "Currency EUR not available";
        }

        ImportHandler handler = new ImportHandler(sources, eur.get(), webRequestService);
        for(Instrument security : secuirities){
            //all prices are in EUR so we do not need prices for this currency
            if(security.getSecurityType()==SecurityType.CURRENCY && ((Instrument)security).getCurrencycode().equals("EUR")) continue;
            LocalDate lastPricedDay = instrumentDao.getLastPricedDay(security.getInstrumentid());
            Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
            prices.putAll(handler.importSource(security, lastPricedDay, ts));
            for (EndOfDayPrice price : prices.values()) {
                instrumentDao.saveEndOfDayPrice(price);
            }
        }
*/
        return "sucessful";
    }

    @Override
    public String fillPriceHistory(int sourceId, String isin, LocalDateTime ts){
       /* Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            return "Source with id "+sourceId+" is not available";
        }
        Optional<Instrument> security = getSecurity(isin);
        if(!security.isPresent()){
            return "Security with isin "+isin+" is not available";
        }
        List<Source> sources = new ArrayList<>();
        sources.add(source.get());

        //all prices are in EUR so we do not need prices for this currency
        if(security.get().getSecurityType()==SecurityType.CURRENCY && ((Instrument)security.get()).getCurrencycode().equals("EUR")) {
            return "Prices for currency EUR are not necessary";
        }

        Optional<Instrument> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            return "Currency EUR not available";
        }

        ImportHandler handler = new ImportHandler(sources, eur.get(), webRequestService);

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
*/
        return "sucessful";
    }
}
