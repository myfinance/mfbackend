/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : Handler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.importhandler;

import de.hf.dac.marketdataprovider.api.domain.Security;
import de.hf.dac.marketdataprovider.api.domain.SecurityType;

import java.time.LocalDate;
import java.util.Map;

public interface Handler {
    Map<LocalDate, Double> importPrices(Security security);
}
