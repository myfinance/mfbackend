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

import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.SecuritySymbols;
import de.hf.dac.marketdataprovider.api.domain.Source;
import de.hf.dac.marketdataprovider.api.domain.SourceName;
import de.hf.dac.marketdataprovider.api.exceptions.MDException;
import de.hf.dac.marketdataprovider.api.exceptions.MDMsgKey;
import de.hf.dac.web.Http;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ImportHandler {
    Http downloadHandler;
    Map<Integer, Handler> importHandler = new HashMap<>();

    public ImportHandler(List<Source> sources, Boolean useProxy, String proxyUrl, int proxyPort, String proxyUser, String proxyPw){
        downloadHandler = new Http(10000,
            useProxy,
            proxyUrl,
            proxyPort,
            proxyUser,
            proxyPw);
        for(Source source : sources){
            if(source.getDescription().equals(SourceName.ALPHAVANTAGEEQ)) {
                AlphavantageHandler handler = new AlphavantageHandler(AlphavantageType.EQ,
                    source.getUrlprefix(),
                    source.getUrlpostfix(),
                    downloadHandler);
                importHandler.put(source.getPrio(), handler);
            } else if (source.getDescription().equals(SourceName.ALPHAVANTAGEFX)) {
                AlphavantageHandler handler = new AlphavantageHandler(AlphavantageType.FX,
                    source.getUrlprefix(),
                    source.getUrlpostfix(),
                    downloadHandler);
                importHandler.put(source.getPrio(), handler);
            }
        }
    }

    public LocalDate importSource(LocalDate lastPricedDay, Security security){
        SortedSet<Integer> keys = new TreeSet<Integer>(importHandler.keySet());
        for (Integer key : keys) {
            Handler handler = importHandler.get(key);
            handler.importPrices(security);
        }

        Map<LocalDate, Double> prices = handler.importPrices("MSFT");
        return prices;
    }
}
