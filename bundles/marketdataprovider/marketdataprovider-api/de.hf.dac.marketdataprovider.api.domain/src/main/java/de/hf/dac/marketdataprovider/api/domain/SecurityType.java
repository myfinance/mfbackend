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
    EQUITY(new Long(1));

    private final Long value;

    SecurityType(final Long newValue) {
        value = newValue;
    }

    public Long getValue() { return value; }
}

