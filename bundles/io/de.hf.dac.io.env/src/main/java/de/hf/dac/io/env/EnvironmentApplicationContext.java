/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentApplicationContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;

import com.google.inject.Module;
import de.hf.dac.api.io.env.context.ApplicationContext;

public class EnvironmentApplicationContext extends BaseGuiceApplicationContext implements ApplicationContext {

    public EnvironmentApplicationContext(Module[] providers) {
        super(providers);
    }

}

