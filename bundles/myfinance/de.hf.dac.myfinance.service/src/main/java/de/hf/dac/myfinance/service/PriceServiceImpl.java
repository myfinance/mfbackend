package de.hf.dac.myfinance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import de.hf.dac.api.io.audit.AuditService;
import de.hf.dac.api.io.domain.Severity;
import de.hf.dac.api.io.web.WebRequestService;
import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Equity;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.SecuritySymbols;
import de.hf.dac.myfinance.api.domain.Source;
import de.hf.dac.myfinance.api.domain.SourceName;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.service.PriceService;
import de.hf.dac.myfinance.importhandler.ImportHandler;

public class PriceServiceImpl implements PriceService{

    private EndOfDayPriceDao endOfDayPriceDao;
    private InstrumentDao instrumentDao;
    private WebRequestService webRequestService;
    private AuditService auditService;
    private static final String AUDIT_MSG_TYPE="PriceService_User_Event";

    @Inject
    public PriceServiceImpl(EndOfDayPriceDao endOfDayPriceDao, InstrumentDao instrumentDao, WebRequestService webRequestService, AuditService auditService){
        this.endOfDayPriceDao = endOfDayPriceDao;
        this.instrumentDao = instrumentDao;
        this.webRequestService = webRequestService;
        this.auditService = auditService;
    }

    @Override
    public List<EndOfDayPrice> listEodPrices(int instrumentId) {

        return endOfDayPriceDao.listEndOfDayPrices(instrumentId);
    }

    @Override
    public Set<SecuritySymbols> listSymbols(String isin) {
        Optional<Equity> equity = instrumentDao.getEquity(isin);
        if(!equity.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Equity with isin "+isin+" is not available");
        }
        return equity.get().getSymbols();
    }

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(String isin, LocalDate date){
        Optional<Equity> security = instrumentDao.getEquity(isin);;
        if(!security.isPresent()) {
            return Optional.empty();
        }
        return getEndOfDayPrice(security.get().getInstrumentid(), date);
    }

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentId, LocalDate date){
        return endOfDayPriceDao.getEndOfDayPrice(instrumentId, date);
    }

    @Override
    public Optional<Source> getSource(int sourceId){
        return instrumentDao.getSource(sourceId);
    }

    @Override
    public void importPrices(LocalDateTime ts){
        List<Source> sources = instrumentDao.getActiveSources();
        List<Instrument> secuirities = instrumentDao.getSecurities();
        Optional<Instrument> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_CURRENCY_EXCEPTION, "Currency EUR not available");
        }

        ImportHandler handler = new ImportHandler(sources, eur.get(), webRequestService);
        for(Instrument security : secuirities){
            //all prices are in EUR so we do not need prices for this currency
            if(security.getInstrumentType()==InstrumentType.CURRENCY && security.getBusinesskey().equals("EUR")) continue;
            LocalDate lastPricedDay = endOfDayPriceDao.getLastPricedDay(security.getInstrumentid());
            Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
            prices.putAll(handler.importSource(security, lastPricedDay, ts));
            for (EndOfDayPrice price : prices.values()) {
                endOfDayPriceDao.saveEndOfDayPrice(price);
            }
        }
    }

    @Override
    public void fillPriceHistory(int sourceId, String businesskey, LocalDateTime ts){
        Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_SOURCE_EXCEPTION, "Source with id "+sourceId+" is not available");
        }
        Optional<Instrument> security = instrumentDao.getSecurity(businesskey);
        if(!security.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Security with isin "+businesskey+" is not available");
        }
        List<Source> sources = new ArrayList<>();
        sources.add(source.get());

        //all prices are in EUR so we do not need prices for this currency
        if(security.get().getInstrumentType()==InstrumentType.CURRENCY && security.get().getBusinesskey().equals("EUR")) {
            throw new MFException(MFMsgKey.WRONG_REQUEST_EXCEPTION, "Prices for currency EUR are not necessary");
        }

        Optional<Instrument> eur = instrumentDao.getCurrency("EUR");
        if(!eur.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_CURRENCY_EXCEPTION, "Currency EUR not available");
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
                endOfDayPriceDao.saveEndOfDayPrice(price);
                pricedDates.add(price.getDayofprice());
            }

        }
    }

    @Override
    public void saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDateTime lastchanged) {
        saveEndOfDayPrice(currencyCode, isin, SourceName.MAN.getValue(), dayofprice, value, lastchanged);
    }

    protected void saveEndOfDayPrice(String currencyCode, String businesskey, int sourceId, LocalDate dayofprice, Double value, LocalDateTime lastchanged) {
        Optional<Instrument> currency = instrumentDao.getCurrency(currencyCode);
        if(!currency.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_CURRENCY_EXCEPTION, "Currency with code "+currencyCode+" is not available");
        }
        Optional<Instrument> security = instrumentDao.getSecurity(businesskey);
        if(!security.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENT_EXCEPTION, "Security with businesskey "+businesskey+" is not available");
        }
        Optional<Source> source = getSource(sourceId);
        if(!source.isPresent()){
            throw new MFException(MFMsgKey.UNKNOWN_SOURCE_EXCEPTION, "Source with id "+sourceId+" is not available");
        }
        EndOfDayPrice price = new EndOfDayPrice(currency.get(), security.get(), source.get(), dayofprice, value, lastchanged);
        auditService.saveMessage("Price inserted for instrument " + security.get().getInstrumentid() +" and date " + dayofprice + " : " + value + " " + currency.get().getBusinesskey()
            , Severity.INFO, AUDIT_MSG_TYPE);
        endOfDayPriceDao.saveEndOfDayPrice(price);
    }
}