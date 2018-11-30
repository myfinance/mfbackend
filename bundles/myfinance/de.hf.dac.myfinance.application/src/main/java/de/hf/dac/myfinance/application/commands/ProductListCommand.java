/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ProductListCommand.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 28.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.commands;

import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.service.InstrumentService;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

import javax.inject.Inject;
import java.util.List;

@Command(scope = "marketdata", name = "listproduct")
public class ProductListCommand extends EnvironmentBasedCommand<InstrumentService>{

    private MarketDataEnvironmentBuilder marketDataEnvironmentBuilder;
    @Inject
    public void setMarketDataEnvironmentBuilder(MarketDataEnvironmentBuilder marketDataEnvironmentBuilder){
        this.marketDataEnvironmentBuilder = marketDataEnvironmentBuilder;
    }

    @Override
    public Object execute(CommandSession commandSession) throws Exception {
        MarketDataEnvironment marketDataEnvironment = marketDataEnvironmentBuilder.build(env);
        List<Instrument> instruments = marketDataEnvironment.getInstrumentService().listInstruments();
        for (Instrument instrument : instruments) {
            System.out.println(instrument);
        }
        return "OK";
    }
}
