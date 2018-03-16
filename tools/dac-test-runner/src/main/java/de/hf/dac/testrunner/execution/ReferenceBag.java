/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ReferenceBag.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution;

import java.io.Serializable;
import java.util.Set;

public interface ReferenceBag extends Serializable {
    public <T> T get(String key);

    public Set<String> getKeys();

    void set(String key, Object value);
}

