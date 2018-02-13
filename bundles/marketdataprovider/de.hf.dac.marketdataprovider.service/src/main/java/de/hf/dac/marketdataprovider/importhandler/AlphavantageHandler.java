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

package de.hf.dac.marketdataprovider.importhandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.hf.dac.marketdataprovider.api.domain.Currency;
import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.SecuritySymbols;
import de.hf.dac.marketdataprovider.api.domain.SecurityType;
import de.hf.dac.marketdataprovider.api.domain.Source;
import de.hf.dac.web.Http;
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
    Currency eur;

    public AlphavantageHandler(AlphavantageType type,
            Source source,
            Http downloadHandler, Currency eur){

        this.type=type;
        this.source = source;
        this.downloadHandler=downloadHandler;
        this.eur = eur;
    }

    public Map<LocalDate, EndOfDayPrice> importPrices(Security security, LocalDate lastPricedDate, LocalDateTime ts){
        Map<LocalDate, EndOfDayPrice> values = new HashMap<>();
        SecurityType securityType = security.getSecurityType();
        if(type == AlphavantageType.EQ || type == AlphavantageType.EQFULL){
            values.putAll(getEqPricesForSymbols(security, lastPricedDate, ts, securityType));

        } else {
            if(!securityType.equals(SecurityType.CURRENCY)) return values;
            try{
                values.putAll(convertToEndOfDayPrice(getFxPrices((Currency) security), lastPricedDate,
                    eur, source,
                    security,
                    ts));
            }catch(Exception e){
                log.error("can not load prices for currency " +security.getIsin() + ":"+e);
            }
        }

        return values;
    }

    private Map<LocalDate, EndOfDayPrice> getEqPricesForSymbols(Security security, LocalDate lastPricedDate, LocalDateTime ts, SecurityType securityType) {
        Map<LocalDate, EndOfDayPrice> values = new HashMap<>();
        if(!securityType.equals(SecurityType.EQUITY))
            return values;

        Set<SecuritySymbols> symbols = security.getSecuritySymbols();
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
                log.error("can not load prices for Security " +security.getIsin() + " and Symbol " + symbol.getSymbol() + ":"+e);
            }

        }
        return values;
    }

    private Map<LocalDate, Double> getFxPrices(Currency security) {
        String url;
        Map<LocalDate, Double> prices = new HashMap<>();
        String currencyCode = security.getCurrencycode();
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
