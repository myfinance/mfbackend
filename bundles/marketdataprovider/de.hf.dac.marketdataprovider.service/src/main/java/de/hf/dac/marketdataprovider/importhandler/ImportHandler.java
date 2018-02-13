/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ImportHandler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.importhandler;

import de.hf.dac.marketdataprovider.api.domain.Currency;
import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.Source;
import de.hf.dac.marketdataprovider.api.domain.SourceName;
import de.hf.dac.web.Http;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportHandler {
    Http downloadHandler;
   List<Handler> importHandler = new ArrayList<>();

    public ImportHandler(List<Source> sources, Boolean useProxy, String proxyUrl, int proxyPort, String proxyUser, String proxyPw,
            Currency eur){
        downloadHandler = new Http(30000,
            useProxy,
            proxyUrl,
            proxyPort,
            proxyUser,
            proxyPw);
        //add all and only active sources
        for(Source source : sources){
            if(source.getDescription().equals(SourceName.ALPHAVANTAGEEQ.name())) {
                AlphavantageHandler handler = new AlphavantageHandler(AlphavantageType.EQ,
                    source,
                    downloadHandler, eur);
                importHandler.add(handler);
            } else if (source.getDescription().equals(SourceName.ALPHAVANTAGEFX.name())) {
                AlphavantageHandler handler = new AlphavantageHandler(AlphavantageType.FX,
                    source,
                    downloadHandler, eur);
                importHandler.add(handler);
            }else if (source.getDescription().equals(SourceName.ALPHAVANTAGEEQFULL.name())) {
                AlphavantageHandler handler = new AlphavantageHandler(AlphavantageType.EQFULL,
                    source,
                    downloadHandler, eur);
                importHandler.add(handler);
            }
        }
    }

    public Map<LocalDate, EndOfDayPrice> importSource(Security security, LocalDate lastPricedDate, LocalDateTime ts){
        Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
        for (Handler handler : importHandler) {
            prices.putAll(handler.importPrices(security, lastPricedDate, ts));
        }
        return prices;
    }


}
