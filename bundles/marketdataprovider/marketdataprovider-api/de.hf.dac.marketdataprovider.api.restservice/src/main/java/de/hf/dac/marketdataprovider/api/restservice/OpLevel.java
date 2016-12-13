package de.hf.dac.marketdataprovider.api.restservice;

/** ----------------------------------------------------------------------------
 *
 * ---         HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : OpLevel.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.12.2016
 *
 * ----------------------------------------------------------------------------
 */

public enum OpLevel {
    environment,
    instrument,
    position,
    portfolio, //a type of trades like loads or real estate
    account
}
