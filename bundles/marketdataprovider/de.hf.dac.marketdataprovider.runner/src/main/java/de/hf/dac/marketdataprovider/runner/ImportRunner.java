/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ImportRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.runner;

import de.hf.dac.io.baserunner.BaseRestCommandLineRunner;
import de.hf.dac.io.baserunner.OptionsParser;

public class ImportRunner extends BaseRestCommandLineRunner {

    private static String[] cmdArgs = null;

    public ImportRunner(){
        super(new OptionsParser());
    }

    /**
     * @param args
     *        The command line arguments
     */
    public static void main(String[] args) {
        cmdArgs = args;
        try {
            new ImportRunner().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void addCustomCommandLineOptions() {

    }
}
