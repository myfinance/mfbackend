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

package de.hf.dac.marketdataprovider.application.commands;

import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironmentBuilder;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

import javax.inject.Inject;
import java.util.List;

@Command(scope = "marketdata", name = "listproduct")
public class ProductListCommand extends EnvironmentBasedCommand<ProductService>{

    private MarketDataEnvironmentBuilder marketDataEnvironmentBuilder;
    @Inject
    public void setMarketDataEnvironmentBuilder(MarketDataEnvironmentBuilder marketDataEnvironmentBuilder){
        this.marketDataEnvironmentBuilder = marketDataEnvironmentBuilder;
    }

    @Override
    public Object execute(CommandSession commandSession) throws Exception {
        MarketDataEnvironment marketDataEnvironment = marketDataEnvironmentBuilder.build(env);
        List<Product> products = marketDataEnvironment.getProductService().listProducts();
        for (Product product : products) {
            System.out.println(product);
        }
        return "OK";
    }
}
