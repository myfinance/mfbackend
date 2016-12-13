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

import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.RootSecurityProvider;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.service.InstrumentService;

import javax.inject.Inject;
import java.util.List;


//todo besser hier eine extra property erstellen die von IdentifiableResource erbt da dies restapi spezifisch ist
public class MarketDataEnvironmentImpl implements MarketDataEnvironment, IdentifiableResource {

    final private ProductService productService;
    final private InstrumentService instrumentService;
    final private RootSecurityProvider rootSecurityProvider;

    @Inject
    public MarketDataEnvironmentImpl(RootSecurityProvider rootSecurityProvider, ProductService productService, InstrumentService instrumentService){
        this.productService=productService;
        this.instrumentService=instrumentService;
        this.rootSecurityProvider=rootSecurityProvider;
    }

    @Override
    public ProductService getProductService() {
        return productService;
    }

    @Override
    public InstrumentService getInstrumentService() {
        return instrumentService;
    }

    @Override
    public RootSecurityProvider getRootSecurityProvider() {
        return rootSecurityProvider;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public List<String> getParentIdTrail() {
        return null;
    }

    @Override
    public Object getOpLevel() {
        return null;
    }
}
