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

package de.hf.dac.marketdataprovider.api.domain;

public enum InstrumentType {
    SECURITY(new Long(1));

    private final Long value;

    InstrumentType(final Long newValue) {
        value = newValue;
    }

    public Long getValue() { return value; }
}
