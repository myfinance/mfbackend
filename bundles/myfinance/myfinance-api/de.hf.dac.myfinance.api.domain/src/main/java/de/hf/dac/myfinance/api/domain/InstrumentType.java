/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : InstrumentType.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 02.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.domain;

public enum InstrumentType {
    SECURITY(new Integer(1)),
    UNKNOWN(new Integer(99));

    private final Integer value;

    InstrumentType(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }
}
