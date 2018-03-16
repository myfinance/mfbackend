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

package de.hf.dac.myfinance.importhandler;

import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Security;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface Handler {
    Map<LocalDate, EndOfDayPrice> importPrices(Security security, LocalDate lastPricedDate, LocalDateTime ts);
}
