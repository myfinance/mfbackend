/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataSystemDescriptor.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 13.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.restservice;

public class MarketDataSystemDescriptor {
    public static final String KEY = "ACCESSTYPE";
    public static final String VALUE = "marketdataprovider";
    public static final String FILTER = "(&(ACCESSTYPE=marketdataprovider))";
}
