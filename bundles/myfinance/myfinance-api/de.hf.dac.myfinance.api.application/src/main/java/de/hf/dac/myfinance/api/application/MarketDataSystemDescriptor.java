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
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.application;

public class MarketDataSystemDescriptor {
    private MarketDataSystemDescriptor(){}

    public static final String CONTEXT = "MarketDataEnvironment";
    public static final String KEY = "ACCESSTYPE";
    public static final String VALUE = "myfinance";
    public static final String FILTER = "(&(ACCESSTYPE=myfinance))";
}
