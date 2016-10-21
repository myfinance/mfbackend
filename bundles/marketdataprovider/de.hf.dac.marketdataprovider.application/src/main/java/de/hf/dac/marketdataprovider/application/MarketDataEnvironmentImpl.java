/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataEnvironmentImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.application;

import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.service.ProductService;

import javax.inject.Inject;

public class MarketDataEnvironmentImpl implements MarketDataEnvironment {

    @Inject
    ProductService productService;

    @Override
    public ProductService getProductService() {
        return productService;
    }
}
