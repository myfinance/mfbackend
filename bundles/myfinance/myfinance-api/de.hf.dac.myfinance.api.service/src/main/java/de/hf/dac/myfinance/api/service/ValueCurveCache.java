/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ValueCurveCache.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.service;

import java.time.LocalDate;
import java.util.TreeMap;

public interface ValueCurveCache {
    TreeMap<LocalDate, Double> getValueCurve(int instrumentId);
    void addValueCurve(int instrumentId, TreeMap<LocalDate, Double> valueCurve);
    void removeCurve(int instrumentId);
    void flush();
}
