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
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.SecuritySymbols;
import de.hf.dac.marketdataprovider.api.domain.SecurityType;
import de.hf.dac.marketdataprovider.api.exceptions.MDException;
import de.hf.dac.marketdataprovider.api.exceptions.MDMsgKey;
import de.hf.dac.web.Http;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Slf4j
public class AlphavantageHandler implements Handler{
    AlphavantageType type;
    String prefix;
    String postfix;
    Http downloadHandler;

    public AlphavantageHandler(AlphavantageType type,
            String prefix,
            String postfix,
            Http downloadHandler){

        this.type=type;
        this.prefix=prefix;
        this.postfix=postfix;
        this.downloadHandler=downloadHandler;
    }

    public Map<LocalDate, Double> importPrices(Security security){
        String url;
        Map<LocalDate, Double> values = new HashMap<>();
        SecurityType securityType = security.getSecurityType();
        if(type == AlphavantageType.EQ){
            if(!securityType.equals(SecurityType.EQUITY)) return values;

            Set<SecuritySymbols> symbols = security.getSecuritySymbols();
            if(symbols == null || symbols.size()==0){
                return values;
            }
            for (SecuritySymbols symbol:symbols) {
                String symbolString = symbol.getSymbol();
                values.putAll(getEQPrices(symbolString));
            }

        } else {
            if(!securityType.equals(SecurityType.CURRENCY)) return values;
            String currencyCode = ((Currency)security).getCurrencycode();
            url = prefix+currencyCode+postfix;
            Map<String, Object> map = getMapFromUrl(url);
            Map<String, String> info = (Map<String, String>)map.get("Realtime Currency Exchange Rate");
            try{
                LocalDate date = LocalDate.parse(info.get("6. Last Refreshed"));
                Double value = Double.parseDouble(info.get("5. Exchange Rate"));
                values.put(date, value);
            }catch(Exception e){
                log.error("can not pars value for Currency " + currencyCode);
            }
        }


        return values;
    }

    private Map<LocalDate, Double> getEQPrices(String symbol){
        String url = prefix+symbol+postfix;
        Map<LocalDate, Double> values = new HashMap<>();
        Map<String, Object> map = getMapFromUrl(url);
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

    private Map<String, Object> getMapFromUrl(String url) {
        String returnvalue;
        try {
            returnvalue=downloadHandler.getRequest(url);
        } catch (IOException e) {
            throw new MDException(MDMsgKey.NO_RESPONSE_FROM_URL_EXCEPTION, "no response form "+url, e);
        }
        Gson gson = new Gson();

        return gson.fromJson(returnvalue, new TypeToken<Map<String, Object>>(){}.getType());
    }
}
