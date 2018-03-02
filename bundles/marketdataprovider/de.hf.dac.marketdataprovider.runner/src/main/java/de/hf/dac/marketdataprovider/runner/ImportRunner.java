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

package de.hf.dac.marketdataprovider.runner;

import de.hf.dac.marketdataprovider.api.runner.BaseMDRunnerParameter;
import de.hf.dac.marketdataprovider.api.runner.Runner;
import de.hf.dac.marketdataprovider.api.service.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class ImportRunner implements Runner {
    public static final Logger LOG = LoggerFactory.getLogger(ImportRunner.class);
    @Inject
    InstrumentService service;

    @Override
    public void run(BaseMDRunnerParameter params) {
        LOG.info("import... ");
        service.importPrices(LocalDateTime.now());
        LOG.info("import sucessful");
    }
}
