/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ImportRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 02.03.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner;

import de.hf.dac.myfinance.api.runner.BaseMFRunnerParameter;
import de.hf.dac.myfinance.api.runner.Runner;
import de.hf.dac.myfinance.api.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class ImportRunner implements Runner {
    public static final Logger LOG = LoggerFactory.getLogger(ImportRunner.class);
    @Inject
    PriceService service;

    @Override
    public void run(BaseMFRunnerParameter params) {
        LOG.info("import... ");
        service.importPrices(LocalDateTime.now());
        LOG.info("import sucessful");
    }
}
