/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MdResfileExecutionContext.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.test.plugin;

import de.hf.dac.io.baserunner.OptionsParser;
import de.hf.dac.io.config.resfile.ResFileParser;
import de.hf.dac.testrunner.execution.ReferenceBag;
import de.hf.dac.testrunner.execution.plugin.DefaultExecutionContext;
import de.hf.dac.testrunner.execution.plugin.Plugin;

import java.util.Map;

public class MdResfileExecutionContext extends DefaultExecutionContext {

    private static final String RES_FILE_SECTION_PREFIX = "API_CLIENT_ENV_";

    private final String env;

    protected final ResFileParser parser;

    public MdResfileExecutionContext(Plugin plugin, String env) {
        super(env, plugin);
        this.env = env;
        OptionsParser p = new OptionsParser();
        parser = new ResFileParser();

        parser.load("./src/test/resources/dac.res");
    }

    @Override
    public ReferenceBag getReferenceBag() {
        // copy all props from Res File into reference bag for this invocation
        ReferenceBag bagRef = super.getReferenceBag();
        parser.getProperties(RES_FILE_SECTION_PREFIX + env).forEach((key, value) -> bagRef.set(key, value));
        return bagRef;
    }

    public Map getProperties() {
        return parser.getProperties(RES_FILE_SECTION_PREFIX + env);
    }

    public String get(String name, String def) {
        String result = parser.getProperties(MdResfileExecutionContext.RES_FILE_SECTION_PREFIX + env).get(name);
        if (result == null) {
            return def;
        }
        return result;
    }



}
