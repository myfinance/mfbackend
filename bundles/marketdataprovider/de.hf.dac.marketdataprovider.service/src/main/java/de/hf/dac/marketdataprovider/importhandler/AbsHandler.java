/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AbsHandler.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 12.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.importhandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.hf.dac.marketdataprovider.api.domain.Currency;
import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.Source;
import de.hf.dac.marketdataprovider.api.exceptions.MDException;
import de.hf.dac.marketdataprovider.api.exceptions.MDMsgKey;
import de.hf.dac.web.Http;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsHandler {

    Http downloadHandler;

    protected Map<LocalDate, EndOfDayPrice> convertToEndOfDayPrice(Map<LocalDate, Double> prices,
            LocalDate lastPricedDate,
            Currency currency,
            Source source,
            Security security,
            LocalDateTime ts){
        Map<LocalDate, EndOfDayPrice> eodPrices = new HashMap<>();
        if(lastPricedDate==null){
            lastPricedDate = LocalDate.MIN;
        }
        for (LocalDate date:prices.keySet()) {
            if(date.isAfter(lastPricedDate) && !eodPrices.containsKey(date) && prices.get(date)>0){
                eodPrices.put(date, new EndOfDayPrice(currency, security, source, date, prices.get(date), ts));
            }
        }

        return eodPrices;
    }

    protected Map<String, Object> getJsonMapFromUrl(String url) {
        String returnvalue;
        try {
            returnvalue=downloadHandler.getRequest(url);
        } catch (IOException e) {
            throw new MDException(MDMsgKey.NO_RESPONSE_FROM_URL_EXCEPTION, "no response form "+url, e);
        }
        if(returnvalue== null || returnvalue.isEmpty()){
            throw new MDException(MDMsgKey.NO_RESPONSE_FROM_URL_EXCEPTION, "empty response form "+url);
        }
        Gson gson = new Gson();

        return gson.fromJson(returnvalue, new TypeToken<Map<String, Object>>(){}.getType());
    }
}
