/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2013, ... All Rights Reserved
 *
 *
 *  Project     : domain
 *
 *  File        : SecurityType.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.12.2013
 *
 * ----------------------------------------------------------------------------
 */
 package de.hf.dac.marketdataprovider.api.domain;

public enum SecurityType {
    EQUITY(new Integer(1)),
    CURRENCY(new Integer(2)),
    UNKNOWN(new Integer(99));

    private final Integer value;

    SecurityType(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }
}

