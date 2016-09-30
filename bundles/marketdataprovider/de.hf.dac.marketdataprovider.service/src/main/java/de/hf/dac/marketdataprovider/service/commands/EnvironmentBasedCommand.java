/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentBasedCommand.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 28.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service.commands;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;

abstract public class EnvironmentBasedCommand<T> implements Action {

    @Argument(name = "env", description = "which environment", multiValued = false, required = true, index = 0)
    protected String env;

    //@Argument(name= "target", description = "which target", multiValued = false, required = true, index = 1)
    //protected String target;

    abstract protected T build() throws java.sql.SQLException;
}