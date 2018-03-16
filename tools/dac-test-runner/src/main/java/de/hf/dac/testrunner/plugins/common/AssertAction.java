/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AssertAction.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.common;

import de.hf.dac.testrunner.execution.plugin.ActionField;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.plugins.base.BasePluginAction;

public abstract class AssertAction extends BasePluginAction {

    @ActionField(name="condition", mandatory = true ,description = "Evaluation expression")
    String condition;

    public AssertAction(Plugin plugin) {
        super(plugin);
    }

}
