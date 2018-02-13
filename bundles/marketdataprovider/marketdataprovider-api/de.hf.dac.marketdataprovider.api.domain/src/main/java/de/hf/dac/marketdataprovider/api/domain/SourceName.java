/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SourceName.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 09.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.domain;

public enum SourceName {
    MAN(new Integer(1)),
    ALPHAVANTAGEEQ(new Integer(2)),
    ALPHAVANTAGEFX(new Integer(3)),
    ALPHAVANTAGEEQFULL(new Integer(4));

    private final Integer value;

    SourceName(final Integer newValue) {
        value = newValue;
    }

    public Integer getValue() { return value; }
}

