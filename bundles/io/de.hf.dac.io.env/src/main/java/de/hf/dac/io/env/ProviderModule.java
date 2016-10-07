/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ProviderModule.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import com.google.inject.AbstractModule;
import de.hf.dac.api.io.env.context.ApplicationContext;

public class ProviderModule extends AbstractModule {
    private final ApplicationContext ctxt;

    public ProviderModule(ApplicationContext ctxt) {
        this.ctxt = ctxt;
    }

    @Override
    protected void configure() {

        // finally bind the context itself
        bind(ApplicationContext.class).toInstance(ctxt);
    }
}
