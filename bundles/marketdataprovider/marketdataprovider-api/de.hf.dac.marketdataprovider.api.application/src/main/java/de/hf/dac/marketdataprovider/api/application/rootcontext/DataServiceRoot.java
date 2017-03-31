/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DataServiceRoot.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 31.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.application.rootcontext;

import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.Secured;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.OpLevel;

import java.sql.SQLException;
import java.util.List;

public interface DataServiceRoot  extends IdentifiableResource<OpLevel>, Secured {

    MarketDataEnvironment getMarketDataEnvironment(String env) throws SQLException;
    List<String> getEnvironmentInfo();
}
