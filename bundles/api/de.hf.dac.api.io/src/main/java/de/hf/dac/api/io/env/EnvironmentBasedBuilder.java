/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentBasedBuilder.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 29.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.env;

import java.sql.SQLException;
import java.util.List;

public interface EnvironmentBasedBuilder<T> {
    T build(String environment);
    List<String> getInfo();
}
