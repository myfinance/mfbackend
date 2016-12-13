/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DacPrincipal.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 09.12.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

public interface DacPrincipal {

    String getProperty(String propertyName);
    String getName();
}
