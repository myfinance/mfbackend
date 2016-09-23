/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentConfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 22.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.env;

import java.util.Properties;

public interface EnvironmentConfiguration {

    String decrypt(String value);

    String getString(String section, String key);

    String getString(String section, String key, String def);

    boolean getBoolean(String section, String key, boolean def);

    Properties getProperties(String section);
}
