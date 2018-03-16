/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDEnvironmentContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 18.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.application.servicecontext;

import de.hf.dac.myfinance.api.application.MarketDataEnvironment;
import de.hf.dac.myfinance.api.application.ServiceResourceType;

public interface MDEnvironmentContext extends ServiceResourceType {

    MarketDataEnvironment getMarketDataEnvironment();

}
