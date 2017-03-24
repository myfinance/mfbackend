/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : StringJobResult.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

public class StringJobResult extends BaseJobResult<String> {

    public StringJobResult(String result, String routingID) {
        super(routingID, result);
    }
}
