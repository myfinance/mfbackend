/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AlphavantageHandler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 08.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.importhandler;


import de.hf.dac.api.io.web.WebRequestService;
import de.hf.dac.myfinance.api.domain.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AlphavantageHandler extends AbsHandler implements Handler {
    AlphavantageType type;
    Source source;
    Instrument eur;

    public AlphavantageHandler(AlphavantageType type,
            Source source,
        WebRequestService downloadHandler, Instrument eur){

        this.type=type;
        this.source = source;
        this.downloadHandler=downloadHandler;
        this.eur = eur;
    }

    public Map<LocalDate, EndOfDayPrice> importPrices(Instrument security, LocalDate lastPricedDate, LocalDateTime ts){
        Map<LocalDate, EndOfDayPrice> values = new HashMap<>();
        InstrumentType securityType = security.getInstrumentType();
        if(type == AlphavantageType.EQ || type == AlphavantageType.EQFULL){
            values.putAll(getEqPricesForSymbols(security, lastPricedDate, ts, securityType));

        } else {
            if(!securityType.equals(InstrumentType.Currency)) return values;
            try{
                values.putAll(convertToEndOfDayPrice(getFxPrices((Instrument) security), lastPricedDate,
                    eur, source,
                    security,
                    ts));
            }catch(Exception e){
                log.error("can not load prices for currency " +security.getBusinesskey() + ":"+e);
            }
        }

        return values;
    }

    private Map<LocalDate, EndOfDayPrice> getEqPricesForSymbols(Instrument security, LocalDate lastPricedDate, LocalDateTime ts, InstrumentType securityType) {
        Map<LocalDate, EndOfDayPrice> values = new HashMap<>();
        if(!securityType.equals(InstrumentType.Equity))
            return values;

        Set<SecuritySymbols> symbols = ((Equity)security).getSymbols();
        if(symbols == null || symbols.size()==0){
            return values;
        }
        for (SecuritySymbols symbol:symbols) {
            String symbolString = symbol.getSymbol();
            try{
                values.putAll(convertToEndOfDayPrice(getEQPrices(symbolString), lastPricedDate,
                    symbol.getCurrency(), source,
                    security,
                    ts));
            } catch(Exception e){
                log.error("can not load prices for Security " +security.getBusinesskey() + " and Symbol " + symbol.getSymbol() + ":"+e);
            }

        }
        return values;
    }

    private Map<LocalDate, Double> getFxPrices(Instrument security) {
        String url;
        Map<LocalDate, Double> prices = new HashMap<>();
        String currencyCode = security.getBusinesskey();
        url = source.getUrlprefix()+currencyCode+source.getUrlpostfix();
        Map<String, Object> map = getJsonMapFromUrl(url);
        Map<String, String> info = (Map<String, String>)map.get("Realtime Currency Exchange Rate");
        try{
            LocalDate date = LocalDateTime.parse(info.get("6. Last Refreshed"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
            Double value = Double.parseDouble(info.get("5. Exchange Rate"));
            prices.put(date, value);
        }catch(Exception e){
            log.error("can not pars value for Currency " + currencyCode + ":"+e);
        }
        return prices;
    }

    private Map<LocalDate, Double> getEQPrices(String symbol){
        String url = source.getUrlprefix()+symbol+source.getUrlpostfix();
        Map<LocalDate, Double> values = new HashMap<>();
        Map<String, Object> map = getJsonMapFromUrl(url);
        Map<String, Object> timeSeries = (Map<String, Object>)map.get("Time Series (Daily)");


        for (String dateString: timeSeries.keySet() ) {
            try{
                LocalDate date = LocalDate.parse(dateString);
                String valueString = ((Map<String, String>)timeSeries.get(dateString)).get("4. close");
                Double value = Double.parseDouble(valueString);
                values.put(date, value);

            } catch(Exception e){
                log.error("can not pars value for date"+dateString+ " and symbol " + symbol);
                continue;
            }

        }
        return values;
    }


}
