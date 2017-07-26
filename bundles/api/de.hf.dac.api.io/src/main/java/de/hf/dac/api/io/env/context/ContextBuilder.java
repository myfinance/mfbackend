/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ContextBuilder.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.env.context;

import com.google.inject.Module;

public interface ContextBuilder {
    ApplicationContext build(String contextID, Module[] provider);
    void clearCache();
    void remove(String id);
}

