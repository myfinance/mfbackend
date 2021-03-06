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

package de.hf.dac.myfinance.importhandler;

import de.hf.dac.api.io.web.WebRequestService;
import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.Source;
import de.hf.dac.myfinance.api.domain.SourceName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImportHandler {

    WebRequestService downloadHandler;
    List<Handler> importHandler = new ArrayList<>();

    public ImportHandler(List<Source> sources, Instrument eur, WebRequestService downloadHandler){
        this.downloadHandler = downloadHandler;
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

    public Map<LocalDate, EndOfDayPrice> importSource(Instrument security, LocalDate lastPricedDate, LocalDateTime ts){
        Map<LocalDate, EndOfDayPrice> prices = new HashMap<>();
        for (Handler handler : importHandler) {
            prices.putAll(handler.importPrices(security, lastPricedDate, ts));
        }
        return prices;
    }


}
