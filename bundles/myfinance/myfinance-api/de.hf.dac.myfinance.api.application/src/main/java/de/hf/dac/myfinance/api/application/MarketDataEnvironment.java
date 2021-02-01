/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataEnvironment.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.application;

import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.PriceService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueService;

public interface MarketDataEnvironment {
    InstrumentService getInstrumentService();
    ValueService getValueService();
    TransactionService getTransactionService();
    PriceService getPriceService();
}
