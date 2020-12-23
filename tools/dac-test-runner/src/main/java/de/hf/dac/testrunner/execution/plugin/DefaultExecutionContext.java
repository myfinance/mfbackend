/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DefaultExecutionContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution.plugin;

import de.hf.dac.testrunner.execution.ExecutionContext;
import de.hf.dac.testrunner.execution.ReferenceBag;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExecutionContext implements ExecutionContext {



    protected final Properties properties;
    private ReferenceBag bag;
    private Plugin plugin;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExecutionContext.class);


    public DefaultExecutionContext(String env, Plugin plugin) {
        this.plugin = plugin;
        this.properties  = new Properties();
        try {
            InputStream is = getClass().getResourceAsStream("/testenv/" + plugin.getName() + "/" + env + ".properties");
            if (is != null)
                properties.load(is);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }



    @Override
    public void setReferenceBag(ReferenceBag references) {
        this.bag = references;
    }

    @Override
    public ReferenceBag getReferenceBag() {
        return bag;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    public Map<String,Object> getProperties() {
        HashMap<String, Object> result = new HashMap<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            Object o = enumeration.nextElement();
            result.put((String) o, properties.getProperty((String) o));
        }

        return result;
    }
}
