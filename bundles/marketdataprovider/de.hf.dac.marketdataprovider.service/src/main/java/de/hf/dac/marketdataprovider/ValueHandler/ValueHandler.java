/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ValueHandler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.ValueHandler;

import de.hf.dac.marketdataprovider.api.domain.Instrument;

import java.time.LocalDate;
import java.util.TreeMap;

public interface ValueHandler {
    TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument);
}
