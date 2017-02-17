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
 *  Created     : 17.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.runner;


public class ImportRunner extends BaseMDRunner {

    /**
     * @param args
     *        The command line arguments
     */
    public static void main(String[] args) {

        try {
            new ImportRunner().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
