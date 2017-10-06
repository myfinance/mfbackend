/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BasePluginAction.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.plugins.base;

import de.hf.dac.testrunner.execution.plugin.ActionField;
import de.hf.dac.testrunner.execution.plugin.Plugin;
import de.hf.dac.testrunner.execution.plugin.PluginAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class BasePluginAction implements PluginAction {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private final Plugin plugin;

    private StringBuffer lastMessage = new StringBuffer();

    @ActionField(name = "shouldFail", description = "Test Steps may be marked to fail as expected. llowing for negative tests.")
    private boolean shouldFail;

    public BasePluginAction(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    protected void setLastMessage(String msg) {
        log.debug("Replace LastMessage {} to {}", lastMessage.toString(), lastMessage.toString());
        lastMessage.setLength(0);
        lastMessage.append(msg);
    }

    @Override
    public String getLastMessage() {
        String result = lastMessage.toString();
        lastMessage.setLength(0);
        return result;
    }

    @Override
    public boolean shouldFail() {
        return shouldFail;
    }
}

